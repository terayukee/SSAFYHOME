<script setup>
import { defineProps } from "vue";

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
});

function getImagePath(avgDealAmount, dealSpace, dealCategory) {
  let category = "";

  // 평당 가격
  let amountPerSpace = avgDealAmount / dealSpace;

  if (dealCategory == "월세") {
    amountPerSpace = amountPerSpace * 10;
    console.log("amountPerSpace : ", amountPerSpace);
  }

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

console.log("카드 houses : ", props.houses);
</script>

<template>
  <div class="vertical-nav-content">
    <div class="house-card" v-for="house in houses" :key="house.aptSeq">
      <div class="house-image">
        <img
          :src="
            getImagePath(
              house.avgDealAmount,
              house.dealSpace,
              house.dealCategory
            )
          "
          alt="House"
          class="image"
        />
      </div>
      <div class="house-info">
        <h3>{{ house.aptNm }}</h3>
        <p class="deal-space">{{ house.dealSpace }}평</p>
        <p class="avg-deal-amount">
          <span v-if="house.dealCategory == '월세'">
            보증금 {{ house.avgDealAmount || "N/A" }} / 월세
            {{ house.avgFeeAmount || "N/A" }}만
          </span>
          <span v-else>
            {{ house.dealCategory }}
            {{ (house.avgDealAmount * 0.0001).toFixed(2) }}억
          </span>
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

.deal-space {
  margin-right: 5px;
}
</style>
