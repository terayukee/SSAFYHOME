<template>
  <div class="ad-banner">
    <img :src="Ad" alt="광고 배너" />
  </div>
</template>

<script setup>
import Ad from "@/assets/findmyhome.jpg";
</script>

<style scoped>
.ad-banner {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ad-banner img {
  max-width: 100%;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
