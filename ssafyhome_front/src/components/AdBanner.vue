<template>
  <div class="ad-banner">
    <img :src="Ad" alt="광고 배너" />
  </div>
</template>

<script setup>
import Ad from "@/assets/seoul.png";
</script>

<style scoped>
.ad-banner {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 7%;
  margin-right: 5%;
}
.ad-banner img {
  max-width: 100%;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 600px;
  height: 430px;
}
</style>
