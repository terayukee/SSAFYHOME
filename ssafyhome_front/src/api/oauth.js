import { oauthAxios } from "@/util/http-commons";

// API 호출 함수
const oauth = oauthAxios();

// 환경 변수
const {
  VITE_KAKAO_REST_API_KEY,
  VITE_KAKAO_REDIRECT_URI,
  VITE_KAKAO_CLIENT_SECRET,
} = import.meta.env;

// 백엔드로 code 보내서 카카오 로그인 처리하기
function kakaoLogin(code, success, fail) {
  oauth
    .get(`/kakao/login`, {
      params: {
        code,
        clientId: VITE_KAKAO_REST_API_KEY,
        redirectUri: VITE_KAKAO_REDIRECT_URI,
        clientSecret: VITE_KAKAO_CLIENT_SECRET,
      },
    })
    .then(success)
    .catch(fail);
}

export { kakaoLogin };
