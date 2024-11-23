import { realestateAxios } from "@/util/http-commons";

// API 호출 함수
const realestateApi = realestateAxios();

function fetchRealEstateInfos(params, success, fail) {
  realestateApi
    .get("/listby-space-dealtype", {
      params,
    })
    .then(success)
    .catch(fail);
}

export { fetchRealEstateInfos };
