<script setup>
import { ref, onMounted } from "vue";
import VKakaoMap from "@/components/common/VKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";
import { listSido, listGugun } from "@/api/map";

const VITE_OPEN_API_SERVICE_KEY = import.meta.env.VITE_OPEN_API_SERVICE_KEY;

const filters = ref({
  rentType: "",
  roomSize: "",
  approvalDate: "",
  numHouseholds: "",
  parkingSpaces: "",
  numRooms: "",
  additionalFilters: "",
});

const filterOptions = {
  rentType: [
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
  numHouseholds: [
    { text: "50세대 이하", value: "50세대 이하" },
    { text: "50~100세대", value: "50~100세대" },
    { text: "100세대 이상", value: "100세대 이상" },
  ],
  parkingSpaces: [
    { text: "1대 이하", value: "1대 이하" },
    { text: "2대", value: "2대" },
    { text: "3대 이상", value: "3대 이상" },
  ],
  numRooms: [
    { text: "1개", value: "1개" },
    { text: "2개", value: "2개" },
    { text: "3개 이상", value: "3개 이상" },
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
</script>

<template>
  <div class="apt-map-view">
    <!-- 상단 네비게이션 -->
    <header class="top-nav">
      <VSelect
        :selectOption="filterOptions.rentType"
        placeholder="월세, 전세, 매매"
        @onKeySelect="(val) => onFilterChange('rentType', val)"
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
      <VSelect
        :selectOption="filterOptions.numHouseholds"
        placeholder="세대수"
        @onKeySelect="(val) => onFilterChange('numHouseholds', val)"
      />
      <VSelect
        :selectOption="filterOptions.parkingSpaces"
        placeholder="주차대수"
        @onKeySelect="(val) => onFilterChange('parkingSpaces', val)"
      />
      <VSelect
        :selectOption="filterOptions.numRooms"
        placeholder="방수"
        @onKeySelect="(val) => onFilterChange('numRooms', val)"
      />
      <VSelect
        :selectOption="filterOptions.additionalFilters"
        placeholder="추가필터"
        @onKeySelect="(val) => onFilterChange('additionalFilters', val)"
      />
    </header>

    <div class="main-content">
      <!-- 좌측 네비게이션 -->
      <aside class="left-nav">
        <ul>
          <li>원/투룸</li>
          <li>아파트</li>
          <li>주택/빌라</li>
          <li>오피스텔</li>
          <li>분양</li>
        </ul>
      </aside>

      <!-- 지도 및 결과 영역 -->
      <section class="map-section">
        <VKakaoMap
          :stations="chargingStations"
          :selectStation="selectStation"
        />
      </section>
    </div>
  </div>
</template>

<style scoped>
.apt-map-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 상단 네비게이션 스타일 */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #e0e0e0;
  gap: 16px;
}

.top-nav > * {
  flex: 1;
}

/* 메인 컨텐츠 영역 */
.main-content {
  display: flex;
  flex: 1;
}

/* 좌측 네비게이션 스타일 */
.left-nav {
  width: 200px;
  padding: 16px;
  background-color: #f3f3f3;
  border-right: 1px solid #e0e0e0;
}
.left-nav ul {
  list-style: none;
  padding: 0;
}
.left-nav li {
  margin: 8px 0;
  cursor: pointer;
}

/* 지도 섹션 */
.map-section {
  flex: 1;
  padding: 16px;
}
</style>
