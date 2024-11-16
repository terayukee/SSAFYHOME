<template>
  <div class="category-grid">
    <div
      v-for="(category, index) in categories"
      :key="index"
      class="category-item"
      @click="navigateToCategory(category.type)"
    >
      <p>{{ category.name }}</p>
      <small>{{ category.description }}</small>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const categories = [
  {
    name: "원/투룸",
    description: "주택/빌라, 오피스텔까지!",
    type: "one-room",
  },
  {
    name: "아파트",
    description: "가장 빠른 실거래가 알림!",
    type: "apartment",
  },
  { name: "주택/빌라", description: "전/월세부터 매매까지!", type: "villa" },
  { name: "오피스텔", description: "다양한 정보와 매물!", type: "officetel" },
  { name: "분양", description: "전국 분양 정보 확인!", type: "pre-sale" },
];

const router = useRouter();

const navigateToCategory = (type) => {
  router.push(`/category/${type}`);
};
</script>

<style scoped>
.category-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.category-item {
  padding: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
}
.category-item:hover {
  background-color: #f0f0f0;
}
</style>
