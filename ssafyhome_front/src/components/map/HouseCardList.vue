<script setup>
import { defineProps, defineEmits } from "vue";
import "@/assets/main.css";

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
  selectedNav: {
    type: String,
  },
});

function getImagePath(avgDealAmount, dealSpace, dealCategory) {
  let category = "";

  // 주택 타입
  let houseType = "";
  if (props.selectedNav == "apartment") {
    houseType = "apart";
  } else if (props.selectedNav == "villa") {
    houseType = "villa";
  } else if (props.selectedNav == "officetel") {
    houseType = "officetel";
  }

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

  const randomIndex = Math.floor(Math.random() * 20) + 1; // 1~20 범위의 숫자
  return `/assets/${houseType}/${category}${randomIndex}.jpg`;
}

// 부모로 이벤트를 전달하기 위한 emit 정의
const emit = defineEmits(["cardClick"]);

// 카드 클릭 핸들러
const handleCardClick = (house) => {
  emit("cardClick", house); // 선택된 house 정보를 부모로 emit
};
</script>

<template>
  <div class="vertical-nav-content">
    <div
      class="house-card"
      v-for="house in houses"
      :key="house.aptSeq"
      @click="handleCardClick(house)"
    >
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
  width: 160px;
  height: 160px;
}

.house-info {
  flex: 2;
  padding-left: 20px;
}

.house-info h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50; /* 어두운 파란색 톤 */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.house-info p {
  font-size: 14px;
  color: #7f8c8d; /* 회색 톤 */
  line-height: 1.5;
}

/* 거래 유형과 금액 강조 */
.avg-deal-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c; /* 붉은색 톤 */
}

.deal-space {
  font-size: 14px;
  font-weight: bold;
  color: #3498db; /* 밝은 파란색 */
}
</style>
