<script setup>
import { ref, watch } from "vue";
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
    selectedSpace.value = newOptions[0].value; // 첫 번째 옵션 자동 선택
  }
});

// 최고/최저 거래가를 저장할 상태
const threeYearHighLow = ref({ maxDealAmount: "N/A", minDealAmount: "N/A" });

// 최고/최저 거래가 계산 함수
const calculateHighLow = () => {
  if (dealList.value.length === 0) {
    threeYearHighLow.value = { maxDealAmount: "N/A", minDealAmount: "N/A" };
    return;
  }

  const amounts = dealList.value.map((deal) =>
    parseInt(deal.dealAmount.replace(/,/g, ""), 10)
  );

  threeYearHighLow.value = {
    maxDealAmount: Math.max(...amounts),
    minDealAmount: Math.min(...amounts),
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
watch([spaceOptions, () => props.selectedHouse], ([newOptions, newHouse]) => {
  if (newHouse && newOptions.length > 0) {
    selectedSpace.value = newOptions[0].value; // 첫 번째 옵션 자동 선택
  }
});

// 차트 데이터
const chartData = ref({
  labels: [], // X축 레이블 (거래 연월)
  datasets: [
    {
      label: "거래 금액 (만원)",
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
  const data = dealList.value.map((deal) =>
    parseInt(deal.dealAmount.replace(/,/g, ""), 10)
  );

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
          <span class="highlight"
            >{{ (selectedHouse.avgDealAmount * 0.0001).toFixed(2) }}억</span
          >
          <span class="sub-info">
            평당가
            {{
              calculatePerPyeong(
                selectedHouse.avgDealAmount,
                selectedHouse.dealSpace
              )
            }}만원
          </span>
        </div>
      </div>

      <!-- 거래가 정보 -->
      <div class="info-row">
        <div class="info-title">
          <span>거래가</span>
          <span>최근 3년</span>
        </div>
        <div class="info-content">
          <div class="info-text">
            <span class="highlight red">최고 </span>
            <span class="amount">
              {{
                threeYearHighLow.maxDealAmount !== "N/A"
                  ? (threeYearHighLow.maxDealAmount * 0.0001).toFixed(2)
                  : "N/A"
              }}억
            </span>
          </div>
          <div class="info-text">
            <span class="highlight blue">최저 </span>
            <span class="amount">
              {{
                threeYearHighLow.minDealAmount !== "N/A"
                  ? (threeYearHighLow.minDealAmount * 0.0001).toFixed(2)
                  : "N/A"
              }}억
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Chart Section -->
    <div class="chart-container">
      <div class="chart-placeholder">
        <LineChart :chart-data="chartData" :chart-options="chartOptions" />
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

/* 셀렉트 박스 스타일 */
.space-select {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-bottom: 16px;
}

.custom-select {
  width: 150px;
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
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
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

.info-row span {
  font-size: 16px;
  color: #333;
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

.chart-container {
  margin-top: 50px;
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
</style>
