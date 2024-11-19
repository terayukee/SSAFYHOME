<script setup>
import { ref, watch } from "vue";
import VKakaoMap from "@/components/common/VKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";
import { listHouses, listHousesInBounds } from "@/api/house.js";

import ApartmentIcon from "@/assets/icons/residential.png";
import VillaIcon from "@/assets/icons/villa.png";
import OfficetelIcon from "@/assets/icons/office.png";
import PreSaleIcon from "@/assets/icons/presale.png";

import HouseCardList from "@/components/map/HouseCardList.vue";

// 좌측 네비게이션 항목
const navItems = [
  { id: "apartment", label: "아파트", icon: ApartmentIcon },
  { id: "villa", label: "주택/빌라", icon: VillaIcon },
  { id: "officetel", label: "오피스텔", icon: OfficetelIcon },
  { id: "pre-sale", label: "분양", icon: PreSaleIcon },
];

const selectedNav = ref("apartment"); // 선택 상태
const selectedCategory = ref("매매"); // 현재 선택된 타입 (매매, 전세, 월세)

const houses = ref([]); // house 정보를 저장할 ref 변수
const bounds = ref(null); // 현재 지도 bounds

// 추가: 선택된 하우스 정보를 저장
const selectedHouse = ref(null);

const fetchHousesInBounds = (bounds) => {
  console.log("bounds:", bounds);
  listHousesInBounds(
    bounds.value,
    selectedCategory.value,
    (response) => {
      houses.value = response.data;
      // console.log("listHousesInBounds 성공, ", response.data);
    },
    (error) => {
      console.error("Failed to fetch houses:", error);
    }
  );
};

// 지도 영역 변경 이벤트 핸들러
const handleBoundsChange = (newBounds) => {
  bounds.value = newBounds;
  fetchHousesInBounds(bounds);
};

// 네비게이션 항목 선택 함수
const selectNav = (id) => {
  selectedNav.value = id;
};

// 필터 데이터 및 옵션
const filters = ref({
  dealCategory: "",
  roomSize: "",
  approvalDate: "",
  numHouseholds: "",
  parkingSpaces: "",
  numRooms: "",
  additionalFilters: "",
});

const filterOptions = {
  dealCategory: [
    { text: "월세", value: "월세" },
    { text: "전세", value: "전세" },
    { text: "매매", value: "매매" },
  ],
  roomSize: [
    { text: "10평 이하", value: "10평 이하" },
    { text: "10~20평", value: "10~20평" },
    { text: "20평 이상", value: "20평 이상" },
  ],
  approvalDate: [
    { text: "5년 이내", value: "5년 이내" },
    { text: "10년 이내", value: "10년 이내" },
    { text: "10년 이상", value: "10년 이상" },
  ],
  additionalFilters: [
    { text: "옵션 포함", value: "옵션 포함" },
    { text: "신축", value: "신축" },
    { text: "엘리베이터", value: "엘리베이터" },
  ],
};

const onFilterChange = (filterKey, value) => {
  filters.value[filterKey] = value;
  console.log("Filter Changed:", filters.value);
};

// 선택된 타입이 변경될 때 fetch 호출
watch(selectedCategory, (newCategory) => {
  console.log("Category changed to:", newCategory);
  if (bounds.value) {
    fetchHousesInBounds(bounds);
  } else {
    console.warn("Bounds are not defined yet.");
  }
});

// 카드 클릭 시 선택된 하우스 정보를 설정
const onCardClick = (house) => {
  selectedHouse.value = house; // 클릭한 하우스 정보 저장
};

// 지도 클릭 시 detail nav 접기
const handleMapClick = () => {
  selectedHouse.value = null;
};
</script>

<template>
  <div class="apt-map-view">
    <!-- 좌측 네비게이션 -->
    <aside class="left-nav">
      <ul>
        <li
          v-for="item in navItems"
          :key="item.id"
          :class="{ active: selectedNav === item.id }"
          @click="selectNav(item.id)"
        >
          <img :src="item.icon" alt="" class="nav-icon" />
          <span>{{ item.label }}</span>
        </li>
      </ul>
    </aside>

    <div class="main-content">
      <!-- 상단 네비게이션 -->
      <header class="top-nav">
        <VSelect
          :selectOption="filterOptions.dealCategory"
          v-model="selectedCategory"
          placeholder="매매"
        />
        <VSelect
          :selectOption="filterOptions.roomSize"
          placeholder="방크기"
          @onKeySelect="(val) => onFilterChange('roomSize', val)"
        />
        <VSelect
          :selectOption="filterOptions.approvalDate"
          placeholder="사용승인일"
          @onKeySelect="(val) => onFilterChange('approvalDate', val)"
        />
      </header>

      <div class="content-wrapper">
        <!-- 세로 네비게이션 -->
        <nav class="vertical-nav">
          <div class="vertical-nav-buttons"></div>
          <div class="vertical-nav-content">
            <!-- HouseCardList 컴포넌트로 데이터 전달 -->
            <HouseCardList :houses="houses" @cardClick="onCardClick" />
          </div>
        </nav>

        <!-- 두 번째 세로 네비게이션: 하우스 상세보기 -->
        <nav class="vertical-nav-detail" v-if="selectedHouse">
          <!-- X 버튼 -->
          <button class="close-button" @click="selectedHouse = null">X</button>

          <div class="detail-content">
            <h3>{{ selectedHouse.aptNm }}</h3>
            <p>거래 유형: {{ selectedHouse.dealCategory }}</p>
            <p>평수: {{ selectedHouse.dealSpace }}평</p>
            <p>거래 금액: {{ selectedHouse.avgDealAmount }}억</p>
          </div>
        </nav>

        <!-- 지도 및 결과 영역 -->
        <section class="map-section">
          <VKakaoMap
            :houses="houses"
            :selectedCategory="selectedCategory"
            @boundsChange="handleBoundsChange"
            @markerClick="onCardClick"
            @mapClick="handleMapClick"
          />
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.apt-map-view {
  display: flex;
  height: 100vh;
}

/* 좌측 네비게이션 스타일 */
.left-nav {
  width: 150px;
  background-color: #fff;
  border-right: 1px solid #e0e0e0;
  padding: 16px 0;
}

.left-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.left-nav li {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s, border-color 0.2s;
  border-radius: 8px;
}

.left-nav li:hover {
  background-color: #f5f5f5;
}

.left-nav li.active {
  background-color: #e8f0fe;
  border-left: 4px solid #4285f4;
  font-weight: bold;
  color: #4285f4;
}

/* main */
.main-content {
  width: 100%;
}

/* 세로 네비게이션 스타일 */
.vertical-nav {
  width: 350px;
  background-color: #ffffff;
  border-right: 1px solid #e0e0e0;
  padding: 16px 0;
  overflow-y: scroll; /* 세로 스크롤 활성화 */
  max-height: 100%; /* 부모 요소 높이에 맞춤 */
}

.vertical-nav button {
  margin: 10px 10px;
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #ffffff;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.vertical-nav button.active {
  background-color: #4285f4;
  color: white;
  border-color: #4285f4;
}

.vertical-nav button:hover {
  background-color: #e8f0fe;
}

/* 콘텐츠 스타일 */
.content-wrapper {
  display: flex;
  flex: 1;
  max-height: 100%;
}

/* 두 번째 세로 네비게이션 */
.vertical-nav-detail {
  width: 300px;
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  padding: 16px;
  overflow-y: auto;
}

/* 닫기 버튼 */
.close-button {
  position: absolute;
  top: 190px;
  left: 850px;
  background-color: #ff4d4f;
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: background-color 0.2s ease;
  z-index: 10; /* 다른 요소 위에 표시되도록 설정 */
}

.close-button:hover {
  background-color: #d9363e;
}

.detail-content {
  padding: 16px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.detail-content h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #333;
}

.detail-content p {
  margin: 4px 0;
  font-size: 14px;
  color: #666;
}

.map-section {
  flex: 1;
  padding: 16px;
}

.top-nav {
  display: flex;
  justify-content: left;
  align-items: center;
  padding: 16px;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  gap: 16px;
}

.nav-icon {
  width: 24px;
  height: 24px;
  margin-right: 12px;
}
</style>
