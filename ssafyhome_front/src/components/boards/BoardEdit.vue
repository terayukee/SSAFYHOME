<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { detailArticle, modifyArticle } from '@/api/board';

const route = useRoute();
const router = useRouter();

const boardNo = ref(route.params.boardNo);
const currentPage = ref(route.query.page || 1);
const isLoading = ref(false);
const errorMessage = ref('');

// 수정할 게시글 데이터
const article = ref({
  subject: '',
  content: '',
  fileInfos: []
});

// 새로 추가할 파일들
const newFiles = ref([]);
// 삭제된 파일들의 정보를 추적
const deletedFiles = ref([]);

// 게시글 불러오기
const getArticle = () => {
  isLoading.value = true;
  errorMessage.value = '';
  
  detailArticle(
    boardNo.value,
    ({ data }) => {
      article.value = {
        ...data,
        fileInfos: data.fileInfos || []
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

// 파일 추가
const handleFileInput = (event) => {
  const files = event.target.files;
  newFiles.value = [...newFiles.value, ...Array.from(files)];
};

// 기존 첨부파일 삭제
const removeExistingFile = (index) => {
  // 삭제된 파일 정보 저장
  deletedFiles.value.push(article.value.fileInfos[index]);
  article.value.fileInfos = article.value.fileInfos.filter((_, i) => i !== index);
};

// 새로 추가된 파일 삭제
const removeNewFile = (index) => {
  newFiles.value = newFiles.value.filter((_, i) => i !== index);
};

const submitUpdate = () => {
  if (!article.value.subject.trim() || !article.value.content.trim()) {
    errorMessage.value = '제목과 내용을 모두 입력해주세요.';
    return;
  }

  isLoading.value = true;
  errorMessage.value = '';

  const formData = new FormData();
  // 게시글 정보
  formData.append('article', JSON.stringify({
    boardNo: boardNo.value,
    subject: article.value.subject,
    content: article.value.content,
    fileInfos: article.value.fileInfos, // 남아있는 첨부파일 정보
  }));
  
  // 새 파일들 추가
  newFiles.value.forEach(file => formData.append('files', file));
  
  // 삭제된 파일 정보 추가
  if (deletedFiles.value.length > 0) {
    formData.append('deletedFiles', JSON.stringify(deletedFiles.value));
  }

  modifyArticle(
    formData,
    () => {
      router.push({
        name: 'board-view',
        params: { boardNo: boardNo.value }
      });
    },
    (error) => {
      console.error('게시글 수정 중 오류:', error);
      errorMessage.value = '게시글 수정 중 오류가 발생했습니다.';
      isLoading.value = false;
    }
  );
};

const cancelEdit = () => {
  router.push({
    name: 'board-view',
    params: { boardNo: boardNo.value },
    query: { page: currentPage.value }
  });
};

onMounted(getArticle);
</script>

<template>
  <div class="edit-container">
    <!-- 로딩 상태 -->
    <div v-if="isLoading" class="loading-spinner">
      로딩 중...
    </div>

    <!-- 에러 메시지 -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <!-- 수정 폼 -->
    <template v-if="!isLoading">
      <div class="form-group">
        <label for="subject">제목</label>
        <input
          id="subject"
          v-model="article.subject"
          type="text"
          class="form-control"
          placeholder="제목을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea
          id="content"
          v-model="article.content"
          class="form-control"
          rows="10"
          placeholder="내용을 입력하세요"
        ></textarea>
      </div>

      <!-- 기존 첨부파일 목록 -->
      <div v-if="article.fileInfos.length > 0" class="attachments">
        <h3>기존 첨부파일</h3>
        <ul>
          <li v-for="(file, index) in article.fileInfos" 
              :key="index"
              class="attachment-item">
            {{ file.fileName }}
            <span class="file-size">({{ file.fileSize }} KB)</span>
            <button 
              @click="removeExistingFile(index)"
              class="remove-button">
              삭제
            </button>
          </li>
        </ul>
      </div>

      <!-- 새로운 첨부파일 -->
      <div class="form-group">
        <label for="files">새 첨부파일</label>
        <input
          type="file"
          id="files"
          multiple
          @change="handleFileInput"
          class="file-input"
        />
        
        <!-- 새로 추가된 파일 목록 -->
        <ul v-if="newFiles.length > 0" class="new-files-list">
          <li v-for="(file, index) in newFiles" 
              :key="index"
              class="attachment-item">
            {{ file.name }}
            <button 
              @click="removeNewFile(index)"
              class="remove-button">
              삭제
            </button>
          </li>
        </ul>
      </div>

      <!-- 버튼 영역 -->
      <div class="button-area">
        <button @click="cancelEdit" class="cancel-button">
          취소
        </button>
        <button @click="submitUpdate" class="submit-button">
          수정하기
        </button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.edit-container {
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-control {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea.form-control {
  resize: vertical;
  min-height: 200px;
}

.attachments {
  margin: 20px 0;
}

.attachments h3 {
  font-size: 16px;
  margin-bottom: 10px;
}

.attachment-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.file-size {
  color: #666;
  font-size: 0.9em;
  margin: 0 10px;
}

.remove-button {
  padding: 4px 8px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.remove-button:hover {
  background-color: #c82333;
}

.file-input {
  margin-top: 8px;
}

.new-files-list {
  margin-top: 10px;
  padding-left: 0;
  list-style: none;
}

.button-area {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eaeaea;
}

.cancel-button {
  padding: 8px 20px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #333;
  cursor: pointer;
  font-size: 14px;
}

.cancel-button:hover {
  background-color: #e5e5e5;
}

.submit-button {
  padding: 8px 20px;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  margin-left: 10px;
}

.submit-button:hover {
  background-color: #0056b3;
}
</style>