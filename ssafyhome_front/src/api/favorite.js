import { favoriteAxios } from "@/util/http-commons";

const favoriteApi = favoriteAxios();

// 관심 단지(부동산) 가져오기
function fetchUserFavorites(param, success, fail) {
  favoriteApi
    .get("/get-favorite-house", { params: param })
    .then(success)
    .catch(fail);
}

// 관심 매물(RealEstate) 가져오기
function fetchUserFavoriteRealEstates(param, success, fail) {
  favoriteApi
    .get("/get-favorite-realestate", { params: param })
    .then(success)
    .catch(fail);
}

// 관심 단지 토글
function toggleFavorite(param, success, fail) {
  favoriteApi.post("/toggle-house", param).then(success).catch(fail);
}

// 관심 매물 토글
function toggleFavoriteRealEstate(param, success, fail) {
  favoriteApi.post("/toggle-realestate", param).then(success).catch(fail);
}

export {
  fetchUserFavorites,
  fetchUserFavoriteRealEstates,
  toggleFavorite,
  toggleFavoriteRealEstate,
};
