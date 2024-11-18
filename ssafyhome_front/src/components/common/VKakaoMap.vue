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
        loadInitialMarkers(); // 초기 데이터 로드 및 마커 표시
      });
    document.head.appendChild(script);
  }
});

// 초기 마커 로드 함수 추가
const loadInitialMarkers = () => {
  const bounds = map.getBounds(); // 초기 지도의 경계 가져오기
  const sw = bounds.getSouthWest(); // 남서쪽 좌표
  const ne = bounds.getNorthEast(); // 북동쪽 좌표

  const boundsParams = {
    swLat: sw.getLat(),
    swLng: sw.getLng(),
    neLat: ne.getLat(),
    neLng: ne.getLng(),
  };

  // 초기 경계 내 데이터를 요청
  emit("boundsChange", boundsParams);
};

watch(
  () => props.houses,
  (newHouses) => {
    console.log("Updated houses:", newHouses);
    if (newHouses.length === 0) {
      console.warn("No houses data provided to props.houses");
    }
    // 기존 로직 유지
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

  const newMarkers = houses
    .map((house) => {
      if (!house.latitude || !house.longitude) {
        console.warn("Invalid house coordinates:", house);
        return null; // 유효하지 않은 경우 생략
      }

      const position = new kakao.maps.LatLng(house.latitude, house.longitude);

      // 만 단위를 억 단위로 변환
      const dealAmountInEok = house.avgDealAmount
        ? (parseFloat(house.avgDealAmount) * 0.0001).toFixed(2) // 억 단위 변환 및 소수점 둘째 자리 반올림
        : "0.00";

      const content = `
        <div class="custom-overlay">
          <div class="custom-balloon">
            <div class="balloon-top"> 
              ${house.dealType || "N/A"}
            </div>
            <div class="balloon-bottom">
              매 ${dealAmountInEok}억
            </div>
          </div>
        </div>
      `;

      // 커스텀 오버레이 생성
      const customOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: content,
        yAnchor: 1,
      });

      customOverlay.setMap(map); // 오버레이를 지도에 표시
      return customOverlay;
    })
    .filter((overlay) => overlay !== null); // null 값 필터링

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

  console.log("onBoundsChange, ", sw, ne);

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

<style>
#map {
  width: 100%;
  height: 100vh;
}

.custom-marker {
  position: relative;
  width: 100px;
  text-align: center;
  border-radius: 10px;
  overflow: hidden;
  font-size: 12px;
  font-family: Arial, sans-serif;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.3);
}

.custom-overlay {
  position: relative;
}

.custom-balloon {
  display: inline-block;
  text-align: center;
  font-family: Arial, sans-serif;
  font-size: 12px;
  font-weight: bold;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);
}

.balloon-top {
  background-color: #4285f4;
  color: #ffffff;
  padding: 5px 10px;
  border-bottom: 1px solid #ffffff;
}

.balloon-bottom {
  background-color: #ffffff;
  color: #333333;
  padding: 5px 10px;
}
</style>
