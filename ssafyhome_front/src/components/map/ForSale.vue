<script setup>
import { ref, watch } from "vue";
import { fetchRealEstateInfos } from "@/api/realestate.js";

import ForSaleCardList from "./ForSaleCardList.vue";

// Props
const props = defineProps({
  selectedHouse: {
    type: Object,
    required: true,
  },
  spaceOptions: {
    type: Array,
    required: true,
  },
});

// State
const selectedSpace = ref(props.spaceOptions[0]?.value || null); // 초기 선택된 공간
const selectedCategory = ref("매매"); // 초기 선택된 거래 유형
const houses = ref([]); // 조회된 매물 리스트

// Fetch real estate infos
const fetchHouses = () => {
  if (props.selectedHouse.aptSeq) {
    fetchRealEstateInfos(
      {
        aptSeq: props.selectedHouse.aptSeq,
        space: selectedSpace.value,
        dealCategory: selectedCategory.value,
      },
      (response) => {
        houses.value = response.data;
      },
      (error) => {
        console.error("Failed to fetch real estate infos:", error);
      }
    );
  }
};

// Watchers for fetch triggers
watch([selectedSpace, selectedCategory], fetchHouses, { immediate: true });
</script>

<template>
  <div class="forsale-container">
    <!-- Top 영역 -->
    <div class="top-section">
      <div class="filters">
        <!-- SpaceOptions SelectBox -->
        <select v-model="selectedSpace" class="custom-select">
          <option
            v-for="option in spaceOptions"
            :key="option.value"
            :value="option.value"
          >
            {{ option.text }}
          </option>
        </select>

        <!-- DealCategory SelectBox -->
        <select v-model="selectedCategory" class="custom-select">
          <option value="매매">매매</option>
          <option value="전세">전세</option>
          <option value="월세">월세</option>
        </select>
      </div>
    </div>

    <!-- Content 영역 -->
    <div class="content-section">
      <nav class="vertical-nav">
        <!-- ForSaleList 컴포넌트 -->
        <ForSaleCardList :houses="houses" :houseType="selectedCategory" />
      </nav>
    </div>
  </div>
</template>

<style scoped>
.forsale-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* Top Section */
.top-section {
  background-color: #f9f9f9;
  padding: 16px;
  border-bottom: 1px solid #ddd;
  display: flex;
  gap: 16px;
  justify-content: space-between;
  align-items: center;
}

/* Filters */
.filters {
  display: flex;
  gap: 16px;
}

.custom-select {
  width: 150px;
  height: 30px;
  padding: 5px 10px;
  font-size: 14px;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.custom-select:hover {
  border-color: #4285f4;
}

/* Content Section */
.content-section {
  flex: 1;
  overflow-y: auto;
}

/* Vertical Nav */
.vertical-nav {
  width: 100%;
  background-color: #ffffff;
  border-right: 1px solid #e0e0e0;
  padding: 16px 0;
  overflow-y: scroll; /* 세로 스크롤 활성화 */
  max-height: 100%; /* 부모 요소 높이에 맞춤 */
}
</style>
