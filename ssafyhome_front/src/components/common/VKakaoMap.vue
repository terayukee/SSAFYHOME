<script setup>
import { ref, watch, onMounted } from "vue";

let map;
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
    level: 3,
  };
  map = new kakao.maps.Map(container, options);

  kakao.maps.event.addListener(map, "bounds_changed", onBoundsChange);
};

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
