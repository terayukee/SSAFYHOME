<template>
  <div class="category-container">
    <!-- 첫 번째 줄 -->
    <div class="category-row large">
      <div
        v-for="(category, index) in categories.slice(0, 2)"
        :key="index"
        class="category-item large-item"
        @click="navigateToCategory(category.type)"
      >
        <p class="category-title">{{ category.name }}</p>
        <p class="category-description">{{ category.description }}</p>
      </div>
    </div>

    <!-- 두 번째 줄 -->
    <div class="category-row small">
      <div
        v-for="(category, index) in categories.slice(2)"
        :key="index"
        class="category-item small-item"
        @click="navigateToCategory(category.type)"
      >
        <p class="category-title">{{ category.name }}</p>
        <p class="category-description">{{ category.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const categories = [
  {
    name: "원/투룸",
    description: "주택/빌라, 오피스텔까지 모~든 전월세 매물을 한 번에!",
    type: "one-room",
  },
  {
    name: "아파트",
    description: "가장 빠른 실거래가 알림! 풍부한 단지정보 및 실시간 랭킹까지!",
    type: "apartment",
  },
  {
    name: "주택/빌라",
    description: "전월세부터 매매까지 모~든 매물!",
    type: "villa",
  },
  {
    name: "오피스텔",
    description: "다양한 정보와 다양한 매물!",
    type: "officetel",
  },
  {
    name: "분양",
    description: "전국의 모든 분양/입주 정보 확인 가능!",
    type: "pre-sale",
  },
];

const router = useRouter();

const navigateToCategory = (type) => {
  router.push(`/category/${type}`);
};
</script>

<style scoped>
.category-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.category-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.category-item {
  cursor: pointer;
  text-align: left;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s;
}

.category-item:hover {
  background-color: #f9f9f9;
}

/* 첫 번째 줄 (큰 아이템) */
.large .large-item {
  flex: 1;
  height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.large-item:not(:last-child) {
  margin-right: 16px;
}

/* 두 번째 줄 (작은 아이템) */
.small {
  margin-top: 60px;
}

.small .small-item {
  flex: 1;
  height: 100px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.small-item:not(:last-child) {
  margin-right: 16px;
}

.category-title {
  font-weight: bold;
  margin-bottom: 8px;
  font-size: 16px;
}

.category-description {
  font-size: 14px;
  color: #666;
}
</style>
