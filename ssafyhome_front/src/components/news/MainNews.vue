<template>
  <div>
    <h1 class="page-title">부동산 뉴스</h1>
    <div class="news-container">
      <div class="news-card" v-for="news in filteredNews" :key="news.link">
        <div class="news-image-wrapper">
          <img
            :src="getRandomNewsImage()"
            alt="뉴스 이미지"
            class="news-image"
          />
        </div>
        <div class="news-content">
          <a :href="news.link" target="_blank" class="news-title">{{ removeHTMLTags(news.title) }}</a>
          <p class="news-description">{{ removeHTMLTags(news.description) || '설명이 없습니다.' }}</p>
        </div>
      </div>
    </div>
  </div>
 </template>
 
 <script setup>
 import { ref, computed, onMounted } from 'vue'
 import axios from 'axios'
 
 const newsList = ref([]);
 
 const filteredNews = computed(() => {
  return newsList.value.filter(news => 
    removeHTMLTags(news.title).includes('부동산')
  );
 });
 
 function removeHTMLTags(str) {
  if (!str) return '';
  return str.replace(/<[^>]*>/g, '')
            .replace(/&quot;/g, '"')
            .replace(/&amp;/g, '&')
            .replace(/&lt;/g, '<')
            .replace(/&gt;/g, '>')
            .replace(/&nbsp;/g, ' ');
 }
 
 function getRandomNewsImage() {
  const randomNum = Math.floor(Math.random() * 20) + 1;
  return `/src/assets/news/news${randomNum}.jpg`;
 }
 
 onMounted(async () => {
  try {
    const response = await axios.get('http://localhost/home/api/news', {
      params: { query: '부동산' }
    });
    newsList.value = response.data.items;
  } catch (error) {
    console.error('부동산 뉴스를 가져오는 중 오류 발생:', error);
  }
 });
 </script>
 
 <style scoped>
 .page-title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin: 2rem 0;
  color: #333;
 }
 
 .news-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
  padding: 1rem;
  max-width: 1400px;
  margin: 0 auto;
 }
 
 .news-card {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease-in-out;
  height: 100%;
  display: flex;
  flex-direction: column;
 }
 
 .news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
 }
 
 .news-image-wrapper {
  width: 100%;
  height: 200px;
  background-color: #f8f9fa;
  position: relative;
  overflow: hidden;
 }
 
 .news-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
 }
 
 .news-card:hover .news-image {
  transform: scale(1.05);
 }
 
 .news-content {
  padding: 1.5rem;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
 }
 
 .news-title {
  font-size: 1.2rem;
  font-weight: bold;
  color: #1a1a1a;
  text-decoration: none;
  line-height: 1.4;
  margin-bottom: 0.5rem;
  display: -webkit-box;
  overflow: hidden;
 }
 
 .news-title:hover {
  color: #0066cc;
 }
 
 .news-description {
  font-size: 0.95rem;
  color: #666;
  line-height: 1.5;
  display: -webkit-box;
  overflow: hidden;
  margin: 0;
 }
 
 @media (max-width: 768px) {
  .news-container {
    grid-template-columns: 1fr;
    padding: 1rem;
  }
 
  .page-title {
    font-size: 1.5rem;
    margin: 1.5rem 0;
  }
 }
 
 @media (min-width: 769px) and (max-width: 1200px) {
  .news-container {
    grid-template-columns: repeat(2, 1fr);
  }
 }
 </style>