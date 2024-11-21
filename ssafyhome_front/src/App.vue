<template>
  <div id="app">
    <Header />
    <router-view />
    <Footer />
  </div>
</template>

<script setup>
import Header from "./components/common/Header.vue";
import Footer from "./components/common/Footer.vue";
</script>

<style>
/* 공통 스타일 작성 */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}
</style>
