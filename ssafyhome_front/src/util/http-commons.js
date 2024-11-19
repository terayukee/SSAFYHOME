import axios from "axios";

const { VITE_VUE_API_URL, VITE_ELECTRIC_CHARGING_STATION_URL } = import.meta.env;

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

// station vue api axios instance
function stationAxios() {
  const instance = axios.create({
    baseURL: VITE_ELECTRIC_CHARGING_STATION_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 아파트 정보 가져오기
function houseInfoAxios() {
  const instance = axios.create({
    baseURL: "http://localhost:8080/api/houseinfo", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// 거래 정보 가져오기
function houseDealAxios() {
  const instance = axios.create({
    baseURL: "http://localhost:8080/api/housedeal", // Spring Boot API의 baseURL
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export { localAxios, stationAxios, houseInfoAxios, houseDealAxios };
