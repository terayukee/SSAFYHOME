<script setup>
import { ref, watch, onMounted } from "vue";

let map;
let clusterer; // 클러스터러 선언
const positions = ref([]);
const markers = ref([]);

const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["boundsChange"]);

watch(
  () => props.selectStation,
  (newStation) => {
    if (newStation?.lat && newStation?.lng) {
      const moveLatLon = new kakao.maps.LatLng(newStation.lat, newStation.lng);
      map.panTo(moveLatLon);
    }
  },
  { deep: true }
);

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`;
    script.onload = () => kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }
});

watch(
  () => props.houses,
  (newHouses) => {
    positions.value = newHouses.map((house) => ({
      latlng: new kakao.maps.LatLng(house.latitude, house.longitude),
      title: house.aptNm,
    }));
    loadMarkers();
  },
  { deep: true }
);

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 4,
  };
  map = new kakao.maps.Map(container, options);

  // 클러스터러 초기화
  clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 5,
  });

  // 초기 마커 로드
  updateMarkers(props.houses);

  kakao.maps.event.addListener(map, "bounds_changed", onBoundsChange);
};

// 마커 업데이트
const updateMarkers = (houses) => {
  // 기존 마커 제거
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value = [];

  const newMarkers = houses.map((house) => {
    // 커스텀 오버레이 내용
    const content = `
      <div class="custom-overlay">
        <p>${house.dealType || "N/A"}㎡</p>
        <p>${house.avgDealAmount || "N/A"}만원</p>
      </div>
    `;

    // 커스텀 오버레이 생성
    const customOverlay = new kakao.maps.CustomOverlay({
      map: map,
      position: new kakao.maps.LatLng(house.latitude, house.longitude),
      content: content,
      yAnchor: 1, // 마커 아래쪽에 배치
    });

    // 커스텀 오버레이를 마커 배열에 저장
    return customOverlay;
  });

  clusterer.clear();
  clusterer.addMarkers(newMarkers);

  markers.value = newMarkers;
};

// props.houses가 변경될 때 마커 업데이트
watch(
  () => props.houses,
  (newHouses) => {
    updateMarkers(newHouses);
  },
  { deep: true }
);

const onBoundsChange = () => {
  const bounds = map.getBounds(); // 현재 지도 영역
  const sw = bounds.getSouthWest(); // 남서쪽 좌표
  const ne = bounds.getNorthEast(); // 북동쪽 좌표

  // emit 이벤트로 부모 컴포넌트에 전달
  emit("boundsChange", {
    swLat: sw.getLat(),
    swLng: sw.getLng(),
    neLat: ne.getLat(),
    neLng: ne.getLng(),
  });
};

const loadMarkers = () => {
  deleteMarkers();

  markers.value = positions.value.map((position) => {
    const marker = new kakao.maps.Marker({
      map: map,
      position: position.latlng,
      title: position.title,
      clickable: true,
    });
    return marker;
  });

  // if (positions.value.length > 0) {
  //   const bounds = positions.value.reduce(
  //     (bounds, position) => bounds.extend(position.latlng),
  //     new kakao.maps.LatLngBounds()
  //   );
  //   map.setBounds(bounds);
  // }
};

const deleteMarkers = () => {
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value = [];
};

// 최근 거래 정보 
const fetchDealData = async (aptSeq) => {
  try {
    const response = await getRecentDeals(aptSeq);
    return response.data;
  } catch (error) {
    console.error("Error fetching recent deals:", error);
    return [];
  }
};
</script>

<template>
  <div id="map"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100vh;
}
</style>