<!-- KakaoLogin.vue -->
<template>
    <div>
      <button @click="loginWithKakao">카카오로 로그인</button>
    </div>
  </template>
  
  <script setup>
  import { defineProps, emit } from 'vue';
  
  // 카카오 로그인 함수
  const loginWithKakao = () => {
    if (!window.Kakao.isInitialized()) {
      window.Kakao.init('YOUR_APP_KEY');  // 카카오 앱의 JavaScript 키 입력
    }
  
    window.Kakao.Auth.login({
      success: (authObj) => {
        // 로그인 성공 시, access_token을 부모 컴포넌트로 전달
        emit('kakao-login-success', authObj.access_token);
      },
      fail: (err) => {
        console.error('카카오 로그인 실패', err);
        emit('kakao-login-fail', err);
      }
    });
  };
  </script>
  