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
  margin-top: 16px;
  text-align: center;
}
.ad-banner img {
  max-width: 100%;
  border-radius: 8px;
}
</style>
