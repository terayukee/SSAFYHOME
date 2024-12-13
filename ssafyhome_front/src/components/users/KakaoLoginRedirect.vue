<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { kakaoLogin } from "@/api/oauth";

// Vue Router
const router = useRouter();

const handleKakaoLogin = async () => {
  // 현재 URL에서 code 파라미터 가져오기
  const query = router.currentRoute.value.query;
  const code = query.code;

  if (!code) {
    console.error("카카오 로그인 코드가 없습니다.");
    alert("로그인 실패: 카카오 인증 코드가 없습니다.");
    window.close(); // 팝업 창 닫기
    return;
  }

  // 카카오 로그인 처리
  kakaoLogin(
    code,
    (res) => {
      console.log("카카오 로그인 성공:", res);

      // 사용자 정보를 부모 창으로 전달
      const jwtTokens = {
        "access-token": res.data["access-token"],
        "refresh-token": res.data["refresh-token"],
      };

      window.opener.postMessage(jwtTokens, "*"); // 부모 창으로 메시지 전달
      window.close(); // 팝업 창 닫기
    },
    (err) => {
      console.error("카카오 로그인 실패:", err);

      // 에러 메시지 전달
      window.opener.postMessage({ error: "카카오 로그인 실패" }, "*");
      window.close(); // 팝업 창 닫기
    }
  );
};

onMounted(() => {
  handleKakaoLogin();
});
</script>

<template>
  <div>
    <h2>카카오 로그인 처리 중...</h2>
  </div>
</template>
