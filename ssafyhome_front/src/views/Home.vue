<template>
  <div class="home">
    <input
      type="text"
      placeholder="지역, 지하철, 대학, 단지명 또는 매물번호를 입력해주세요."
      class="search-bar"
    />
    <MainCategory />
    <AdBanner />
  </div>
</template>

<script setup>
import MainCategory from "../components/MainCategory.vue";
import AdBanner from "../components/AdBanner.vue";
</script>

<style scoped>
.home {
  padding: 16px;
}
.search-bar {
  width: 100%;
  padding: 8px;
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
