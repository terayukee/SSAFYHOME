<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const newsList = ref([]);
const analysisStatus = ref(new Map());
const newsImages = ref(new Map());

const filteredNews = computed(() => {
  return newsList.value
    .filter(news => removeHTMLTags(news.title).includes('부동산'));
});

function getAnalysisClass(newsId) {
  const status = analysisStatus.value.get(newsId);
  return status?.result?.includes('호재') ? 'positive' : 'negative';
}

function getAnalysisResult(newsId) {
  return analysisStatus.value.get(newsId)?.result || null;
}

function isAnalyzing(newsId) {
  return analysisStatus.value.get(newsId)?.analyzing || false;
}

function getRandomNewsImage(newsId) {
  if (newsImages.value.has(newsId)) {
    return newsImages.value.get(newsId);
  }
  
  const randomNum = Math.floor(Math.random() * 20) + 1;
  const imagePath = `/src/assets/news/news${randomNum}.jpg`;
  newsImages.value.set(newsId, imagePath);
  return imagePath;
}

async function analyzeNews(news) {
  const newsId = news.link;
  
  if (isAnalyzing(newsId)) return;
  
  analysisStatus.value.set(newsId, { analyzing: true, result: null });
  
  try {
    const encodedTitle = encodeURIComponent(removeHTMLTags(news.title));
    const response = await axios.get(`http://localhost/home/news/analysis/${encodedTitle}`);
    analysisStatus.value.set(newsId, { 
      analyzing: false, 
      result: response.data.trim()
    });
  } catch (error) {
    console.error('뉴스 분석 중 오류 발생:', error);
    analysisStatus.value.set(newsId, { 
      analyzing: false, 
      result: '분석 실패'
    });
  }
}

function removeHTMLTags(str) {
  if (!str) return '';
  return str.replace(/<[^>]*>/g, '')
            .replace(/&quot;/g, '"')
            .replace(/&amp;/g, '&')
            .replace(/&lt;/g, '<')
            .replace(/&gt;/g, '>')
            .replace(/&nbsp;/g, ' ');
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

<template>
  <div>
    <h1 class="page-title">부동산 뉴스</h1>
    <div class="news-container">
      <div class="news-card" v-for="news in filteredNews" :key="news.link">
        <div class="news-image-wrapper">
          <img
            :src="getRandomNewsImage(news.link)"
            alt="뉴스 이미지"
            class="news-image"
          />
        </div>
        <div class="news-content">
          <a :href="news.link" target="_blank" class="news-title">
            {{ removeHTMLTags(news.title) }}
          </a>
          <p class="news-description">
            {{ removeHTMLTags(news.description) || '설명이 없습니다.' }}
          </p>
          <div class="analysis-section">
            <button 
              @click="analyzeNews(news)" 
              class="analysis-button"
              :disabled="isAnalyzing(news.link)"
              :class="{ 'analyzing': isAnalyzing(news.link) }"
            >
              {{ isAnalyzing(news.link) ? '분석중...' : '호재/악재 분석' }}
            </button>
            <div 
              v-if="getAnalysisResult(news.link)" 
              :class="['analysis-result', getAnalysisClass(news.link)]"
            >
              {{ getAnalysisResult(news.link) }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
 
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


 .analysis-section {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.analysis-button {
  padding: 0.5rem 1rem;
  background-color: #0066cc;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.analysis-button:hover:not(:disabled) {
  background-color: #0052a3;
}

.analysis-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.analysis-button.analyzing {
  position: relative;
  padding-right: 2.5rem;
}

.analysis-button.analyzing::after {
  content: '';
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  width: 1rem;
  height: 1rem;
  border: 2px solid #ffffff;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.analysis-result {
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-weight: bold;
  font-size: 0.9rem;
}

.analysis-result.positive {
  background-color: #e6f4ea;
  color: #1e8e3e;
}

.analysis-result.negative {
  background-color: #fce8e6;
  color: #d93025;
}

@keyframes spin {
  to { transform: translateY(-50%) rotate(360deg); }
}
 </style>