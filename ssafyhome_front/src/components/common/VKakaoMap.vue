<script setup>
import { ref, watch, onMounted, defineProps, defineEmits } from "vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/userStore";
import { fetchUserFavoriteHouses } from "@/api/favorite"; // API 호출

const userStore = useUserStore();
const { isLogin, accessToken, userInfo } = storeToRefs(userStore);

let map;
let clusterer;
const markers = ref([]); // Custom Overlay 마커
const clusterMarkers = ref([]); // Clustering에 사용할 기본 마커
const clusterLevel = 5;

const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },

  selectedCategory: {
    type: String,
  },

  houseType: {
    type: String,
  },
  initialLatitude: {
    type: Number,
    // default: 37.514575,
  },
  initialLongitude: {
    type: Number,
    // default: 127.0495556,
  },
  initialMapLevel: {
    type: Number,
    default: clusterLevel,
  },
});

const emit = defineEmits(["boundsChange", "markerClick", "mapClick"]);

onMounted(() => {
  // 사용자의 관심 주택 불러오기
  fetchFavorites();

  if (window.kakao && window.kakao.maps) {
    initMap();
    loadInitialMarkers();
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
  console.log(
    "위도, 경도 props 전달",
    props.initialLatitude,
    props.initialLongitude
  );

  const container = document.getElementById("map");

  const options = {
    center: new kakao.maps.LatLng(
      props.initialLatitude,
      props.initialLongitude
    ),
    level: props.initialMapLevel,
  };

  map = new kakao.maps.Map(container, options);

  // 클러스터러 초기화
  clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: clusterLevel, // 클러스터링이 활성화되는 최소 레벨
  });

  // 초기 bounds 정보 부모에게 전달
  const bounds = map.getBounds(); // 현재 지도 경계를 가져옴
  // emitBoundsChange(bounds);

  kakao.maps.event.addListener(map, "zoom_changed", onZoomChanged);
  kakao.maps.event.addListener(map, "bounds_changed", onBoundsChanged); // 지도 경계 변경 이벤트 추가

  // 초기 마커 로드
  updateMarkers(props.houses);

  kakao.maps.event.addListener(map, "click", () => {
    emit("mapClick"); // 부모 컴포넌트로 지도 클릭 이벤트 전달
  });
};

// 줌 변경 이벤트 핸들러
const onZoomChanged = () => {
  const level = map.getLevel();
  console.log("level : ", level);
  if (level >= clusterLevel) {
    console.log("클러스터링");
    displayClusterMarkers(); // 클러스터 마커 표시
  } else {
    console.log("커스텀 마커");
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
  console.log("houses : ", houses);
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

    // 관심 단지 여부 확인
    const isFavorite = favoriteAptSeqs.value.has(house.aptSeq);

    // 커스텀 마커 스타일
    const dealAmountInEok = house.avgDealAmount
      ? (parseFloat(house.avgDealAmount) * 0.0001).toFixed(2)
      : "0.00";

    const content = `
      <div class="custom-marker">
        <div class="custom-marker-content">
          <div class="custom-marker-title" style="background: ${
            isFavorite ? "red" : "#136dff"
          }">${house.dealSpace}평</div>
          <div class="custom-marker-price">
            ${
              props.selectedCategory === "월세"
                ? `${house.avgDealAmount || "N/A"} / ${
                    house.avgFeeAmount || "N/A"
                  }만`
                : `${props.selectedCategory} ${dealAmountInEok}억`
            }
          </div>
        </div>
      </div>
    `;

    const customOverlay = new kakao.maps.CustomOverlay({
      position: position,
      content: content,
      yAnchor: 1,
    });

    // 클릭 이벤트 추가
    // 여기에서 DOM 요소를 클릭했을 때 이벤트 연결
    const element = document.createElement("div");
    element.innerHTML = content.trim();
    element.addEventListener("click", () => {
      handleMarkerClick(house); // 마커 클릭 이벤트 핸들러
    });

    customOverlay.setContent(element);

    markers.value.push(customOverlay);
  });

  const level = map.getLevel();
  if (level >= clusterLevel) {
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

// 지도 경계 변경 이벤트 핸들러
let debounceTimeout; // 디바운싱 타이머를 위한 변수

const onBoundsChanged = () => {
  if (debounceTimeout) {
    clearTimeout(debounceTimeout); // 이전 타이머를 취소
  }

  debounceTimeout = setTimeout(() => {
    const bounds = map.getBounds(); // 현재 지도 경계를 가져옴
    emitBoundsChange(bounds); // 부모 컴포넌트에 `boundsChange` 이벤트 전달
  }, 300); // 300ms 후에 실행
};

// `boundsChange` 이벤트를 부모에 전달하는 함수
const emitBoundsChange = (bounds) => {
  const sw = bounds.getSouthWest();
  const ne = bounds.getNorthEast();

  const boundsParams = {
    swLat: sw.getLat(),
    swLng: sw.getLng(),
    neLat: ne.getLat(),
    neLng: ne.getLng(),
  };

  console.log("Bounds changed:", bounds); // 디버그용
  emit("boundsChange", boundsParams);
};

// 마커 클릭 핸들러
const handleMarkerClick = (house) => {
  console.log("마커 클릭, house : ", house);
  emit("markerClick", house); // 선택된 house 정보를 부모로 emit
};

// 지도의 중심을 업데이트
const updateMapCenter = (latitude, longitude) => {
  const center = new kakao.maps.LatLng(latitude, longitude);
  map.setCenter(center);
};

// props의 변경을 감지하여 지도 중심 업데이트
watch(
  () => [props.initialLatitude, props.initialLongitude],
  ([newLat, newLng], [oldLat, oldLng]) => {
    if (map && (newLat !== oldLat || newLng !== oldLng)) {
      updateMapCenter(newLat, newLng);
    }
  }
);

const favoriteAptSeqs = ref(new Set());
// 관심 단지 불러오기
const fetchFavorites = async () => {
  if (userStore.isLogin) {
    const userNo = userStore.userInfo.userNo;
    await fetchUserFavoriteHouses({ userNo }, (response) => {
      favoriteAptSeqs.value = new Set(
        response.data
          .filter((item) => item.houseType === props.houseType)
          .map((item) => item.aptSeq)
      );
    });
  }
};

watch(
  () => props.houseType,
  (newHouseType, oldHouseType) => {
    if (newHouseType !== oldHouseType) {
      console.log("houseType 변경:", newHouseType);
      fetchFavorites(); // houseType 변경 시 관심 단지 목록 다시 로드
    }
  }
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
  width: 75px;
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
