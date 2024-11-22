<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const KAKAO_REST_API_KEY = `4736eef2397a78d68348b4f3fdbea4ca`;
const KAKAO_REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback';

// 카카오 로그인 리디렉션
const kakaoLogin = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
  
  // 바로 카카오 인증 페이지로 리디렉션
  window.location.href = kakaoAuthUrl;
};

// 페이지 로드 후 URL에서 액세스 토큰과 리프레시 토큰을 추출하여 처리
onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search);
  const accessToken = urlParams.get("access_token");
  const refreshToken = urlParams.get("refresh_token");

  if (accessToken && refreshToken) {
    console.log("Access Token:", accessToken);
    console.log("Refresh Token:", refreshToken);

    // 토큰을 localStorage에 저장
    localStorage.setItem("access_token", accessToken);
    localStorage.setItem("refresh_token", refreshToken);

    // 로그인 성공 후 메인 페이지로 리다이렉트
    router.push('/');
  }
});
</script>

<template>
  <div class="kakao-login-container">
    <button @click="kakaoLogin" class="kakao-login-button">
      <img src="/src/assets/icons/kakao/kakao_login_medium_narrow.png" alt="카카오 로그인" />
    </button>
  </div>
</template>

<style scoped>
.kakao-login-button {
  background-color: #FEE500;
  color: black;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
  width: 100%;
  cursor: pointer;
}
</style>
