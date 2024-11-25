<script setup>
import { onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from "pinia";
import { useUserStore } from '@/stores/userStore';

const userStore = useUserStore();
const { getUserInfo } = userStore;
const { isLogin, isLoginError, isValidToken } = storeToRefs(userStore);
const router = useRouter();

const KAKAO_REST_API_KEY = '4736eef2397a78d68348b4f3fdbea4ca';
const KAKAO_REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback';

let loginBox = null;

// 메시지 이벤트 리스너
const handleMessage = async (event) => {
  const { accessToken, refreshToken, error } = event.data;
  if (error) {
    console.error('카카오 로그인 오류:', error);
  } else {
    console.log('AccessToken:', accessToken);
    console.log('RefreshToken:', refreshToken);
    try {
      await getUserInfo(accessToken);
      isLogin.value = true;
      isLoginError.value = false;
      isValidToken.value = true;
      router.replace('/');
    } catch (err) {
      console.error("사용자 정보 가져오기 실패:", err);
    }
  }
};

const kakaoLogin = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
  
  // 팝업 창 열기
  loginBox = window.open(kakaoAuthUrl, '_blank', 'width=800, height=600');
  
  // 메시지 리스너 추가
  window.addEventListener('message', handleMessage);
};

// 컴포넌트 언마운트 시 이벤트 리스너 제거
onBeforeUnmount(() => {
  window.removeEventListener('message', handleMessage);
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
