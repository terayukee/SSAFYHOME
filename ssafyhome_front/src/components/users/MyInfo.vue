<template>
  <div class="my-page">
    <div class="tab-container">
      <div
        class="tab"
        :class="{ active: activeTab === 'userInfo' }"
        @click="activeTab = 'userInfo'"
      >
        내 정보
      </div>
      <div
        class="tab"
        :class="{ active: activeTab === 'favoriteHouse' }"
        @click="activeTab = 'favoriteHouse'"
      >
        관심단지
      </div>
      <div
        class="tab"
        :class="{ active: activeTab === 'favoriteRealEstate' }"
        @click="activeTab = 'favoriteRealEstate'"
      >
        관심매물
      </div>
    </div>

    <div class="content-container" v-if="userInfo">
      <DetailUserInfo v-if="activeTab === 'userInfo'" />
      <FavoriteHouse v-if="activeTab === 'favoriteHouse'" />
      <FavoriteRealEstate v-if="activeTab === 'favoriteRealEstate'" />
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from "vue";
import DetailUserInfo from "@/components/users/myinfo/DetailUserInfo.vue";
import FavoriteHouse from "@/components/users/myinfo/FavoriteHouse.vue";
import FavoriteRealEstate from "@/components/users/myinfo/FavoriteRealEstate.vue";
import { useUserStore } from "@/stores/userStore";
import { useRouter } from "vue-router";

// User 정보 및 라우터
const userStore = useUserStore();
const { userInfo } = userStore;
const router = useRouter();

// 현재 활성화된 탭 상태
const activeTab = ref("userInfo");

onBeforeUnmount(() => {
  if (!userInfo) {
    router.push({ name: "login" });
  }
});
</script>

<style scoped>
.my-page {
  width: 100%;
  max-width: 1024px;
  margin: 0 auto;
  padding: 20px;
}

.tab-container {
  justify-content: center;
  display: flex;
  gap: 20px;
  margin-top: 50px;
  margin-bottom: 20px;
}

.tab {
  padding: 15px 40px;
  border: none;
  cursor: pointer;
  font-size: 16px;
  min-width: 200px;
  text-align: center;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
}

.tab.active {
  background-color: #333;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
