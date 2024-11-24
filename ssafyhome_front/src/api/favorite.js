import { favoriteAxios } from "@/util/http-commons";

const favoriteApi = favoriteAxios();

function fetchUserFavorites(param, success, fail) {
  favoriteApi
    .get("/get-favorites-by-user", { params: param })
    .then(success)
    .catch(fail);
}

function toggleFavorite(param, success, fail) {
  favoriteApi.post("/toggle", param).then(success).catch(fail);
}

export { fetchUserFavorites, toggleFavorite };
