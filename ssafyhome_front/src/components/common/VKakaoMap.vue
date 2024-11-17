<script setup>
import { ref, watch, onMounted } from "vue";

let map;
const positions = ref([]);
const markers = ref([]);

const props = defineProps({
  stations: {
    type: Array,
    default: () => [],
  },
  selectStation: {
    type: Object,
    default: () => ({}),
  },
});

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
  () => props.stations,
  (newStations) => {
    if (Array.isArray(newStations) && newStations.length > 0) {
      positions.value = newStations.map((station) => ({
        latlng: new kakao.maps.LatLng(station.lat, station.lng),
        title: station.statNm,
      }));
      loadMarkers();
    }
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

  if (positions.value.length > 0) {
    const bounds = positions.value.reduce(
      (bounds, position) => bounds.extend(position.latlng),
      new kakao.maps.LatLngBounds()
    );
    map.setBounds(bounds);
  }
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
  height: 700px;
}
</style>
