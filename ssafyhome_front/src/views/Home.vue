<template>
  <div class="home">
    <div class="top-section">
      <div class="search-bar">
        <SearchBar />
      </div>
    </div>
    <div class="content-section">
      <div class="content-inside">
        <div class="main-category">
          <MainCategory />
        </div>
        <div class="ad-banner">
          <AdBanner />
        </div>
      </div>
    </div>

    <!-- Guide 컴포넌트 삽입 -->
    <Guide />
  </div>
</template>

<script setup>
import MainCategory from "../components/MainCategory.vue";
import AdBanner from "../components/AdBanner.vue";
import SearchBar from "../components/SearchBar.vue";
import Guide from "../components/Guide.vue";
</script>

<style scoped>
.home {
  padding: 16px;
  min-width: 1400px;
}

.top-section {
  display: flex;
  justify-content: center;
  background-color: #ececec;
  height: 50px;
}

.content-section {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 500px;
  gap: 16px;
  background-color: #ececec;
}

.search-bar {
  margin-top: 20px;
  width: 1100px;
}

.content-inside {
  display: flex;
  justify-content: center;
  width: 1300px;
}

.main-category {
  flex: 1;
  width: 600px;
  height: 400px;
}

.ad-banner {
  flex: 1;
  width: 600px;
  height: 400px;
}
</style>
