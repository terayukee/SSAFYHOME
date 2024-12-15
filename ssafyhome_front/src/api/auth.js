import { authAxios } from "@/util/http-commons";
import { useRouter } from "vue-router";

// API 호출 함수
const auth = authAxios();

// 로그인 요청
async function login(param, success, fail) {
  try {
    const response = await auth.post(`/login`, param);
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 로그아웃 요청
function logout(userNo, success, fail) {
  auth
    .get(`/logout`, {
      params: {
        userNo,
      },
    })
    .then(success)
    .catch(fail);
}

// 토큰 재발급
async function tokenRegeneration(success, fail) {
  const router = useRouter();

  try {
    // 로컬스토리지에서 userStore 가져오기
    const userStore = JSON.parse(localStorage.getItem("userStore"));
    if (!userStore || !userStore.refreshToken) {
      throw new Error("리프레시 토큰이 없거나 유효하지 않습니다.");
    }

    // API 요청: 새 액세스 토큰 발급
    const response = await auth.post("/auth/refresh", userStore.userInfo, {
      headers: {
        refreshToken: userStore.refreshToken, // 헤더에 리프레시 토큰 추가
      },
    });

    // 성공 처리
    if (response.status === 201) {
      console.log("액세스 토큰 재발급 성공:", response.data);

      // 로컬스토리지 업데이트
      const updatedUserStore = {
        ...userStore,
        accessToken: response.data["access-token"], // 새 액세스 토큰 저장
      };
      localStorage.setItem("userStore", JSON.stringify(updatedUserStore));

      if (success) success(response); // 성공 콜백 호출
    } else {
      throw new Error("예상치 못한 응답 상태 코드:", response.status);
    }
  } catch (error) {
    console.error("토큰 재발급 실패:", error.message || error.response?.data);

    // 리프레시 토큰 만료 시 처리
    if (error.response?.status === 401) {
      alert("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.");
      localStorage.removeItem("userStore"); // 저장된 사용자 정보 제거
      router.push({ name: "user-login" }); // 로그인 페이지로 리다이렉트
    }

    // 실패 콜백 호출
    if (fail) fail(error);
  }
}

// 백엔드로 code 보내서 카카오 로그인 처리하기
function validateToken(accessToken, success, fail) {
  console.log("auth.js | accessToken : ", accessToken);
  auth
    .get(`/validate`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
}

export { login, logout, tokenRegeneration, validateToken };
