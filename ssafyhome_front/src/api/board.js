import { localAxios , formAxios } from "@/util/http-commons";

const local = localAxios();
const form = formAxios();
function listArticle(param, success, fail) {
  local.get(`/board`, { params: param }).then(success).catch(fail);
}

function detailArticle(boardNo, success, fail) {
  local.get(`/board/${boardNo}`).then(success).catch(fail);
}

function registArticle(formData, success, fail) {
  console.log("boardjs article", formData);
  form.post(`/board`, formData).then(success).catch(fail);
}

function getModifyArticle(boardNo, success, fail) {
  local.get(`/board/modify/${boardNo}`).then(success).catch(fail);
}

function modifyArticle(formData, success, fail) {
  form.put(`/board`, formData).then(success).catch(fail);
}

function deleteArticle(boardNo, success, fail) {
  local.delete(`/board/${boardNo}`).then(success).catch(fail);
}

export {
  listArticle,
  detailArticle,
  registArticle,
  getModifyArticle,
  modifyArticle,
  deleteArticle,
};
