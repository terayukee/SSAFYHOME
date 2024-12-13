<script setup>
import { onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/userStore";

// Key
const { VITE_KAKAO_REST_API_KEY, VITE_KAKAO_REDIRECT_URI } = import.meta.env;

// Pinia Store
const userStore = useUserStore();
const { setUserInfo, parseAccessToken, setToken } = userStore;

// Vue Router
const router = useRouter();

// 팝업 창 참조
let loginBox = null;

// 메시지 이벤트 리스너
const handleMessage = (event) => {
  // 출처 확인
  if (event.origin !== window.location.origin) {
    console.error("Invalid message origin:", event.origin);
    return;
  }

  // jwtToken 세팅
  console.log("카카오 로그인 토큰 : ", event.data);
  setToken(event.data);

  // accessToken 디코딩 후 상태 갱신
  const userData = parseAccessToken(event.data["access-token"]);
  setUserInfo(userData);

  if (event.data.error) {
    console.error("카카오 로그인 오류:", event.data.error);
    alert("로그인 실패: " + event.data.error);
    return;
  }

  // 메인 페이지로 이동
  router.replace("/"); // Home.vue로 이동
};

// 카카오 로그인
const kakaoLogin = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${VITE_KAKAO_REST_API_KEY}&redirect_uri=${VITE_KAKAO_REDIRECT_URI}&response_type=code`;

  // 팝업 창 열기
  loginBox = window.open(kakaoAuthUrl, "_blank", "width=800, height=600");

  if (!loginBox) {
    alert("팝업 차단을 해제해주세요.");
  }

  // 메시지 리스너 추가
  window.addEventListener("message", handleMessage);
};

// 컴포넌트 언마운트 시 메시지 리스너 제거
onBeforeUnmount(() => {
  window.removeEventListener("message", handleMessage);
});
</script>

<template>
  <div class="kakao-login-container">
    <button @click="kakaoLogin" class="kakao-login-button">
      <img
        src="/src/assets/icons/kakao/kakao_login_medium_narrow.png"
        alt="카카오 로그인"
      />
    </button>
  </div>
</template>

<style scoped>
.kakao-login-button {
  background-color: #fee500;
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
