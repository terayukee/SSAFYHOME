<template>
  <div class="home">
    <div class="top-section">
      <input
        type="text"
        placeholder="지역, 지하철, 대학, 단지명 또는 매물번호를 입력해주세요."
        class="search-bar"
      />
    </div>
    <div class="content-section">
      <div class="main-category">
        <MainCategory />
      </div>
      <div class="ad-banner">
        <AdBanner />
      </div>
    </div>
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
.top-section {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}
.search-bar {
  width: 60%;
  padding: 10px 16px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 25px;
  outline: none;
}
.content-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

/* MainCategory와 AdBanner의 가로 영역 비율 조정 */
.main-category {
  flex: 1; /* MainCategory의 너비를 50%로 설정 */
}

.ad-banner {
  flex: 1; /* AdBanner의 너비를 50%로 설정 */
}
</style>
