<script setup>
import { ref, watch } from "vue";
import VKakaoMap from "@/components/common/VKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";
import { listHousesInBounds } from "@/api/house.js";

import ApartmentIcon from "@/assets/icons/residential.png";
import VillaIcon from "@/assets/icons/villa.png";
import OfficetelIcon from "@/assets/icons/office.png";

import HouseCardList from "@/components/map/HouseCardList.vue";
import HouseDetailCard from "@/components/map/HouseDetailCard.vue";

const props = defineProps({
  latitude: {
    type: Number,
    required: true,
  },
  longitude: {
    type: Number,
    required: true,
  },
  maplevel: {
    type: Number,
    required: false,
  },
  houseType: {
    type: String,
    required: false,
    default: "apartment",
  },
});

// 로컬 상태로 houseType 관리
const localHouseType = ref(props.houseType);

const houses = ref([]); // house 정보를 저장할 ref 변수
const bounds = ref(null); // 현재 지도 bounds
const selectedHouse = ref(null); // 선택된 하우스 정보를 저장

const navItems = [
  { id: "apartment", label: "아파트", icon: ApartmentIcon },
  { id: "villa", label: "주택/빌라", icon: VillaIcon },
  { id: "officetel", label: "오피스텔", icon: OfficetelIcon },
];

const fetchHousesInBounds = (bounds) => {
  console.log("bounds:", bounds);
  console.log("filters:", filters);
  listHousesInBounds(
    bounds.value,
    filters.value,
    localHouseType.value,
    (response) => {
      houses.value = response.data.map((house) => {
        if (localHouseType.value === "villa") {
          return {
            ...house,
            avgDealAmount: house.avgDealAmount
              ? Math.floor(house.avgDealAmount * 0.5)
              : house.avgDealAmount,
          };
        } else if (localHouseType.value === "officetel") {
          return {
            ...house,
            avgDealAmount: house.avgDealAmount
              ? Math.floor(house.avgDealAmount * 0.7)
              : house.avgDealAmount,
          };
        }
        return house;
      });

      console.log("listHousesInBounds 성공: ", houses.value);
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
  localHouseType.value = id;
  fetchHousesInBounds(bounds);
};

// 필터 데이터 및 옵션
const filters = ref({
  dealCategory: "매매",
  roomSize: "전체",
  approvalDate: "전체",
  additionalFilters: "",
});

const filterOptions = {
  dealCategory: [
    { text: "매매", value: "매매" },
    { text: "전세", value: "전세" },
    { text: "월세", value: "월세" },
  ],
  roomSize: [
    { text: "전체", value: "전체" },
    { text: "10평 이하", value: "10평 이하" },
    { text: "10~20평", value: "10~20평" },
    { text: "20평 이상", value: "20평 이상" },
  ],
  approvalDate: [
    { text: "전체", value: "전체" },
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

watch(
  () => filters,
  () => {
    console.log("Filters changed, fetching houses...");
    fetchHousesInBounds(bounds);
  },
  { deep: true }
);

const onCardClick = (house) => {
  selectedHouse.value = { ...house };
};

const handleMapClick = () => {
  selectedHouse.value = null;
};

// houseType 값 변경 감지
watch(localHouseType, (newNav) => {
  console.log("Navigation changed to:", newNav);
  fetchHousesInBounds(bounds);
});
</script>

<template>
  <div class="apt-map-view">
    <!-- 좌측 네비게이션 -->
    <aside class="left-nav">
      <ul>
        <li
          v-for="item in navItems"
          :key="item.id"
          :class="{ active: localHouseType === item.id }"
          @click="selectNav(item.id)"
        >
          <img :src="item.icon" alt="" class="nav-icon" />
          <span>{{ item.label }}</span>
        </li>
      </ul>
    </aside>

    <div class="main-content">
      <header class="top-nav">
        <VSelect
          :selectOption="filterOptions.dealCategory"
          placeholder="거래 유형"
          v-model="filters.dealCategory"
        />
        <VSelect
          :selectOption="filterOptions.roomSize"
          placeholder="방크기"
          v-model="filters.roomSize"
        />
        <VSelect
          :selectOption="filterOptions.approvalDate"
          placeholder="사용승인일"
          v-model="filters.approvalDate"
        />
      </header>

      <div class="content-wrapper">
        <nav class="vertical-nav">
          <HouseCardList
            :houses="houses"
            @cardClick="onCardClick"
            :houseType="localHouseType"
          />
        </nav>

        <nav class="vertical-nav-detail" v-if="selectedHouse">
          <button class="close-button" @click="selectedHouse = null">X</button>

          <HouseDetailCard
            :selectedHouse="selectedHouse"
            :houseType="localHouseType"
          />
        </nav>

        <section class="map-section">
          <VKakaoMap
            :houses="houses"
            :selectedCategory="filters.dealCategory"
            :houseType="localHouseType"
            :initialLatitude="latitude"
            :initialLongitude="longitude"
            :initialMapLevel="maplevel"
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
  width: 350px;
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  padding: 16px;
  overflow-y: auto;
}

/* 닫기 버튼 */
.close-button {
  position: absolute;
  top: 190px;
  left: 900px;
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
