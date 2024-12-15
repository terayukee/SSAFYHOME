import { userAxios } from "@/util/http-commons";

const userApi = userAxios();

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

// 회원가입 요청
async function registerUser(user, success, fail) {
  try {
    const response = await userApi.post(`/register`, user);
    success(response);
  } catch (error) {
    fail(error);
  }
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

export { findById, registerUser, updateUser, deleteUser, userRestore };
