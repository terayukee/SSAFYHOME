<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { registArticle } from '@/api/board';
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from 'pinia';
const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore)
const router = useRouter();
const route = useRoute();

const selectedFiles = ref([]);
const currentPage = ref(route.params.page || 1);


const article = ref({
  boardNo: 0,
  subject: "",
  content: "",
  userNo: userInfo.value.userNo,
  userNickname: userInfo.value.userNickname,
  registerTime: "",
});

const handleFileUpload = (event) => {
  const files = Array.from(event.target.files);
  selectedFiles.value.push(...files);
};

const removeFile = (index) => {
  selectedFiles.value.splice(index, 1);
};



const submitPost = () => {
  const formData = new FormData();

  // 유저 정보 출력 (디버그용)
  console.log("userNo:", userInfo.value.userNo);

  // 선택한 파일들 formData에 추가
  selectedFiles.value.forEach((file) => {
    formData.append('files', file);
  });

  // userNo 값을 명시적으로 설정
  article.value.userNo = userInfo.value.userNo;

  // 글 데이터를 formData에 추가 (JSON으로 변환)
  formData.append('article', JSON.stringify(article.value));

  // formData 로그 (디버깅용)
  for (const [key, value] of formData.entries()) {
    console.log(`${key}:`, value);
  }

  // API 호출
  registArticle(
    formData,
    (response) => {
      let msg = "글등록이 완료되었습니다.";
      if (response.status === 201) {
        alert(msg);
        router.push({ name: "board-list-page", params: { page: currentPage.value } });
      }
    },
    (error) => {
      console.error(error);
      alert("글등록 처리시 문제가 발생했습니다.");
    }
  );
};


const cancelWriting = () => {
  router.push({ name: "board-list-page", params: { page: currentPage.value } });
};

const ck = () => {
  console.log(userInfo.value.userNickname)
}
</script>

<template>
  <div class="write-container">
    <button @click="ck">체크</button>
    <h1 class="page-title">글쓰기</h1>
    
    <div class="write-form">
      <!-- 제목 입력 -->
      <div class="form-group">
        <label for="title" class="form-label">제목</label>
        <input 
          type="text" 
          id="title"
          v-model="article.subject"
          class="form-input"
          placeholder="제목을 입력하세요"
        >
      </div>

      <!-- 내용 입력 -->
      <div class="form-group">
        <label for="content" class="form-label">내용</label>
        <textarea
          id="content"
          v-model="article.content"
          class="form-textarea"
          placeholder="내용을 입력하세요"
          rows="10"
        ></textarea>
      </div>

      <!-- 파일 첨부 -->
      <div class="form-group">
        <label class="form-label">첨부파일</label>
        <div class="file-upload-container">
          <div class="file-upload-button">
            <label for="file-upload" class="file-label">
              파일 선택
            </label>
            <input
              type="file"
              id="file-upload"
              @change="handleFileUpload"
              multiple
              class="hidden-input"
            >
          </div>
          
          <!-- 선택된 파일 목록 -->
          <ul v-if="selectedFiles.length > 0" class="file-list">
            <li v-for="(file, index) in selectedFiles" :key="index" class="file-item">
              <span class="file-name">{{ file.name }}</span>
              <button @click="removeFile(index)" class="remove-button">삭제</button>
            </li>
          </ul>
        </div>
      </div>

      <!-- 버튼 그룹 -->
      <div class="button-group">
        <button @click="cancelWriting" class="cancel-button">취소</button>
        <button @click="submitPost" class="submit-button">등록</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.write-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
}

.write-form {
  background-color: white;
  border-radius: 4px;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-textarea {
  resize: vertical;
  min-height: 200px;
}

.hidden-input {
  display: none;
}

.file-upload-button {
  margin-bottom: 10px;
}

.file-label {
  display: inline-block;
  padding: 8px 16px;
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.file-label:hover {
  background-color: #e9ecef;
}

.file-list {
  list-style: none;
  padding: 0;
}

.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 8px;
}

.file-name {
  font-size: 14px;
}

.remove-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}

.remove-button:hover {
  background-color: #c82333;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 30px;
}

.submit-button, .cancel-button {
  padding: 8px 24px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.submit-button {
  background-color: #007bff;
  color: white;
  border: none;
}

.submit-button:hover {
  background-color: #0056b3;
}

.cancel-button {
  background-color: #6c757d;
  color: white;
  border: none;
}

.cancel-button:hover {
  background-color: #545b62;
}
</style> 
