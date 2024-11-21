<template>
    <div class="kakao-login-container">
      <button 
        @click="kakaoLogin" 
        class="kakao-login-button"
      >
        <img src="/src/assets/icons/kakao_login_medium_wide.png" alt="카카오 로그인">
        카카오로 로그인
      </button>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  
//   const KAKAO_REST_API_KEY = import.meta.env.VITE_KAKAO_REST_API_KEY
//   const KAKAO_REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI
  const KAKAO_REST_API_KEY =`4736eef2397a78d68348b4f3fdbea4ca`
const KAKAO_REDIRECT_URI = 'http://localhost/home/oauth/kakao/callback'
  const kakaoLogin = () => {
    const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`
    window.location.href = kakaoAuthUrl
  }
  
  // 리다이렉트 후 인증 코드 처리
  const handleKakaoCallback = async () => {
    const urlParams = new URLSearchParams(window.location.search)
    const code = urlParams.get('code')
  
    if (code) {
      try {
        const response = await axios.post('/api/kakao/login', { code })
        // 로그인 성공 처리 (토큰 저장, 라우팅 등)
        localStorage.setItem('token', response.data.token)
        // 메인 페이지로 리다이렉트
        router.push('/')
      } catch (error) {
        console.error('카카오 로그인 실패:', error)
      }
    }
  }
  
  // 컴포넌트 마운트 시 콜백 처리
//   onMounted(() => {
//     if (window.location.pathname === '/kakao/callback') {
//       handleKakaoCallback()
//     }
//   })
  </script>
  
  <style scoped>
  .kakao-login-button {
    background-color: #FEE500;
    color: black;
    border: none;
    padding: 10px 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    border-radius: 5px;
  }
  </style>