<script setup>
import { defineProps, defineEmits } from "vue";

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
  houseType: {
    type: String,
  },
});

// 부모로 이벤트를 전달하기 위한 emit 정의
const emit = defineEmits(["cardClick"]);

// 카드 클릭 핸들러
const handleCardClick = (house) => {
  emit("cardClick", house); // 선택된 house 정보를 부모로 emit
};

// 이미지 경로 생성 함수
function getImagePath(avgDealAmount, dealSpace, dealCategory) {
  let category = "";

  // 주택 타입
  let houseType = "";
  if (props.houseType === "아파트") {
    houseType = "apart";
  } else if (props.houseType === "빌라") {
    houseType = "villa";
  } else if (props.houseType === "오피스텔") {
    houseType = "officetel";
  }

  // 평당 가격 계산
  let amountPerSpace = avgDealAmount / dealSpace;

  if (dealCategory === "월세") {
    amountPerSpace *= 10;
  }

  // 가격 기준으로 카테고리 설정
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
              house.avgDealAmount || 0,
              house.dealSpace || 1,
              house.dealCategory
            )
          "
          alt="House"
          class="image"
        />
      </div>
      <div class="house-info">
        <h3>{{ house.aptNm }}</h3>
        <p class="deal-space">{{ house.dealSpace }}㎡</p>
        <p class="avg-deal-amount">
          <span v-if="house.dealCategory === '월세'">
            보증금 {{ house.avgDealAmount || "N/A" }}만 / 월세
            {{ house.avgFeeAmount || "N/A" }}만
          </span>
          <span v-else>
            {{ house.dealCategory }}
            {{ (house.avgDealAmount * 0.0001).toFixed(2) || "N/A" }}억
          </span>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.vertical-nav-content {
  display: flex;
  flex-direction: column;
  padding: 16px;
  background-color: #f9f9f9;
  overflow-y: scroll;
}

.house-card {
  display: flex;
  margin-bottom: 16px;
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.house-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
}

.house-image {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.house-info {
  flex: 2;
  padding-left: 16px;
}

.house-info h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50; /* 어두운 파란색 */
  margin-bottom: 8px;
}

.house-info p {
  font-size: 14px;
  color: #7f8c8d; /* 회색 톤 */
  margin: 4px 0;
}

.deal-space {
  font-size: 14px;
  font-weight: bold;
  color: #3498db; /* 밝은 파란색 */
}

.avg-deal-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c; /* 붉은색 */
}
</style>
