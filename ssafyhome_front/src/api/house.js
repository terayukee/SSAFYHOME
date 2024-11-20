import { houseInfoAxios, houseDealAxios } from "@/util/http-commons";

// API 호출 함수
const houseInfo = houseInfoAxios();
const houseDeal = houseDealAxios();

function listHouses(param, success, fail) {
  houseInfo.get("/list", { params: param }).then(success).catch(fail);
}

function listHousesInBounds(bounds, filters, success, fail) {
  const params = {
    ...bounds, // bounds 객체 내의 swLat, swLng, neLat, neLng
    dealCategory: filters.dealCategory, // 필터의 거래 유형
    roomSize: filters.roomSize, // 필터의 방 크기
    approvalDate: filters.approvalDate,
  };

  houseInfo.get("/bounds", { params }).then(success).catch(fail);
}

function getPopularDealInfo(aptSeq, success, fail) {
  houseInfo.get(`/popular`, { params: { aptSeq } }).then(success).catch(fail);
}

function getRecentDeals(aptSeq, success, fail) {
  houseDeal
    .get(`/recent`, { params: { aptSeq } })
    .then(success) // 요청 성공 시 success 콜백 호출
    .catch(fail); // 요청 실패 시 fail 콜백 호출
}

export { listHouses, listHousesInBounds, getPopularDealInfo, getRecentDeals };
