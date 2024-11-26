import { houseCommentAxios } from "@/util/http-commons";

const houseCommentApi = houseCommentAxios();

function getCommentList(param, success, fail) {
  console.log("Sending params to API:", param);

  houseCommentApi.post("/list", param).then(success).catch(fail);
}

function addComment(param, success, fail) {
  houseCommentApi.post("/add", param).then(success).catch(fail);
}

export { getCommentList, addComment };
