<template>
  <header class="header">
    <div class="header-left">
      <div class="logo" @click="goHome">
        <img :src="Logo" alt="로고" />
      </div>
      <SearchBar />
    </div>
    <nav class="header-right">
      <ul class="nav-menu">
        <li @click="navigateTo('board')">공지사항</li>
        <li @click="navigateTo('map')">지도</li>
        <li @click="navigateTo('news')">뉴스</li>
      </ul>
      <div class="auth-buttons">
        <div v-if="!isLogin" class="">
          <button class="btn btn-shadow mg-l-1" @click="navigateTo('login')">
            로그인
          </button>
          <button class="btn btn-gradient" @click="navigateTo('user-regist')">
            회원가입
          </button>
        </div>

        <div v-else>
          <!-- 로그인 후 내 정보 버튼만 표시 -->
          <button class="btn mg-l-1" @click="logout">로그아웃</button>
          <button class="btn btn-gradient" @click="navigateTo('user-info')">
            내 정보
          </button>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from "vue-router";

import SearchBar from "@/components/SearchBar.vue";

import Logo from "@/assets/logo.jpg";
import { useUserStore } from "@/stores/userStore";
import { storeToRefs } from "pinia";

const userStore = useUserStore();
const { isLogin } = storeToRefs(userStore);

const { userLogout } = userStore;

const logout = () => {
  userLogout();
  router.replace("/");
};

const router = useRouter();

const goHome = () => {
  router.push("/");
};

const navigateTo = (route) => {
  router.push({ name: `${route}` });
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
  min-width: 900px;
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

.mg-l-1 {
  margin-right: 10px;
}

.btn {
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

/* 기본 스타일 */
.btn-default {
  background-color: #f3f4f6;
  color: #374151;
}
.btn-default:hover {
  background-color: #e5e7eb;
}

/* 파란색 강조 스타일 */
.btn-primary {
  background-color: #2563eb;
  color: white;
}
.btn-primary:hover {
  background-color: #1d4ed8;
}

/* 테두리 있는 스타일 */
.btn-outline {
  background-color: transparent;
  border: 1px solid #d1d5db;
  color: #374151;
}
.btn-outline:hover {
  background-color: #f3f4f6;
  border-color: #9ca3af;
}

/* 투명 스타일 */
.btn-ghost {
  background-color: transparent;
  color: #374151;
}
.btn-ghost:hover {
  background-color: #f3f4f6;
}

/* 둥근 스타일 */
.btn-rounded {
  border-radius: 9999px;
  background-color: #f3f4f6;
  color: #374151;
}
.btn-rounded:hover {
  background-color: #e5e7eb;
}

/* 그라데이션 스타일 */
.btn-gradient {
  background: linear-gradient(45deg, #2563eb, #4f46e5);
  color: white;
}
.btn-gradient:hover {
  background: linear-gradient(45deg, #1d4ed8, #4338ca);
}

/* 그림자 스타일 */
.btn-shadow {
  background-color: white;
  color: #374151;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.btn-shadow:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.15);
}

/* 어두운 스타일 */
.btn-dark {
  background-color: #1f2937;
  color: white;
}
.btn-dark:hover {
  background-color: #111827;
}

/* 비활성화 상태 */
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 크기 변형 */
.btn-sm {
  padding: 4px 12px;
  font-size: 12px;
}

.btn-lg {
  padding: 8px 20px;
  font-size: 16px;
}
</style>
