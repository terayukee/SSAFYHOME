import { authAxios } from "@/util/http-commons";

// API 호출 함수
const auth = authAxios();

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

export { validateToken };
