<script setup>
import { onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';
const { getUserInfo, userLogin } = useUserStore();
const router = useRouter();

// 카카오 REST API 키와 리디렉션 URI 설정
const KAKAO_REST_API_KEY = '4736eef2397a78d68348b4f3fdbea4ca';
const KAKAO_REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback';

// 카카오 로그인 리디렉션
const kakaoLogin = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
  
  // 팝업 창을 열어 카카오 로그인 페이지로 리디렉션
  const loginBox = window.open(kakaoAuthUrl, '_blank', 'width=800, height=600');
};

onMounted(() => {
  window.addEventListener('message', (event) => {
    // 받은 데이터 확인
    const { accessToken, refreshToken, error } = event.data;
    if (error) {
      console.error('카카오 로그인 오류:', error);
    } else {
      // 토큰을 localStorage에 저장
      // localStorage.setItem('accessToken', accessToken);
      // localStorage.setItem('refreshToken', refreshToken);
      console.log(accessToken)
      // 액세스 토큰으로 사용자 정보 가져오기
      getUserInfo(accessToken).then(() => {
        // 로그인 후 메인 페이지로 리디렉션
        router.replace('/');
      }).catch((error) => {
        console.error("사용자 정보 가져오기 실패:", error);
      });
    }
  });

})
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
