import { articleAxios } from "@/util/http-commons";

// API 호출 함수
const articleApi = articleAxios();

// 가이드 목록 가져오기
function getGuides(success, fail) {
  articleApi.get(`/guide/list`).then(success).catch(fail);
}

export { getGuides };
