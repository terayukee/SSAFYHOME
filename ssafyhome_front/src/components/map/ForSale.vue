<script setup>
import { ref, watch } from "vue";
import { fetchRealEstateInfos } from "@/api/realestate.js";

import ForSaleCardList from "./ForSaleCardList.vue";
import ForSaleDetailCard from "./ForSaleDetailCard.vue";

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
  houseType: {
    type: String,
    required: true,
  },
});

// State
const selectedSpace = ref(props.spaceOptions[0]?.value || null); // 초기 선택된 공간
const selectedCategory = ref("매매"); // 초기 선택된 거래 유형
const realestates = ref([]); // 조회된 매물 리스트
const selectedCard = ref(null); // 선택된 부동산 카드의 세부 정보

const selectedRandomIndex = ref(); // 선택한 카드의 랜덤 인덱스

// Fetch real estate infos
const fetchRealestates = () => {
  console.log("fetchRealestates 실행", props.houseType);
  if (props.selectedHouse.aptSeq) {
    fetchRealEstateInfos(
      {
        aptSeq: props.selectedHouse.aptSeq,
        space: selectedSpace.value,
        dealCategory: selectedCategory.value,
        houseType: props.houseType,
      },
      (response) => {
        realestates.value = response.data;
        console.log("가져온 부동산 매물 정보 : ", realestates);
      },
      (error) => {
        console.error("Failed to fetch real estate infos:", error);
      }
    );
  }
};

// Watchers for fetch triggers
watch([selectedSpace, selectedCategory], fetchRealestates, { immediate: true });

// 카드 클릭 핸들러
const handleCardClick = ({ realestate, randomIndex }) => {
  console.log("부동산 선택 매물 realestate : ", realestate);
  selectedCard.value = realestate; // 클릭한 카드의 정보를 저장
  selectedRandomIndex.value = randomIndex;
};
</script>

<template>
  <div class="forsale-container">
    <!-- Top 영역 -->
    <div class="top-section">
      <!-- 타이틀 -->
      <div class="forsale-title">
        <span class="house-aptNm">{{ selectedHouse.aptNm }}</span>
      </div>
      <div class="select-options">
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
    </div>

    <!-- Content 영역 -->
    <div class="content-section">
      <!-- 좌측: 매물 리스트 -->
      <div class="vertical-nav">
        <ForSaleCardList
          :realestates="realestates"
          :houseType="selectedCategory"
          :randomIndex="randomIndex"
          @cardClick="handleCardClick"
        />
      </div>

      <!-- 우측: 세부 정보 -->
      <div class="vertical-nav-detail">
        <template v-if="selectedCard">
          <ForSaleDetailCard
            :realestates="realestates"
            :houseType="selectedCategory"
            :selectedCard="selectedCard"
            :randomIndex="selectedRandomIndex"
          />
        </template>
        <template v-else>
          <div class="no-selection">
            <p>매물을 선택하면 세부 정보가 여기에 표시됩니다.</p>
          </div>
        </template>
      </div>
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
}

.forsale-title {
  margin-bottom: 30px;
  text-align: center;
}

.forsale-title .house-aptNm {
  color: #4285f4;
  font-size: 23px;
  font-weight: bold;
  margin-left: 20px;
}

.select-options {
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
  display: flex;
  flex: 1;
  overflow: hidden; /* 불필요한 스크롤 숨김 */
}

/* Vertical Nav */
.vertical-nav {
  width: 350px; /* 좌측 영역 너비 */
  background-color: #ffffff;
  border-right: 1px solid #e0e0e0;
  padding: 16px 0;
  overflow-y: scroll; /* 세로 스크롤 활성화 */
}

/* Vertical Nav Detail */
.vertical-nav-detail {
  flex: 1; /* 우측 영역 남은 공간 채움 */
  padding: 16px;
  background-color: #f9f9f9;
  overflow-y: auto; /* 세로 스크롤 활성화 */
}

.vertical-nav-detail h2 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 16px;
}

.vertical-nav-detail p {
  font-size: 14px;
  margin-bottom: 8px;
}

.no-selection {
  text-align: center;
  margin-top: 100px;
}

.no-selection p {
  font-size: 25px;
  color: #0ea242;
}
</style>
