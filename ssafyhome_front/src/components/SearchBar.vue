<script setup>
import { ref } from "vue";
import { searchAll } from "@/api/search";

const keyword = ref(""); // 검색어
const searchResults = ref({
  regions: [],
  houses: [],
}); // 검색 결과
const hoverImage = ref(""); // 이미지 표시 영역에 표시될 이미지

// 랜덤 이미지 경로 생성
const getRandomImage = (houseType) => {
  // 주택 타입
  let housePath = "";
  if (houseType == "아파트") {
    housePath = "apart";
  } else if (houseType == "빌라") {
    housePath = "villa";
  } else if (houseType == "오피스텔") {
    housePath = "officetel";
  }

  const basePath = `/assets/${housePath}/`;
  const categories = ["high", "mid", "low"];

  const randomCategory =
    categories[Math.floor(Math.random() * categories.length)];
  const randomIndex = Math.floor(Math.random() * 20) + 1; // 1~20 중 랜덤
  return `${basePath}${randomCategory}${randomIndex}.jpg`;
};

const onSearch = () => {
  if (!keyword.value.trim()) return;
  searchAll(
    keyword.value,
    (response) => {
      searchResults.value = response.data;
    },
    (error) => {
      console.error("Search failed:", error);
    }
  );
};

// 주택 검색결과 항목에 마우스 오버 시 이미지 변경
const handleMouseOver = (houseType) => {
  hoverImage.value = getRandomImage(houseType);
};
</script>

<template>
  <div class="search-container">
    <!-- 검색 입력 및 버튼 -->
    <div class="search-bar-container">
      <input
        type="text"
        v-model="keyword"
        placeholder="지역, 지하철, 대학, 단지명 또는 매물번호를 입력해주세요."
        class="search-bar"
        @keyup.enter="onSearch"
      />
      <button class="search-button" @click="onSearch">검색</button>
    </div>

    <!-- 검색 결과 영역 -->
    <div
      class="search-results-container"
      :class="{
        active: searchResults.regions.length || searchResults.houses.length,
      }"
    >
      <!-- 지역 검색결과 -->
      <div class="search-column">
        <h3>지역 검색 결과</h3>
        <ul>
          <li
            v-for="(region, index) in searchResults.regions"
            :key="`region-${index}`"
          >
            {{ region.sidoName }} {{ region.gugunName }} {{ region.dongName }}
          </li>
        </ul>
      </div>

      <!-- 주택 검색결과 -->
      <div class="search-column">
        <h3>주택 검색 결과</h3>
        <ul>
          <li
            v-for="(house, index) in searchResults.houses"
            :key="`house-${index}`"
            @mouseover="handleMouseOver(house.houseType)"
          >
            {{ house.houseType }} {{ house.aptNm }}
            <br />
            {{ house.sidoName }} {{ house.gugunName }} {{ house.dongName }}
          </li>
        </ul>
      </div>

      <!-- 이미지 표시 영역 -->
      <div class="search-column image-display">
        <h3>이미지 표시</h3>
        <div class="image-container">
          <img v-if="hoverImage" :src="hoverImage" alt="House Preview" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 전체 컨테이너 */
.search-container {
  width: 80%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}

/* 검색 바 영역 */
.search-bar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 0; /* 아래 여백 제거 */
}

.search-bar {
  width: 60%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-button {
  padding: 10px 16px;
  margin-left: 8px;
  font-size: 14px;
  background-color: #4285f4;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 검색 결과 컨테이너 */
.search-results-container {
  display: flex;
  visibility: hidden; /* 처음에는 숨김 */
  opacity: 0; /* 보이지 않게 */
  transform: translateY(-10px); /* 살짝 위로 이동 */
  transition: all 0.3s ease; /* 애니메이션 효과 */
}

/* 검색 결과가 있으면 표시 */
.search-results-container.active {
  visibility: visible; /* 표시 */
  opacity: 1; /* 보이게 */
  transform: translateY(0); /* 원래 위치로 */
  margin-top: 0; /* 검색 바와 붙게 */
}

/* 개별 검색 결과 컬럼 */
.search-column {
  flex: 1;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow-y: auto; /* 스크롤 활성화 */
  max-height: 400px;
  margin: 0; /* 좌우 마진 제거 */
}

.search-column h3 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.search-column ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.search-column li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.search-column li:last-child {
  border-bottom: none;
}

.search-column li:hover {
  background-color: #f9f9f9;
}

/* 이미지 표시 영역 */
.image-display {
  text-align: center;
}

.image-container {
  margin-top: 20px;
  width: 100%;
  height: 250px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.image-container img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
</style>
