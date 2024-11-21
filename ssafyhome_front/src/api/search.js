import { houseSearchAxios } from "@/util/http-commons";

// API 호출 함수
const houseSearch = houseSearchAxios();

// 지역 검색 결과 가져오기
function searchRegion(keyword, success, fail) {
  houseSearch
    .get(`/region`, {
      params: { keyword },
    })
    .then(success)
    .catch(fail);
}

// 주택 검색 결과 가져오기
function searchHouse(keyword, success, fail) {
  houseSearch
    .get(`/house`, {
      params: { keyword },
    })
    .then(success)
    .catch(fail);
}

// 지역 및 주택 검색 통합 결과 가져오기
function searchAll(keyword, success, fail) {
  houseSearch
    .get(`/all`, {
      params: { keyword },
    })
    .then(success)
    .catch(fail);
}

export { searchRegion, searchHouse, searchAll };
