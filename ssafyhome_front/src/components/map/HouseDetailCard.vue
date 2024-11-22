<script setup>
import { ref, watch, computed } from "vue";
import { getDealsByAptSeq, getDealsBySpace } from "@/api/house.js";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineController,
} from "chart.js";
import { LineChart } from "vue-chart-3";

// Chart.js 플러그인 등록
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineController
);

// Props로 전달된 집 정보
const props = defineProps({
  selectedHouse: {
    type: Object,
    required: true,
  },
});

// 탭 상태 및 차트 상태
const tabs = ["매매", "전세", "월세"];
const selectedTab = ref("매매");

// 평형 옵션
const spaceOptions = ref([]);
const selectedSpace = ref(null);
const dealList = ref([]);

// 평당가 계산 함수
const calculatePerPyeong = (dealAmount, space) => {
  if (!dealAmount || !space) return "N/A";
  return (dealAmount / space).toFixed(0);
};

// 최근 거래 정보 가져오기
const fetchDealsByAptSeq = () => {
  getDealsByAptSeq(
    props.selectedHouse.aptSeq,
    selectedTab.value,
    (response) => {
      dealList.value = response.data;

      // 제곱미터 옵션 생성
      const uniqueSpaces = Array.from(
        new Set(response.data.map((deal) => deal.excluUseAr))
      ).sort((a, b) => a - b); // 제곱미터 값 오름차순 정렬

      // 제곱미터 옵션 설정
      spaceOptions.value = uniqueSpaces.map((space) => ({
        text: `${space}㎡`,
        value: space,
      }));
    },
    (error) => console.error(error)
  );
};

// 제곱미터 선택 시 거래 정보 가져오기
const fetchDealsBySpace = (space) => {
  getDealsBySpace(
    props.selectedHouse.aptSeq,
    selectedTab.value,
    space,
    (response) => {
      dealList.value = response.data;
    },
    (error) => console.error(error)
  );
};

// 초기 데이터 로드
fetchDealsByAptSeq();

// 평형 변경 감지
watch(selectedSpace, (newSpace) => {
  if (newSpace !== null) {
    fetchDealsBySpace(newSpace);
  } else if (spaceOptions.value.length > 0) {
    selectedSpace.value = spaceOptions.value[0].value; // 첫 번째 옵션 자동 선택
  }
});

// 거래 종류에 따라 데이터 재로드
watch(selectedTab, (newTab) => {
  console.log("바뀐 탭 : ", newTab);
  fetchDealsByAptSeq();

  // `selectedSpace`가 null인 경우 첫 번째 옵션을 설정
  if (!selectedSpace.value && spaceOptions.value.length > 0) {
    selectedSpace.value = spaceOptions.value[0].value;
  }

  // 선택된 공간(제곱미터)에 해당하는 거래 정보 로드
  if (selectedSpace.value !== null) {
    fetchDealsBySpace(selectedSpace.value);
  }
});

// 최고/최저 거래가를 저장할 상태
const threeYearHighLow = ref({ maxDealAmount: "N/A", minDealAmount: "N/A" });

// 최고/최저 거래가 계산 함수
const calculateHighLow = () => {
  if (dealList.value.length === 0) {
    threeYearHighLow.value = {
      maxDealAmount: "N/A",
      minDealAmount: "N/A",
      avgDealAmount: "N/A",
    };
    return;
  }

  // 현재 연도와 최근 3년 기준 연도 계산
  const currentYear = new Date().getFullYear();
  const threeYearsAgo = currentYear - 3;

  // 최근 3년의 데이터 필터링
  const filteredDeals = dealList.value.filter(
    (deal) => deal.dealYear >= threeYearsAgo && deal.dealYear <= currentYear
  );

  if (filteredDeals.length === 0) {
    threeYearHighLow.value = {
      maxDealAmount: "N/A",
      minDealAmount: "N/A",
      avgDealAmount: "N/A",
    };
    return;
  }

  // 필터링된 데이터에서 거래 금액 추출 및 계산
  const amounts = filteredDeals.map((deal) =>
    parseInt(deal.dealAmount.replace(/,/g, ""), 10)
  );

  // 평균값 계산
  const avgAmount =
    amounts.reduce((sum, amount) => sum + amount, 0) / amounts.length;

  threeYearHighLow.value = {
    maxDealAmount: Math.max(...amounts),
    minDealAmount: Math.min(...amounts),
    avgDealAmount: avgAmount, // 평균값 추가
  };
};

// `dealList` 변경 시 최고/최저 거래가 계산
watch(dealList, calculateHighLow);

// 선택된 아파트 데이터에 반응
watch(
  () => props.selectedHouse,
  (newVal) => {
    fetchDealsByAptSeq();
    console.log("Selected house updated:", newVal);
    // 필요하면 추가 로직 처리
  }
);

// `spaceOptions`와 `selectedSpace` 동기화
watch(spaceOptions, (newOptions) => {
  if (newOptions.length > 0) {
    selectedSpace.value = newOptions[0].value; // 첫 번째 옵션 자동 선택
  }
});

watch(
  () => props.selectedHouse,
  (newHouse) => {
    if (newHouse && spaceOptions.value.length > 0) {
      selectedSpace.value = spaceOptions.value[0].value; // 첫 번째 옵션 자동 선택
    }
  }
);

// 차트 데이터
const chartData = ref({
  labels: [], // X축 레이블 (거래 연월)
  datasets: [
    {
      label: "거래 금액 (억원)",
      data: [], // 거래 금액 데이터
      borderColor: "#42A5F5",
      backgroundColor: "rgba(66, 165, 245, 0.2)",
      fill: true,
    },
  ],
});

// 차트 옵션
const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "최근 3년 거래 금액 추이",
    },
  },
};

// 차트 데이터 업데이트 함수
const updateChartData = () => {
  const labels = dealList.value.map(
    (deal) => `${deal.dealYear}-${deal.dealMonth}`
  );
  const data = dealList.value.map((deal) => {
    const amount = parseInt(deal.dealAmount.replace(/,/g, ""), 10);
    return (amount / 10000).toFixed(1); // 1억 단위로 변환
  });

  chartData.value.labels = labels;
  chartData.value.datasets[0].data = data;
};

// `dealList` 변경 감지 및 차트 데이터 업데이트
watch(
  dealList,
  () => {
    updateChartData();
  },
  { immediate: true } // 초기 실행
);

// 차트 데이터
const depositChartData = ref({
  labels: [], // X축 레이블 (거래 연월)
  datasets: [
    {
      label: "보증금 (억원)",
      data: [], // 보증금 데이터
      borderColor: "#42A5F5",
      backgroundColor: "rgba(66, 165, 245, 0.2)",
      fill: true,
    },
  ],
});

const rentChartData = ref({
  labels: [], // X축 레이블 (거래 연월)
  datasets: [
    {
      label: "월세 (만원)",
      data: [], // 월세 데이터
      borderColor: "#66BB6A",
      backgroundColor: "rgba(102, 187, 106, 0.2)",
      fill: true,
    },
  ],
});

// 차트 옵션
const commonChartOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "최근 3년 거래 금액 추이",
    },
  },
};

// 월세 차트 데이터 업데이트 함수
const updateMonthlyChartData = () => {
  const labels = dealList.value.map(
    (deal) => `${deal.dealYear}-${deal.dealMonth}`
  );

  // 보증금
  const depositData = dealList.value.map((deal) => {
    const amount = parseInt(deal.dealAmount.replace(/,/g, ""), 10);
    return (amount / 10000).toFixed(1); // 1억 단위로 변환
  });

  // 월세
  const rentData = dealList.value.map((deal) => {
    console.log("월세 deal 객체 : ", deal);
    const fee = parseInt(deal.feeAmount.replace(/,/g, ""), 10);
    return fee; // 만원 단위
  });

  depositChartData.value.labels = labels;
  depositChartData.value.datasets[0].data = depositData;

  rentChartData.value.labels = labels;
  rentChartData.value.datasets[0].data = rentData;
};

// 거래 리스트 변경 시 차트 데이터 업데이트
watch(
  dealList,
  () => {
    if (selectedTab.value === "월세") {
      updateMonthlyChartData();
    } else {
      updateChartData(); // 기존의 "매매" 또는 "전세" 차트
    }
  },
  { immediate: true }
);

// 모달 상태 관리
const isModalOpen = ref(false);

// 카카오 지도 링크 생성
const kakaoMapLink = computed(() => {
  if (
    props.selectedHouse.aptNm &&
    props.selectedHouse.latitude &&
    props.selectedHouse.longitude
  ) {
    return `https://map.kakao.com/link/to/${props.selectedHouse.aptNm},${props.selectedHouse.latitude},${props.selectedHouse.longitude}`;
  }
  return "#";
});

// 모달 열기/닫기 함수
const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

console.log("selectedHouse : ", props.selectedHouse);
</script>

<template>
  <div class="house-detail-card">
    <!-- Tabs -->
    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab"
        :class="{ active: selectedTab === tab }"
        @click="selectedTab = tab"
      >
        {{ tab }}
      </button>
    </div>

    <!-- 단지 정보 -->
    <div class="house-info">
      <h3 class="house-name">{{ selectedHouse.aptNm }}</h3>
    </div>

    <!-- 카카오 길찾기 버튼 -->
    <div class="kakao-map-container">
      <button class="kakao-map-button" @click="openModal">
        <span class="kakao">KaKao</span>
        <span class="path">길찾기</span>
      </button>
    </div>

    <!-- 제곱미터 정보 선택 -->
    <div class="space-select">
      <select v-model="selectedSpace" class="custom-select">
        <option
          v-for="option in spaceOptions"
          :key="option.value"
          :value="option.value"
        >
          {{ option.text }}
        </option>
      </select>
    </div>

    <!-- Transaction Info -->
    <div class="transaction-info">
      <!-- 실거래가 평균 -->
      <div class="info-row">
        <div class="info-title">
          <span>최근 실거래가 평균</span>
        </div>
        <div class="info-content">
          <span class="highlight">
            {{
              threeYearHighLow.avgDealAmount !== "N/A"
                ? (threeYearHighLow.avgDealAmount * 0.0001).toFixed(2) + "억"
                : "-"
            }}
          </span>
          <span class="sub-info">
            평당가
            {{
              threeYearHighLow.avgDealAmount !== "N/A"
                ? calculatePerPyeong(
                    threeYearHighLow.avgDealAmount,
                    selectedSpace
                  ) + "만원"
                : "-"
            }}
          </span>
        </div>
      </div>

      <!-- 거래가 정보 -->
      <div class="info-row">
        <div class="info-title">
          <span>거래가</span>
          <span class="span-recent">최근 3년</span>
        </div>
        <div class="info-content">
          <div class="info-text">
            <span class="highlight red">최고 </span>
            <span class="amount">
              {{
                threeYearHighLow.maxDealAmount !== "N/A"
                  ? (threeYearHighLow.maxDealAmount * 0.0001).toFixed(2)
                  : "-"
              }}억
            </span>
          </div>
          <div class="info-text">
            <span class="highlight blue">최저 </span>
            <span class="amount">
              {{
                threeYearHighLow.minDealAmount !== "N/A"
                  ? (threeYearHighLow.minDealAmount * 0.0001).toFixed(2)
                  : "-"
              }}억
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Chart Section -->
    <div class="chart-container">
      <!-- 월세일 때 보증금과 월세 차트 -->
      <template v-if="selectedTab === '월세'">
        <div class="chart-placeholder">
          <LineChart
            :chart-data="depositChartData"
            :chart-options="commonChartOptions"
          />
        </div>
        <div class="chart-placeholder" style="margin-top: 150px">
          <LineChart
            :chart-data="rentChartData"
            :chart-options="commonChartOptions"
          />
        </div>
      </template>

      <!-- 매매 또는 전세일 때 기본 차트 -->
      <template v-else>
        <div class="chart-placeholder">
          <LineChart
            :chart-data="chartData"
            :chart-options="commonChartOptions"
          />
        </div>
      </template>
    </div>

    <!-- 실거래가 히스토리 -->
    <div class="deal-history">
      <div class="history-header">
        <h3>실거래가 히스토리</h3>
        <span>총 {{ dealList.length }}건</span>
      </div>
      <table class="history-table">
        <thead>
          <tr>
            <th>계약일</th>
            <th>거래종류</th>
            <th>층수</th>
            <th>거래금액</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="deal in [...dealList].reverse()" :key="deal.no">
            <td>
              {{ deal.dealYear }}.{{
                deal.dealMonth.toString().padStart(2, "0")
              }}.{{ deal.dealDay.toString().padStart(2, "0") }}
            </td>
            <td>{{ selectedTab }}</td>
            <td>{{ deal.floor }}</td>
            <td>
              <template v-if="selectedTab === '월세'">
                {{ deal.dealAmount }}만 / {{ deal.feeAmount }}만
              </template>
              <template v-else>
                {{
                  (
                    parseInt(deal.dealAmount.replace(/,/g, ""), 10) * 0.0001
                  ).toFixed(2)
                }}억
              </template>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 모달 -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal">
        <button class="modal-close" @click="closeModal">닫기</button>
        <iframe
          :src="kakaoMapLink"
          width="100%"
          height="800px"
          frameborder="0"
          allowfullscreen
        ></iframe>
      </div>
    </div>
  </div>
</template>

<style scoped>
.house-detail-card {
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.tabs {
  display: flex;
  border-bottom: 1px solid #ddd;
  margin-bottom: 16px;
}

.tabs button {
  flex: 1;
  padding: 12px;
  font-size: 16px;
  background: none;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
}

.tabs button.active {
  font-weight: bold;
  color: #333;
  border-bottom: 2px solid #4285f4;
}

/* 아파트 정보 */
.house-info {
  text-align: center;
  margin-bottom: 20px;
}

.house-name {
  font-size: 24px;
  font-weight: bold;
  color: #3863ff;
}

/* 셀렉트 박스 스타일 */
.space-select {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-bottom: 16px;
}

.custom-select {
  width: 150px;
  height: 30px;
  padding: 8px 12px;
  font-size: 14px;
  color: #333;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.custom-select:hover {
  border-color: #4285f4;
  box-shadow: 0 0 5px rgba(66, 133, 244, 0.5);
}

.custom-select:focus {
  border-color: #4285f4;
  outline: none;
  box-shadow: 0 0 5px rgba(66, 133, 244, 0.5);
}

.transaction-info {
  margin-bottom: 16px;
  padding: 16px;
  /* background-color: #f9f9f9; */
  border: 1px solid #4788ff;
  /* border-radius: 8px; */
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  font-size: 16px;
  border-bottom: 1px solid #eee;
}

.info-row:last-child {
  border-bottom: none; /* 마지막 행은 보더 제거 */
}

.info-row .highlight {
  font-size: 17px;
  font-weight: bold;
}

.info-row .sub-info {
  font-size: 14px;
  color: #888;
  margin-left: 8px;
}

.info-row .highlight.red {
  color: #d93025;
  font-size: 18px;
}

.info-row .highlight.blue {
  color: #1a73e8;
  font-size: 18px;
}

.info-content {
  display: flex;
  flex-direction: column; /* 세로 정렬 */
  align-items: flex-end; /* 오른쪽 정렬 */
  text-align: right; /* 텍스트 오른쪽 정렬 */
}

.span-recent {
  margin-left: 5px;
  font-size: 12px;
  color: gray;
}

.chart-container {
  margin-top: 70px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.deal-history {
  margin-top: 100px;
  max-height: 500px;
  overflow: auto;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding: 10px;
  border-radius: 4px;
  color: #0d47a1; /* 텍스트 색상 */
}

.history-header h3 {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}

.history-header span {
  font-size: 14px;
  color: #888;
}

.history-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 14px;
  background-color: #fff; /* 흰색 배경 */
  border: 1px solid #ddd; /* 테두리 */
}

.history-table thead th {
  background-color: #e3f2fd; /* 하늘색 배경 */
  color: #0d47a1; /* 텍스트 색상 */
  font-weight: bold;
  padding: 10px;
  border-bottom: 2px solid #42a5f5; /* 하늘색 하단 테두리 */
}

.history-table tbody tr:nth-child(even) {
  background-color: #f9f9f9; /* 연한 회색 */
}

.history-table tbody tr:nth-child(odd) {
  background-color: #fff; /* 흰색 */
}

.history-table tbody td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.history-table tbody tr:last-child td {
  border-bottom: none; /* 마지막 행은 하단 테두리 제거 */
}

.history-table tbody td:last-child {
  text-align: right;
  font-weight: bold;
  color: #0d47a1; /* 하늘색 텍스트 */
}

.history-header h3 {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}

.history-header span {
  font-size: 14px;
  color: #0d47a1;
}

.kakao-map-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.kakao-map-button {
  background-color: transparent; /* 버튼 배경 투명 */
  border: 2px solid #ffe28b; /* 주황색 테두리 */
  margin: 10px 0px 40px 0px;
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s, box-shadow 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px; /* 글씨 간격 */
}

.kakao-map-button span.kakao {
  color: #ffcc00; /* KaKao 글씨 노란색 */
}

.kakao-map-button span.path {
  color: #000; /* 길찾기 글씨 검정색 */
}

.kakao-map-button:hover {
  background-color: #fff099; /* 호버 시 주황색 배경 */
  color: #fff; /* 호버 시 글씨 흰색 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.kakao-map-button:hover span.kakao {
  color: #fff; /* 호버 시 노란색 글씨도 흰색으로 변경 */
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 1200px;
  height: 800px;
  /* max-width: 1800px; */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  position: relative;
}

.modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #f44336;
  color: #fff;
  border: none;
  padding: 5px 10px;
  font-size: 14px;
  cursor: pointer;
  border-radius: 4px;
}

.modal-close:hover {
  background-color: #d32f2f;
}
</style>
