<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { detailArticle } from '@/api/board';

const route = useRoute();
const router = useRouter();

const selectedFiles = ref([]);
const currentPage = ref(route.params.page || 1);
const articleno = ref(route.params.articleno)

onMounted(() => {
    getArticle(); 
});

// 게시글 데이터
const article = ref({
  title: '',
  content: '',
  author: '',
  date: new Date(),
  attachments: []
});


const getArticle = () => {
  detailArticle(
    articleno.value, // .value 추가
    ({ data }) => {
      article.value = data;
      console.log(article.value)
    },
    (error) => {
      console.log(error);
    }
  );
};

// 날짜 포맷 함수
const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 파일 크기 포맷 함수
// const formatFileSize = (bytes) => {
//   if (bytes === 0) return '0 Bytes';
//   const k = 1024;
//   const sizes = ['Bytes', 'KB', 'MB', 'GB'];
//   const i = Math.floor(Math.log(bytes) / Math.log(k));
//   return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
// };

// 목록으로 돌아가기
const goToList = () => {
    router.push({ name: "board-list-page", params: { page: currentPage.value } });
};


</script>

<template>
 <div class="post-container">
    <!-- 게시글 헤더 -->
    <div class="post-header">
      <h1 class="post-title">{{ article.title }}</h1>
      <div class="post-meta">
        <span>작성자: {{ article.userNickname }}</span>
        <span>작성일: {{ formatDate(article.registerTime) }}</span>
      </div>
    </div>

    <!-- 게시글 본문 -->
    <div class="post-content">
      {{ article.content }}
    </div>

    <!-- 첨부파일 섹션
    <div v-if="post.attachments && post.attachments.length > 0" class="attachments">
      <h2>첨부파일</h2>
      <ul>
        <li v-for="file in post.attachments" :key="file.id">
          <div class="file-item">
            <span class="file-icon">📎</span>
            <a :href="file.url" class="file-name">{{ file.name }}</a>
            <span class="file-size">({{ formatFileSize(file.size) }})</span>
          </div>
        </li>
      </ul>
    </div> -->

    <!-- 버튼 영역 -->
    <div class="button-area">
      <button @click="goToList" class="list-button">목록으로</button>
    </div>
  </div>
</template>

<style scoped>
.post-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff;
}

.post-header {
  border-bottom: 2px solid #eaeaea;
  padding-bottom: 20px;
  margin-bottom: 30px;
}

.post-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0 0 15px 0;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 14px;
}

.post-content {
  min-height: 200px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 40px;
  white-space: pre-wrap;
}

.attachments {
  border-top: 1px solid #eaeaea;
  padding-top: 20px;
  margin-top: 20px;
}

.attachments h2 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.attachments ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.file-icon {
  margin-right: 10px;
}

.file-name {
  color: #2b6cb0;
  text-decoration: none;
  margin-right: 8px;
}

.file-name:hover {
  text-decoration: underline;
}

.file-size {
  color: #666;
  font-size: 14px;
}

.button-area {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eaeaea;
}

.list-button {
  padding: 8px 20px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #333;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.list-button:hover {
  background-color: #e5e5e5;
}
</style>