<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from "pinia"
import { useRouter } from "vue-router"
import KakaoLogin from './KakaoLogin.vue';
const userStore = useUserStore();
const { isLogin, isLoginError } = storeToRefs(userStore)
const { userLogin, getUserInfo } = useUserStore();
const router = useRouter();

const loginUser = ref({
    email: "",
    userPassword: "",
})

const login = () => {
  userLogin(loginUser.value)
  let token = sessionStorage.getItem("accessToken")
  console.log(token)
  console.log(loginUser.value)
  console.log("isLogin: " + isLogin.value)
  if (isLogin.value) {
    getUserInfo(token)
    // changeMenuState()
    router.replace("/")
  }
}

const mvUserRegister = ()=>{
    router.push({name : `user-regist`});
}

const check = ()=>{
  console.log("ㅡㅡㅡ채집ㅡㅡㅡ")
  console.log(isLogin.value);
  console.log("ㅡㅡ채집완료ㅡㅡ")
}

</script>

<template>
    <div class="login-container">
        <button @click="check">췌크</button>
        <div class="login-card">
            <!-- 헤더 -->
            <div class="login-header">
                <h2>로그인</h2>
                <p class="signup-text">
                    계정이 없으신가요?
                    <a href="#" class="link">회원가입</a>
                </p>
            </div>

            <!-- 폼 -->
            <form class="login-form" @submit.prevent="login">
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input
                        id="email"
                        type="email"
                        v-model="loginUser.email"
                        required
                        placeholder="example@email.com"
                    />
                </div>

                <div class="form-group">
                    <label for="userPassword">비밀번호</label>
                    <input
                        id="userPassword"
                        type="password"
                        v-model="loginUser.userPassword"
                        required
                        placeholder="비밀번호를 입력하세요"
                    />
                </div>
                
                

                <div class="form-options">
                    <!-- <div class="remember-me">
                        <input
                            id="remember-me"
                            type="checkbox"
                            v-model="rememberMe"
                        />
                        <label for="remember-me">로그인 상태 유지</label>
                    </div> -->

                    <a href="#" class="forgot-password link">비밀번호를 잊으셨나요?</a>
                </div>

                <button type="submit" class="login-button">로그인</button>
                <KakaoLogin/>
                <button @click="mvUserRegister" class="login-button">회원가입</button>
            </form>
        </div>
    </div>
</template>

<style scoped>
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
    background-color: #f5f5f5;
}

.login-card {
    width: 100%;
    max-width: 400px;
    padding: 40px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.login-header {
    text-align: center;
    margin-bottom: 0px;
}

.login-header h2 {
    margin: 0;
    font-size: 24px;
    color: #333;
    margin-bottom: 10px;
}

.signup-text {
    font-size: 14px;
    color: #666;
    margin: 0;
}

.link {
    color: #2c6bed;
    text-decoration: none;
    font-weight: 500;
}

.link:hover {
    text-decoration: underline;
}

.login-form {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.form-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.form-group label {
    font-size: 14px;
    color: #333;
    font-weight: 500;
}

.form-group input {
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
    border-color: #2c6bed;
    box-shadow: 0 0 0 3px rgba(44, 107, 237, 0.1);
}

.form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
}

.remember-me {
    display: flex;
    align-items: center;
    gap: 8px;
}

.remember-me input[type="checkbox"] {
    width: 16px;
    height: 16px;
    accent-color: #2c6bed;
}

.forgot-password {
    font-size: 14px;
}

.login-button {
    background-color: #2c6bed;
    color: white;
    padding: 12px;
    border: none;   
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s;
}

.login-button:hover {
    background-color: #1a5ad1;
}

/* 반응형 디자인 */
/* @media (max-width: 480px) {
    .login-card {
        padding: 20px;
    }

    .form-options {
        flex-direction: column;
        gap: 12px;
        align-items: flex-start;
    }
} */
</style>