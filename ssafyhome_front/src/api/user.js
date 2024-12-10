import { localAxios, userAxios } from "@/util/http-commons";
const local = localAxios();
const userApi = userAxios();

async function userConfirm(param, success, fail) {
  await local.post(`/user/login`, param).then(success).catch(fail);
}

async function findById(userNo, accessTokentoken, success, fail) {
  local.defaults.headers["Authorization"] = accessTokentoken;
  await local.get(`/user/info/${userNo}`).then(success).catch(fail);
}

async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] =
    sessionStorage.getItem("refreshToken");
  await local.post(`/user/refresh`, user).then(success).catch(fail);
}

async function registerUser(user, success, fail) {
  await local.post("/user/register", user).then(success).catch(fail);
}

// 백엔드로 code 보내서 카카오 로그인 처리하기
function logout(userNo, success, fail) {
  userApi
    .get(`/logout`, {
      params: {
        userNo,
      },
    })
    .then(success)
    .catch(fail);
}

async function updateUser(userData, accessToken, success, fail) {
  local.defaults.headers["Authorization"] = accessToken;
  await local.put(`/user/update`, userData).then(success).catch(fail);
}

async function deleteUser(userNo, accessToken, success, fail) {
  local.defaults.headers["Authorization"] = accessToken;
  console.log(userNo);
  console.log(accessToken);
  await local.delete(`/user/withdraw/${userNo}`).then(success).catch(fail);
}

async function userRestore(userNo, success, fail) {
  await local.post(`/user/restore`, { userNo }).then(success).catch(fail);
}

export {
  userConfirm,
  findById,
  tokenRegeneration,
  logout,
  registerUser,
  updateUser,
  deleteUser,
  userRestore,
};
