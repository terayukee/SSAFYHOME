import { houseAxios } from "@/util/http-commons";

// API 호출 함수
const house = houseAxios();

function listHouses(param, success, fail) {
  house.get("/houses", { params: param }).then(success).catch(fail);
}

export { listHouses };