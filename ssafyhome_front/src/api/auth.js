import { authAxios } from "@/util/http-commons";

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
async function tokenRegeneration(user, success, fail) {
  try {
    const response = await auth.post(`/refresh`, user, {
      headers: {
        refreshToken: sessionStorage.getItem("refreshToken"),
      },
    });
    success(response);
  } catch (error) {
    fail(error);
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
