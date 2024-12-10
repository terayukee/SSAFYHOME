import axios from "axios";

const { VITE_VUE_API_URL } = import.meta.env;

// local vue api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 소셜 로그인
function userAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/user",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 폼 데이터 가져오기
function formAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return instance;
}

// station vue api axios instance
// function stationAxios() {
//   const instance = axios.create({
//     baseURL: VITE_ELECTRIC_CHARGING_STATION_URL,
//     headers: {
//       "Content-Type": "application/json;charset=utf-8",
//     },
//   });
//   return instance;
// }

// 아파트 정보 가져오기
function houseInfoAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/houseinfo", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 거래 정보 가져오기
function houseDealAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/housedeal", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 검색 정보 가져오기
function houseSearchAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/search", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// article (뉴스, 가이드 등) 정보
function articleAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/article", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 부동산 정보
function realestateAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/realestate", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 관심(favorite) 정보
function favoriteAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/favorite", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 주택 별 댓글 정보
function houseCommentAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/api/housecomment", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 소셜 로그인
function oauthAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL + "/oauth",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export {
  localAxios,
  userAxios,
  houseInfoAxios,
  houseDealAxios,
  formAxios,
  houseSearchAxios,
  articleAxios,
  realestateAxios,
  favoriteAxios,
  houseCommentAxios,
  oauthAxios,
};
