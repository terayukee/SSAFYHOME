<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import KakaoLogin from "./KakaoLogin.vue";
const userStore = useUserStore();
const { isLogin } = storeToRefs(userStore);
const { userLogin } = useUserStore();
const router = useRouter();

const loginUser = ref({
  email: "",
  userPassword: "",
});

const isLoginFail = ref(false);

const login = async () => {
  // 로그인 처리 + 액세스 토큰에서 정보 뽑아오기
  await userLogin(loginUser.value);

  // 1. 로그인 성공 시 메인 화면으로 이동
  if (isLogin.value) {
    console.log("로그인 성공");
    router.replace("/");
  }
  // 2. 로그인 실패 시 처리
  else {
    isLoginFail.value = true;
    loginUser.value.userPassword = "";
  }
};

const mvUserRegister = () => {
  router.push({ name: `user-regist` });
};
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 헤더 -->
      <div class="login-header">
        <h2>로그인</h2>
        <p class="signup-text">
          계정이 없으신가요?
          <span class="link" @click="mvUserRegister">회원가입</span>
        </p>
      </div>

      <!-- 폼 -->
      <form class="login-form" @submit.prevent="login">
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            type="email"
            v-model="loginUser.email"
            required
            placeholder="example@email.com"
          />
        </div>

        <div class="form-group">
          <label for="userPassword">비밀번호</label>
          <input
            id="userPassword"
            type="password"
            v-model="loginUser.userPassword"
            required
            placeholder="비밀번호를 입력하세요"
          />
        </div>
        <div>
          <p v-if="isLoginFail" class="login-failed-message">
            로그인 실패! 이메일 또는 비밀번호를 확인해주세요.
          </p>
        </div>

        <button type="submit" class="primary-button">로그인</button>

        <div class="divider">
          <span>또는</span>
        </div>
      </form>
      <div class="mg-d-20">
        <KakaoLogin />
      </div>

      <button @click="mvUserRegister" class="secondary-button">회원가입</button>
    </div>
  </div>
</template>

<style scoped>
/* 로그인 실패 메시지 스타일 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  /* background-color: #f8f9fa; */
  background-image: url("/assets/background/2.webp");
  background-size: cover; /* 이미지를 요소 크기에 맞게 조정 */
  background-position: center; /* 이미지를 중앙에 배치 */
  background-repeat: no-repeat; /* 이미지 반복 방지 */
  position: relative;
}

.login-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 검정색, 투명도 조정 */
  z-index: 1;
  position: absolute;
}

.login-card {
  width: 100%;
  max-width: 420px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  z-index: 2;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h2 {
  margin: 0;
  font-size: 28px;
  color: #1a1a1a;
  margin-bottom: 12px;
  font-weight: 600;
}

.signup-text {
  font-size: 15px;
  color: #666;
  margin: 0;
}

.link {
  color: #3182f6;
  text-decoration: none;
  font-weight: 500;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 15px;
  color: #1a1a1a;
  font-weight: 500;
}

.form-group input {
  padding: 12px 16px;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  font-size: 15px;
  outline: none;
  transition: all 0.2s ease;
}

.form-group input:focus {
  border-color: #3182f6;
  box-shadow: 0 0 0 3px rgba(49, 130, 246, 0.1);
}

.form-group input::placeholder {
  color: #a0a0a0;
}

.form-options {
  display: flex;
  justify-content: flex-end;
  font-size: 14px;
  margin-top: -8px;
}

.primary-button,
.secondary-button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.primary-button {
  background-color: #3182f6;
  color: white;
}

.primary-button:hover {
  background-color: #2872e5;
}

.secondary-button {
  background-color: #f8f9fa;
  color: #1a1a1a;
  border: 1px solid #e1e1e1;
}

.secondary-button:hover {
  background-color: #f1f3f5;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  margin: 8px 0;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid #e1e1e1;
}

.divider span {
  padding: 0 16px;
  color: #666;
  font-size: 14px;
}

.mg-d-20 {
  margin-bottom: 20px;
}

/* 로그인 실패 메시지 스타일 */
.login-failed-message {
  color: red;
  margin-top: 4px;
  font-weight: bold;
  font-size: 12px;
}
</style>
