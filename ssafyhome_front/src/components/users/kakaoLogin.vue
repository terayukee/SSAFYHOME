<script setup>
const KAKAO_CLIENT_ID =`4736eef2397a78d68348b4f3fdbea4ca`
const REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback'

const loginWithKakao = () => {
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`
    window.location.href = kakaoURL
}
</script>

<template>
    <div class="kakao-login-container">
        <button @click="loginWithKakao" class="kakao-login-btn">
            카카오 계정으로 로그인
        </button>
    </div>
</template>

<style scoped>
.kakao-login-container {
    display: flex;
    justify-content: center;
    padding: 20px;
}

.kakao-login-btn {
    background-color: #FEE500;
    color: #000000;
    border: none;
    padding: 12px 24px;
    border-radius: 6px;
    cursor: pointer;
    font-weight: bold;
    display: flex;
    align-items: center;
    gap: 8px;
}

.kakao-login-btn:hover {
    background-color: #FDD835;
}
</style>