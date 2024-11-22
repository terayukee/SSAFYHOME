<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const KAKAO_REST_API_KEY = `4736eef2397a78d68348b4f3fdbea4ca`;
const KAKAO_REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback';

// 카카오 로그인 리디렉션
// const kakaoLogin = () => {
//   const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
  
//   // // 바로 카카오 인증 페이지로 리디렉션
//   // window.location.href = kakaoAuthUrl;
//   const loginBox= window.open(kakaoAuthUrl, '_blank', 'width=800, height=600');
// };
const kakaoLogin = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
  
  // 로그인 페이지를 새로운 창으로 열기
  const loginBox = window.open(kakaoAuthUrl, '_blank', 'width=800, height=600');

  // 메시지 이벤트 리스너 등록 (메인 창에서 로그인 결과를 받을 때)
  window.addEventListener('message', (event) => {
    // 보안을 위해 출처를 체크 (필요에 따라 출처를 제한)
    if (event.origin !== 'https://your-trusted-origin.com') {  // 출처를 확인하여 보호
      return;
    }

    // 받은 데이터 처리
    const receivedData = event.data;
    console.log('Received Data:', receivedData);

    // 예를 들어, `loginBox`에 받은 데이터를 표시하고 싶다면:
    if (loginBox && !loginBox.closed) {
      loginBox.postMessage(receivedData, '*');  // *는 모든 출처에 대해 허용. 보안을 위해 특정 출처로 제한하는 것이 좋음
    }
  });
};

// loginBox 창에서 수신하는 데이터 처리
const setupLoginBox = () => {
  // 새 창에서 메시지 받기
  window.addEventListener('message', (event) => {
    // 보안을 위해 출처를 체크
    if (event.origin !== 'https://your-trusted-origin.com') {  // 실제 출처로 변경
      return;
    }

    // 받은 데이터 처리
    const data = event.data;
    console.log('Received Data in loginBox:', data);

    // 받은 데이터를 로그인 박스에 표시 (예: 로그인 후 토큰이나 사용자 데이터)
    const resultElement = document.getElementById('result');
    resultElement.textContent = JSON.stringify(data, null, 2);
  });
};

// 페이지가 로드되면 `loginBox`에서 데이터 수신 준비
setupLoginBox();

// 예를 들어, 카카오 로그인 후 받은 데이터를 외부에서 `loginBox`로 전송
const sendLoginDataToLoginBox = (data) => {
  const loginBox = window.open('', '_blank');  // 이미 열린 loginBox
  if (loginBox && !loginBox.closed) {
    loginBox.postMessage(data, '*');  // `loginBox`로 데이터 전송
  }
};

// 외부 시스템에서 데이터를 받았다면
const externalData = { message: '카카오 로그인 성공', userId: '12345' };
sendLoginDataToLoginBox(externalData);

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
