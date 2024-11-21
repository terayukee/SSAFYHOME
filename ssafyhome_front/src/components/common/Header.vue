<template>
  <header class="header">
    <div class="header-left">
      <div class="logo" @click="goHome">
        <img :src="Logo" alt="로고" />
      </div>
      <input
        type="text"
        placeholder="지역, 지하철, 대학, 단지명 또는 매물번호를 입력해주세요."
        class="search-bar"
      />
    </div>
    <nav class="header-right">
      <ul class="nav-menu">
        <li @click="navigateTo('board')">공지사항</li>
        <li @click="navigateTo('map')">지도</li>
        <li @click="navigateTo('sale')">분양</li>
        <li @click="navigateTo('favorites')">관심목록</li>
      </ul>
      <div class="auth-buttons">
        
    <div v-if="!isLogin" class="">
      <button class="btn mg-l-1" @click="navigateTo('login')">로그인</button>
      <button class="btn btn-primary" @click="navigateTo('signup')">회원가입</button>
    </div>
    
    <div v-else>
      <!-- 로그인 후 내 정보 버튼만 표시 -->
      <button class="btn mg-l-1" @click="navigateTo('login')">로그인</button>
      <button class="btn btn-primary" @click="navigateTo('profile')">내 정보</button>
    </div>
        <!-- <button class="btn" @click="navigateTo('login')">로그인</button>
        <button class="btn btn-primary" @click="navigateTo('signup')">
          회원가입
        </button> -->
      </div>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from "vue-router";
import Logo from "@/assets/logo.jpg";
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from "pinia";

const userStore = useUserStore();
const { isLogin } = storeToRefs(userStore)

const router = useRouter();

const goHome = () => {
  router.push("/");
};

const navigateTo = (route) => {
  router.push({name : `${route}`});
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  cursor: pointer;
  margin-right: 16px;
}

.search-bar {
  width: 400px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.header-right {
  display: flex;
  align-items: center;
}

.nav-menu {
  display: flex;
  margin-right: 24px;
  list-style: none;
  padding: 0;
}

.nav-menu li {
  margin: 0 12px;
  cursor: pointer;
  font-size: 16px;
  color: #333;
}

.nav-menu li:hover {
  text-decoration: underline;
}

.auth-buttons {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 8px 16px;
  border: 1px solid #ccc;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary {
  border: none;
  background-color: #007bff;
  color: white;
}

.btn:hover {
  background-color: #f0f0f0;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.mg-l-1{
  margin-right: 10px;
}
</style>
