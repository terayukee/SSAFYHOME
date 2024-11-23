<script setup>
import { defineProps, ref, onMounted } from "vue";
import { getBySeq } from "@/api/house.js";

// Props
const props = defineProps({
  selectedCard: {
    type: Object,
    required: true,
  },
  realestates: {
    type: Array,
    required: true,
  },
  houseType: {
    type: String,
    required: true,
  },
  randomIndex: {
    type: Number,
    required: true,
  },
});

// HouseInfo 데이터 상태
const houseInfo = ref(null); // houseInfo를 저장할 ref

// aptSeq로 houseInfo 조회
const fetchHouseInfo = () => {
  if (props.selectedCard && props.selectedCard.aptSeq) {
    getBySeq(
      { aptSeq: props.selectedCard.aptSeq },
      (response) => {
        houseInfo.value = response.data; // 성공 시 houseInfo 업데이트
        console.log("HouseInfo fetched successfully:", houseInfo.value);
      },
      (error) => {
        console.error("Failed to fetch houseInfo:", error);
      }
    );
  }
};

import House from "@/assets/icons/realestate/house.png";
import Ruler from "@/assets/icons/realestate/3d-printer.png";
import Elevator from "@/assets/icons/realestate/elevator.png";
import Money from "@/assets/icons/realestate/money-bag.png";

// 컴포넌트 마운트 시 houseInfo 가져오기
onMounted(() => {
  fetchHouseInfo();
});
</script>

<template>
  <div class="forsale-detail-card">
    <!-- 사진 영역 -->
    <div class="photo-section">
      <div class="main-photo">
        <img
          :src="`/assets/interior/livingroom/${randomIndex}.jpg`"
          alt="Main"
        />
      </div>
      <div class="side-photos">
        <div class="side-photo1">
          <img :src="`/assets/interior/room/${randomIndex}.jpg`" alt="Main" />
        </div>
        <div class="side-photo2">
          <img
            :src="`/assets/interior/bathroom/${randomIndex}.jpg`"
            alt="Main"
          />
        </div>
      </div>
    </div>

    <!-- 정보 영역 -->
    <div class="info-section">
      <div class="info-row">
        <div class="info-item">
          <img :src="House" alt="" />
          <span class="info-value">{{ houseType }}</span>
        </div>
        <div class="info-item">
          <img :src="Ruler" alt="" />
          <span class="info-value">{{ selectedCard.excluUseAr }}㎡</span>
        </div>
      </div>
      <div class="info-row">
        <div class="info-item">
          <img :src="Elevator" alt="" />
          <span class="info-value"
            >{{ selectedCard.thisFloor }}층 /
            {{ selectedCard.maxFloor }}층</span
          >
        </div>
        <div class="info-item">
          <img :src="Money" alt="" />
          <span class="info-value">{{ selectedCard.maintenanceCost }}만원</span>
        </div>
      </div>
    </div>

    <!-- 매물 정보 테이블 -->
    <div class="table-section">
      <h2>매물 정보</h2>

      <table class="details-table">
        <tbody>
          <tr>
            <th>건축 연도</th>
            <td>{{ houseInfo?.buildYear || "정보 없음" }}</td>
          </tr>
          <tr>
            <th>계약 날짜</th>
            <td>
              {{ selectedCard.registerYear }}-{{
                selectedCard.registerMonth
              }}-{{ selectedCard.registerDay }}
            </td>
          </tr>
          <tr>
            <th>거래 금액</th>
            <td>
              <template v-if="selectedCard.dealCategory === '월세'">
                {{ selectedCard.dealAmount || "N/A" }}만원
              </template>
              <template v-else>
                {{
                  selectedCard.dealAmount
                    ? (
                        parseFloat(selectedCard.dealAmount.replace(/,/g, "")) *
                        0.0001
                      ).toFixed(2) + "억"
                    : "N/A"
                }}
              </template>
            </td>
          </tr>
          <!-- 월세일 경우에만 월세 정보 추가 -->
          <tr v-if="selectedCard.dealCategory === '월세'">
            <th>월세</th>
            <td>{{ selectedCard.feeAmount || "N/A" }}만원</td>
          </tr>
          <tr>
            <th>층수</th>
            <td>
              {{ selectedCard.thisFloor || "N/A" }}층 /
              {{ selectedCard.maxFloor || "N/A" }}층
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.forsale-detail-card {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.photo-section {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.main-photo img {
  width: 400px;
  height: 415px;
  border-radius: 8px;
  object-fit: cover;
}

.side-photos {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.side-photos img {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
}

.info-section {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  justify-content: left;
  width: 500px;
  margin-bottom: 10px;
}

.info-item {
  width: 200px;
}

.info-item img {
  width: 30px;
  height: 30px;
  margin-right: 10px;
}

.table-section {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-section h2 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #2c3e50; /* 어두운 파란색 */
}

.details-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 16px;
  color: #34495e; /* 어두운 회색 */
}

.details-table th {
  text-align: left;
  padding: 15px;
  width: 150px;
  background-color: #f4f6f9; /* 연한 회색 배경 */
  border-bottom: 2px solid #ecf0f1;
  font-weight: bold;
  color: #2c3e50;
}

.details-table td {
  padding: 15px;
  border-bottom: 1px solid #ecf0f1;
  color: #7f8c8d; /* 회색 */
}

.details-table tr:hover td {
  background-color: #f9f9f9; /* 테이블 행 호버 효과 */
}

.details-table td:first-child {
  font-weight: bold;
  color: #34495e;
}

.details-table tr:last-child td {
  border-bottom: none; /* 마지막 행의 테두리 제거 */
}
</style>
