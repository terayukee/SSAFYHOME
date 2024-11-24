import { localAxios } from "@/util/http-commons";
const local = localAxios();

async function userConfirm(param, success, fail) {
  await local.post(`/user/login`, param).then(success).catch(fail);
}

async function findById(userNo , accessTokentoken, success, fail) {
  local.defaults.headers["Authorization"] = accessTokentoken;
  await local.get(`/user/info/${userNo}`).then(success).catch(fail);
}

async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] = sessionStorage.getItem("refreshToken"); //axios header에 refresh-token 셋팅
  await local.post(`/user/refresh`, user).then(success).catch(fail);
}

async function registerUser(user, success, fail){
  await local.post('/user/register',user).then(success).catch(fail)
}

async function logout(userNo, success, fail) {
  await local.get(`/user/logout/${userNo}`).then(success).catch(fail);
}

async function updateUser(userData, accessToken, success, fail) {
  local.defaults.headers["Authorization"] = accessToken;
  await local.put(`/user/update`, userData).then(success).catch(fail);
}

async function deleteUser(userNo, accessToken, success, fail) {
  local.defaults.headers["Authorization"] = accessToken;
  console.log(userNo)
  console.log(accessToken)
  const response = await local.delete(`/user/withdraw/${userNo}`).then(success).catch(fail);
}

async function userRestore(userNo, success, fail) {
  await local.post(`/user/restore`, { userNo }).then(success).catch(fail);
}

export { userConfirm, findById, tokenRegeneration, logout , registerUser, updateUser, deleteUser , userRestore};
