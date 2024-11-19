<script setup>
import { ref, watch, onMounted } from "vue";

let map;
let clusterer;
const markers = ref([]); // Custom Overlay 마커
const clusterMarkers = ref([]); // Clustering에 사용할 기본 마커

const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["boundsChange"]);

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`;
    script.onload = () =>
      kakao.maps.load(() => {
        initMap();
        loadInitialMarkers();
      });
    document.head.appendChild(script);
  }
});

// 초기 데이터 로드 함수
const loadInitialMarkers = () => {
  const bounds = map.getBounds();
  const sw = bounds.getSouthWest();
  const ne = bounds.getNorthEast();

  const boundsParams = {
    swLat: sw.getLat(),
    swLng: sw.getLng(),
    neLat: ne.getLat(),
    neLng: ne.getLng(),
  };

  emit("boundsChange", boundsParams);
};

// 지도 초기화
const initMap = () => {
  const container = document.getElementById("map");

  const options = {
    center: new kakao.maps.LatLng(33.2683, 126.570667),
    level: 5,
  };

  map = new kakao.maps.Map(container, options);

  // 클러스터러 초기화
  clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 5, // 클러스터링이 활성화되는 최소 레벨
  });

  kakao.maps.event.addListener(map, "zoom_changed", onZoomChanged);

  // 초기 마커 로드
  updateMarkers(props.houses);
};

// 줌 변경 이벤트 핸들러
const onZoomChanged = () => {
  const level = map.getLevel();
  console.log("level : ", level);
  if (level > 5) {
    displayClusterMarkers(); // 클러스터 마커 표시
  } else {
    displayCustomMarkers(); // 커스텀 마커 표시
  }
};

// 클러스터 마커 표시
const displayClusterMarkers = () => {
  // 모든 커스텀 마커 제거
  markers.value.forEach((marker) => marker.setMap(null));
  clusterer.addMarkers(clusterMarkers.value); // 클러스터링에 기본 마커 추가
};

// 커스텀 마커 표시
const displayCustomMarkers = () => {
  // 클러스터 마커 제거
  clusterer.clear();
  markers.value.forEach((marker) => marker.setMap(map)); // 커스텀 마커 지도에 표시
};

// 마커 업데이트
const updateMarkers = (houses) => {
  // 기존 마커 제거
  markers.value.forEach((marker) => marker.setMap(null));
  clusterer.clear();

  markers.value = [];
  clusterMarkers.value = [];

  houses.forEach((house) => {
    if (!house.latitude || !house.longitude) {
      console.warn("Invalid house coordinates:", house);
      return;
    }

    const position = new kakao.maps.LatLng(house.latitude, house.longitude);

    // 클러스터링용 기본 마커
    const basicMarker = new kakao.maps.Marker({
      position: position,
    });

    clusterMarkers.value.push(basicMarker);

    // 커스텀 마커 스타일
    const dealAmountInEok = house.avgDealAmount
      ? (parseFloat(house.avgDealAmount) * 0.0001).toFixed(2)
      : "0.00";

    const content = `
      <div class="custom-marker">
        <div class="custom-marker-content">
          <div class="custom-marker-title">${house.dealType || "N/A"}</div>
          <div class="custom-marker-price">매 ${dealAmountInEok}억</div>
        </div>
      </div>
    `;

    const customOverlay = new kakao.maps.CustomOverlay({
      position: position,
      content: content,
      yAnchor: 1,
    });

    markers.value.push(customOverlay);
  });

  const level = map.getLevel();
  if (level <= 5) {
    displayClusterMarkers();
  } else {
    displayCustomMarkers();
  }
};

watch(
  () => props.houses,
  (newHouses) => {
    updateMarkers(newHouses);
  },
  { deep: true }
);
</script>

<template>
  <div id="map"></div>
</template>

<style>
#map {
  width: 100%;
  height: 100vh;
  position: relative;
  z-index: 1; /* 맵이 다른 요소 위로 올라오도록 설정 */
}

.custom-marker {
  position: relative;
  width: 70px;
  text-align: center;
  font-size: 12px;
  font-family: Arial, sans-serif;
}

.custom-marker-content {
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.custom-marker-title {
  background: #136dff;
  color: white;
  padding: 5px;
  font-weight: bold;
}

.custom-marker-price {
  padding: 5px;
  background: white;
  color: black;
}
</style>
