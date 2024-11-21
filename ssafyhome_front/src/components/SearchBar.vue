<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { searchAll } from "@/api/search";
import { useRouter } from "vue-router";

const keyword = ref(""); // 검색어
const searchResults = ref({
  regions: [],
  houses: [],
}); // 검색 결과
const hoverImage = ref(""); // 이미지 표시 영역에 표시될 이미지
const isResultsVisible = ref(false); // 검색 결과 표시 여부
const router = useRouter();

// 랜덤 이미지 경로 생성
const getRandomImage = (houseType) => {
  let housePath = "";
  if (houseType === "아파트") {
    housePath = "apart";
  } else if (houseType === "빌라") {
    housePath = "villa";
  } else if (houseType === "오피스텔") {
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
  if (!keyword.value.trim()) {
    isResultsVisible.value = false; // 검색어가 없으면 결과 숨김
    return;
  }
  searchAll(
    keyword.value,
    (response) => {
      searchResults.value = response.data;
      isResultsVisible.value = true;
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

// 주택 클릭 시 지도 페이지로 이동
const handleHouseClick = (latitude, longitude) => {
  console.log("주택 클릭, 위도 경도 ", latitude, longitude);
  router.push({
    name: "map",
    query: {
      latitude: latitude,
      longitude: longitude,
      maplevel: 3,
    },
  });
};

// 바깥 클릭 이벤트 처리
const handleClickOutside = (event) => {
  const container = document.querySelector(".search-container");
  if (container && !container.contains(event.target)) {
    isResultsVisible.value = false; // 검색 결과 숨김
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
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
        active:
          isResultsVisible &&
          (searchResults.regions.length || searchResults.houses.length),
      }"
    >
      <!-- 지역 검색결과 -->
      <div class="search-column">
        <h3 class="search-title">지역 검색 결과</h3>
        <ul>
          <li
            v-for="(region, index) in searchResults.regions"
            :key="`region-${index}`"
          >
            <span class="region-info"
              >{{ region.sidoName }} {{ region.gugunName }}
              {{ region.dongName }}</span
            >
          </li>
        </ul>
      </div>

      <!-- 주택 검색결과 -->
      <div class="search-column">
        <h3 class="search-title">주택 검색 결과</h3>
        <ul>
          <li
            v-for="(house, index) in searchResults.houses"
            :key="`house-${index}`"
            @mouseover="handleMouseOver(house.houseType)"
            @click="handleHouseClick(house.latitude, house.longitude)"
          >
            <div class="house-info-div">
              <span class="house-type">{{ house.houseType }}</span>
              <span class="house-name">{{ house.aptNm }}</span>
            </div>

            <div class="house-location-div">
              <span class="house-location"
                >{{ house.sidoName }} {{ house.gugunName }}
                {{ house.dongName }}</span
              >{{ house.lo }}
            </div>
          </li>
        </ul>
      </div>

      <!-- 이미지 표시 영역 -->
      <div class="search-column image-display">
        <h3 class="search-title">이미지 미리보기</h3>
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
  position: relative; /* 검색 결과 컨테이너 위치 고정을 위한 부모 설정 */
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

/* 검색 결과 타이틀 */
.search-title {
  color: #74acff;
}

/* 검색 결과 컨테이너 */
.search-results-container {
  position: absolute; /* 절대 위치 지정 */
  top: 100%; /* 부모 요소의 아래로 배치 */
  left: 0;
  width: 100%; /* 부모의 너비에 맞춤 */
  min-width: 800px;
  display: flex;
  visibility: hidden; /* 처음에는 숨김 */
  opacity: 0; /* 보이지 않게 */
  transform: translateY(-10px); /* 살짝 위로 이동 */
  transition: all 0.3s ease; /* 애니메이션 효과 */
  z-index: 10; /* 검색 결과가 다른 요소 위로 표시되도록 설정 */
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
  text-align: center;
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

/* 지역 정보 표시 영역 */
.region-info {
  color: #5c689a;
}

/* 주택 정보 표시 영역 */
.house-info-div {
}

.house-type {
  color: gray;
  font-size: 15px;
}

.house-name {
  margin-left: 10px;
  color: #74acff;
  font-size: 17px;
}

.house-location-div {
  margin-top: 10px;
}

.house-location {
  color: rgb(154, 165, 223);
  font-size: 15px;
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
