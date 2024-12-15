import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";

import {
  userConfirm,
  findById,
  tokenRegeneration,
  logout,
  registerUser,
  updateUser,
  deleteUser,
  userRestore,
} from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useUserStore = defineStore(
  "userStore",
  () => {
    const router = useRouter();

    const isLogin = ref(false);
    const isLoginError = ref(false);
    const isValidToken = ref(false);
    const accessToken = ref(null);
    const refreshToken = ref(null);
    const userInfo = ref(null);

    const userLogin = async (loginUser) => {
      await userConfirm(
        loginUser,
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            console.log("로그인 성공!!!!");

            // access-token, refresh-token
            let { data } = response;
            setToken(data);

            // accessToken 디코딩 후 상태 갱신
            const userData = parseAccessToken(accessToken.value);
            setUserInfo(userData, data);
          }
        },
        (error) => {
          console.log("로그인 실패!!!!");
          isLogin.value = false;
          isLoginError.value = true;
          isValidToken.value = false;
        }
      );
    };

    const userRegister = async (userInfo) => {
      try {
        await registerUser(
          userInfo,
          (response) => {
            if (response.status === httpStatusCode.CREATE) {
              console.log("가입성공!");
            }
          },
          (error) => {
            console.log("가입실패");
            console.log(error);
            throw error;
          }
        );
      } catch (error) {
        throw error;
      }
    };

    // 계정 복구 함수 추가
    const restoreUser = async (userNo) => {
      try {
        await userRestore(
          userNo,
          (response) => {
            if (response.status === httpStatusCode.OK) {
              console.log("계정복구 성공!");
            } else {
              throw new Error("계정 복구 실패");
            }
          },
          (error) => {
            console.log("계정복구 실패");
            console.log(error);
            throw error;
          }
        );
      } catch (error) {
        throw error;
      }
    };

    const getUserInfo = async (forceReload = false) => {
      // forceReload가 true일 때만 서버 호출
      if (!forceReload) {
        console.log("accessToken 기반으로 사용자 정보 반환");
        return userInfo.value;
      }

      console.log("서버에서 사용자 정보 가져오기");
      await findById(
        userInfo.value.userNo,
        accessToken.value,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            userInfo.value = response.data.userInfo;
          }
        },
        (error) => {
          console.error("유저 정보 요청 실패:", error);
        }
      );
    };

    const parseAccessToken = (token) => {
      const decoded = jwtDecode(token);
      console.log("디코드된 accessToken:", decoded);

      // 필요한 정보만 반환
      return {
        userNo: decoded.userNo,
        nickname: decoded.nickname,
        email: decoded.email,
        role: decoded.role,
      };
    };

    const tokenRegenerate = async () => {
      await tokenRegeneration(
        JSON.stringify(userInfo.value),
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            let accessToken = response.data["access-token"];
            localStorage.setItem("accessToken", accessToken);
            isValidToken.value = true;
          }
        },
        async (error) => {
          if (error.response.status === httpStatusCode.UNAUTHORIZED) {
            await logout(
              userInfo.value.userNo,
              (response) => {
                if (response.status === httpStatusCode.OK) {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                isLogin.value = false;
                userInfo.value = null;
                isValidToken.value = false;
                router.push({ name: "user-login" });
              },
              (error) => {
                console.error(error);
                isLogin.value = false;
                userInfo.value = null;
              }
            );
          }
        }
      );
    };

    const userLogout = async () => {
      console.log("로그아웃 아이디 : " + userInfo.value?.userNo);
      logout(
        userInfo.value.userNo,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            setToken(null); // jwt 토큰 초기화
            setUserInfo(null); // userInfo pinia 정보 초기화
            console.log("로그아웃");
          } else {
            console.error("유저 정보 없음!!!!");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    };

    const updateUserInfo = async (updatedData) => {
      await updateUser(
        updatedData,
        accessToken.value, // 토큰 추가
        (response) => {
          if (response.status === httpStatusCode.OK) {
            console.log("유저 정보 수정 성공");
            userInfo.value = response.data.userInfo;
          } else {
            console.log("유저 정보 수정 실패!!!!");
          }
        },
        (error) => {
          console.error("유저 정보 수정 에러:", error);
          throw error; // 에러를 상위로 전파하여 컴포넌트에서 처리할 수 있도록 함
        }
      );
    };

    const withdrawUser = async () => {
      try {
        await deleteUser(
          userInfo.value.userNo,
          accessToken.value,
          (response) => {
            if (response.status === httpStatusCode.OK) {
              // 로그아웃 처리
              isLogin.value = false;
              userInfo.value = null;
              isValidToken.value = false;
              accessToken.value = "";
              refreshToken.value = "";
            }
          },
          (error) => {
            console.error("회원 탈퇴 실패:", error);
            throw error;
          }
        );
      } catch (error) {
        console.error("회원 탈퇴 처리 중 오류 발생:", error);
        throw error;
      }
    };

    const setUserInfo = (userData) => {
      if (userData) {
        isLogin.value = true;
        isValidToken.value = true;
        userInfo.value = userData;
      } else {
        // 데이터가 없을 경우 기본 상태로 초기화
        isLogin.value = false;
        isValidToken.value = false;
        userInfo.value = null;
      }
    };

    const setToken = (jwtTokens) => {
      if (jwtTokens) {
        accessToken.value = jwtTokens["access-token"];
        refreshToken.value = jwtTokens["refresh-token"];
        console.log("토큰이 성공적으로 저장되었습니다.");
      } else {
        accessToken.value = null;
        refreshToken.value = null;
        console.log("토큰 정보 삭제 ");
      }
    };

    return {
      isLogin,
      isLoginError,
      userInfo,
      isValidToken,
      userLogin,
      getUserInfo,
      tokenRegenerate,
      userLogout,
      userRegister,
      updateUserInfo,
      withdrawUser,
      restoreUser,
      accessToken,
      refreshToken,
      setUserInfo,
      parseAccessToken,
      setToken,
    };
  },
  { persist: true }
);
