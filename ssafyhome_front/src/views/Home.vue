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
  </div>
</template>

<script setup>
import MainCategory from "../components/MainCategory.vue";
import AdBanner from "../components/AdBanner.vue";
import SearchBar from "../components/SearchBar.vue";
</script>

<style scoped>
.home {
  padding: 16px;
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

/* MainCategory와 AdBanner의 가로 영역 비율 조정 */
.main-category {
  flex: 1; /* MainCategory의 너비를 50%로 설정 */
  width: 600px;
  height: 400px;
}

.ad-banner {
  flex: 1; /* AdBanner의 너비를 50%로 설정 */
  width: 600px;
  height: 400px;
}
</style>
