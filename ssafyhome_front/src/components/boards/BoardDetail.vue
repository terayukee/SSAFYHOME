<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { detailArticle } from '@/api/board';

const route = useRoute();
const router = useRouter();

// 상태 관리
const currentPage = ref(route.params.page || 1);
const articleno = ref(route.params.articleno);
const isLoading = ref(false);
const errorMessage = ref('');

// 게시글 데이터
const article = ref({
  subject: '',
  content: '',
  userNickname: '',
  registerTime: new Date(),
  attachments: []
});

// 날짜 포맷 함수
const formatDate = (date) => {
  try {
    return new Date(date).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  } catch (error) {
    console.error('날짜 변환 중 오류:', error);
    return '날짜 정보 없음';
  }
};

// 게시글 불러오기
const getArticle = () => {
  isLoading.value = true;
  errorMessage.value = '';
  
  detailArticle(
    articleno.value,
    ({ data }) => {
      article.value = {
        ...data,
        attachments: data.fileInfos || []
      };
      isLoading.value = false;
    },
    (error) => {
      console.error('게시글 로딩 중 오류:', error);
      errorMessage.value = '게시글을 불러오는 중 오류가 발생했습니다.';
      isLoading.value = false;
    }
  );
};

// 파일 다운로드
const downloadFile = (filePath) => {
  const link = document.createElement('a');
  link.href = `http://localhost/home/board/file/download?filePath=${encodeURIComponent(filePath)}`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// 목록으로 돌아가기
const goToList = () => {
  router.push({ 
    name: 'board-list-page', 
    params: { page: currentPage.value }
  });
};

// 수정 페이지로 이동 (상태로 데이터 전달)
const goToUpdate = () => {
  router.push({
    name: 'board-edit',
    state: { article: article.value } // 게시글 데이터 전달
  });
};


onMounted(getArticle);
</script>

<template>
  <div class="post-container">
    <!-- 로딩 상태 -->
    <div v-if="isLoading" class="loading-spinner">
      로딩 중...
    </div>

    <!-- 에러 메시지 -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <!-- 메인 컨텐츠 -->
    <template v-if="!isLoading && !errorMessage">
      <!-- 게시글 헤더 -->
      <div class="post-header">
        <h1 class="post-title">{{ article.subject }}</h1>
        <div class="post-meta">
          <span>작성자: {{ article.userNickname }}</span>
          <span>작성일: {{ formatDate(article.registerTime) }}</span>
        </div>
      </div>

      <!-- 게시글 본문 -->
      <div class="post-content" v-html="article.content"></div>

      <!-- 첨부파일 목록 -->
      <div v-if="article.attachments?.length > 0" class="attachments">
        <h3>첨부파일</h3>
        <ul>
          <li v-for="(file, index) in article.attachments" 
              :key="index"
              class="attachment-item">
            <a
              @click.prevent="downloadFile(file.filePath)"
              href="#"
              class="download-link">
              {{ file.fileName }}
              <span class="file-size">({{ file.fileSize }} KB)</span>
            </a>
          </li>
        </ul>
      </div>

      <!-- 버튼 영역 -->
      <div class="button-area">
        <button @click="goToList" class="list-button">
          목록으로
        </button>
        <button @click="goToUpdate" class="edit-button">
          수정
        </button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.post-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff;
}

.loading-spinner {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.error-message {
  color: #dc3545;
  padding: 1rem;
  border: 1px solid #dc3545;
  border-radius: 4px;
  margin-bottom: 1rem;
  background-color: #fff8f8;
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
  margin-top: 20px;
}

.attachments h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
}

.attachment-item {
  margin-bottom: 10px;
}

.download-link {
  color: #007bff;
  text-decoration: none;
  cursor: pointer;
}

.download-link:hover {
  text-decoration: underline;
}

.file-size {
  color: #666;
  font-size: 0.9em;
  margin-left: 8px;
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

.edit-button {
  padding: 8px 20px;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  margin-left: 10px;
}

.edit-button:hover {
  background-color: #0056b3;
}
</style>
