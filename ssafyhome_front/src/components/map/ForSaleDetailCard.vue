<script setup>
import { defineProps } from "vue";

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

import House from "@/assets/icons/realestate/house.png";
import Ruler from "@/assets/icons/realestate/3d-printer.png";
import Elevator from "@/assets/icons/realestate/elevator.png";
import Money from "@/assets/icons/realestate/money-bag.png";
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
        <thead>
          <tr>
            <th>계약 날짜</th>
            <th>거래 금액</th>
            <th>월세</th>
            <th>층수</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              {{ selectedCard.registerYear }}-{{
                selectedCard.registerMonth
              }}-{{ selectedCard.registerDay }}
            </td>
            <td>{{ selectedCard.dealAmount || "N/A" }}원</td>
            <td>{{ selectedCard.feeAmount || "N/A" }}원</td>
            <td>{{ selectedCard.thisFloor || "N/A" }}층</td>
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
}

.details-table {
  width: 100%;
  border-collapse: collapse;
}

.details-table th,
.details-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

.details-table th {
  background-color: #f9f9f9;
  font-weight: bold;
}

.details-table tbody tr:nth-child(odd) {
  background-color: #f9f9f9;
}
</style>
