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

export {
  localAxios,
  houseInfoAxios,
  houseDealAxios,
  formAxios,
  houseSearchAxios,
  articleAxios,
};
