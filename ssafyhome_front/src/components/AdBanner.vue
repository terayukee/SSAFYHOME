<template>
  <div class="ad-banner">
    <img
      v-for="(ad, index) in ads"
      :key="index"
      :src="ad"
      :class="{
        active: currentIndex === index,
        inactive: currentIndex !== index,
      }"
      alt="광고 배너"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

// 광고 이미지 배열
const ads = [
  "/assets/mainImage/main1.jpg",
  "/assets/mainImage/main2.jpg",
  "/assets/mainImage/main3.jpg",
  "/assets/mainImage/main4.jpg",
  "/assets/mainImage/main5.jpg",
];

const currentIndex = ref(0); // 현재 표시 중인 이미지 인덱스

let interval = null; // 인터벌 ID 저장

// 이미지 변경 함수
const changeAd = () => {
  currentIndex.value = (currentIndex.value + 1) % ads.length;
};

// 컴포넌트가 마운트될 때 인터벌 시작
onMounted(() => {
  interval = setInterval(changeAd, 3000); // 3초마다 이미지 변경
});

// 컴포넌트가 언마운트될 때 인터벌 종료
onUnmounted(() => {
  clearInterval(interval);
});
</script>

<style scoped>
.ad-banner {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 7%;
  margin-right: 5%;
  position: relative; /* 자식 요소의 위치 제어를 위한 부모 설정 */
  width: 600px;
  height: 430px;
  overflow: hidden; /* 이미지가 영역 밖으로 넘어가지 않도록 설정 */
}

.ad-banner img {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지가 컨테이너에 맞게 잘림 */
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  opacity: 0; /* 기본적으로 보이지 않음 */
  transition: opacity 1s ease-in-out; /* 부드러운 전환 효과 */
}

.ad-banner img.active {
  opacity: 1; /* 현재 활성 이미지 */
  z-index: 2; /* 활성 이미지를 위에 표시 */
}

.ad-banner img.inactive {
  opacity: 0; /* 비활성 이미지 */
  z-index: 1; /* 비활성 이미지는 아래로 표시 */
}
</style>
