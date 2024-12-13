import { userAxios } from "@/util/http-commons";

const userApi = userAxios();

// 로그인 요청
async function userConfirm(param, success, fail) {
  try {
    const response = await userApi.post(`/login`, param);
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 사용자 정보 조회
async function findById(userNo, accessToken, success, fail) {
  try {
    const response = await userApi.get(`/info/${userNo}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 토큰 재발급
async function tokenRegeneration(user, success, fail) {
  try {
    const response = await userApi.post(`/refresh`, user, {
      headers: {
        refreshToken: sessionStorage.getItem("refreshToken"),
      },
    });
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 회원가입 요청
async function registerUser(user, success, fail) {
  try {
    const response = await userApi.post(`/register`, user);
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 로그아웃 요청
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

// 사용자 정보 수정
async function updateUser(userData, accessToken, success, fail) {
  try {
    const response = await userApi.put(`/update`, userData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 사용자 계정 삭제
async function deleteUser(userNo, accessToken, success, fail) {
  try {
    const response = await userApi.delete(`/withdraw/${userNo}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    success(response);
  } catch (error) {
    fail(error);
  }
}

// 사용자 계정 복구
async function userRestore(userNo, success, fail) {
  try {
    const response = await userApi.post(`/restore`, { userNo });
    success(response);
  } catch (error) {
    fail(error);
  }
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
