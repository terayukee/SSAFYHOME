<script setup>
import { defineProps } from "vue";

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
});

function getImagePath(avgDealAmount, dealType) {
  let category = "";

  // 평당 가격
  const amountPerSpace = avgDealAmount / dealType;

  // 아파트 가격 기준으로 카테고리 설정
  if (amountPerSpace > 3000) {
    category = "high";
  } else if (amountPerSpace > 1000) {
    category = "mid";
  } else {
    category = "low";
  }

  const randomIndex = Math.floor(Math.random() * 10) + 1; // 1~10 범위의 숫자
  return `/assets/apart/${category}${randomIndex}.jpg`;
}
</script>

<template>
  <div class="vertical-nav-content">
    <div class="house-card" v-for="house in houses" :key="house.aptSeq">
      <div class="house-image">
        <img
          :src="getImagePath(house.avgDealAmount, house.dealType)"
          alt="House"
          class="image"
        />
      </div>
      <div class="house-info">
        <h3>{{ house.aptNm }}</h3>
        <p class="deal-type">{{ house.dealType }}</p>
        <p class="avg-deal-amount">
          {{ (house.avgDealAmount * 0.0001).toFixed(2) }}억
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.house-card {
  display: flex;
  margin-bottom: 16px;
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.house-image {
  flex: 1;
}

.image {
  max-width: 100%;
  border-radius: 8px;
}

.house-info {
  flex: 2;
  padding-left: 10px;
}

.house-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #333;
}

.house-info p {
  margin: 4px 0;
  font-size: 14px;
  color: #666;
}

.deal-type {
  margin-right: 5px;
}
</style>
