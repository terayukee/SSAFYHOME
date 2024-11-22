import { ref, watchEffect } from "vue"
import { useRouter } from "vue-router"
import { defineStore } from "pinia"
import { jwtDecode } from "jwt-decode"

import { userConfirm, findById, tokenRegeneration, logout , registerUser } from "@/api/user"
import { httpStatusCode } from "@/util/http-status"

export const useUserStore = defineStore("memberStore", () => {
  const router = useRouter()
  const isLogin = ref(false)
  const isLoginError = ref(false)
  const isValidToken = ref(false)

  const userInfo = ref(JSON.parse(localStorage.getItem("userInfo")) || null)

  const userLogin = async (loginUser) => {
    await userConfirm(
      loginUser,
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          console.log("로그인 성공!!!!")
          let { data } = response
          let accessToken = data["access-token"]
          let refreshToken = data["refresh-token"]
          isLogin.value = true
          isLoginError.value = false
          isValidToken.value = true
          localStorage.setItem("accessToken", accessToken)
          localStorage.setItem("refreshToken", refreshToken)

          // 로그인 성공 시 userInfo를 로컬 스토리지에서 가져와 업데이트
          getUserInfo(accessToken)
        }
      },
      (error) => {
        console.log("로그인 실패!!!!")
        console.log(loginUser)
        isLogin.value = false
        isLoginError.value = true
        isValidToken.value = false
        console.error(error)
      }
    )
  }

  const userRegister = async (userInfo) => {
    await registerUser(
      userInfo,
      (response) => {
        if(response.status == httpStatusCode.CREATE){
          console.log("가입성공!")
        }
      },
      (error) =>{
        console.log("가입실패")
        console.log(userInfo)
      }
    )
  };


  const getUserInfo = async (token) => {
    let decodeToken = jwtDecode(token)
    console.log("디코드토큰", decodeToken.userNo)
    
    await findById(
      decodeToken.userNo,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = response.data.userInfo
          console.log(userInfo.value, "userInfo 업데이트")
        } else {
          console.log("유저 정보 없음!!!!")
        }
      },
      async (error) => {
        console.error(
          "g[토큰 만료되어 사용 불가능.] : ",
          error.response.status,
          error.response.statusText
        )
        isValidToken.value = false
        await tokenRegenerate()
      }
    )
  }


  const tokenRegenerate = async () => {
    await tokenRegeneration(
      JSON.stringify(userInfo.value),
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          let accessToken = response.data["access-token"]
          localStorage.setItem("accessToken", accessToken)
          isValidToken.value = true
        }
      },
      async (error) => {
        if (error.response.status === httpStatusCode.UNAUTHORIZED) {
          await logout(
            userInfo.value.userNo,
            (response) => {
              if (response.status === httpStatusCode.OK) {
                console.log("리프레시 토큰 제거 성공")
              } else {
                console.log("리프레시 토큰 제거 실패")
              }
              alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.")
              isLogin.value = false
              userInfo.value = null
              isValidToken.value = false
              router.push({ name: "user-login" })
            },
            (error) => {
              console.error(error)
              isLogin.value = false
              userInfo.value = null
            }
          )
        }
      }
    )
  }

  const userLogout = async () => {
    console.log("로그아웃 아이디 : " + userInfo.value?.userNo)
    await logout(
      userInfo.value.userNo,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          isLogin.value = false
          userInfo.value = null
          isValidToken.value = false
          localStorage.removeItem("accessToken")
          localStorage.removeItem("refreshToken")
          localStorage.removeItem("userInfo")
          console.log("로그아웃")
        } else {
          console.error("유저 정보 없음!!!!")
        }
      },
      (error) => {
        console.log(error)
      }
    )
  }

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
  }
}, 
{persist:true}
)
