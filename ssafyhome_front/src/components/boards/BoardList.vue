<script setup>
import { ref, onMounted } from 'vue';
import { listArticle } from '@/api/board';

import VPageNavigation from '../common/VPageNavigation.vue';

onMounted(()=>{
    getArticleList();
})



const articles = ref([]);
const currentPage = ref(1);
const totalPage = ref(0);

const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;

// 페이징 처리
const param = ref({
  pgno: currentPage.value,
  spp: VITE_ARTICLE_LIST_SIZE,
  key: "",
  word: "",
});

const changeKey = (val) => {
  console.log("BoarList에서 선택한 조건 : " + val);
  param.value.key = val;
};

const onPageChange = (val) => {
  console.log(val + "번 페이지로 이동 준비 끝!!!");
  currentPage.value = val;
  param.value.pgno = val;
  getArticleList();
};

const getArticleList = () => {
  listArticle(
    param.value,
    ({data}) => {
      console.log("adsdsa")
    articles.value = data.articles;
    currentPage.value = data.currentPage;
    totalPage.value = data.totalPageCount;
    },
    (error) => {
        console.log(error);
    }
  )
};

</script>

<template>
  <div class="board-container">
    <!-- 게시글 목록 테이블 -->
    <table class="board-table">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>조회수</th>
        </tr>
      </thead>
      <tbody>
        <!-- <tr v-for="post in boardList" 
            :key="post.id" 
            @click="handlePostClick(post.id)"
            class="board-row">
          <td>{{ post.id }}</td>
          <td>{{ post.title }}</td>
          <td>{{ post.author }}</td>
          <td>{{ post.views }}</td>
        </tr> -->
      </tbody>
    </table>
  </div>  
    <!-- 페이지네이션 -->
    <VPageNavigation
        :current-page="currentPage"
        :total-page="totalPage"
        @pageChange="onPageChange"
    ></VPageNavigation>
</template>


<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.board-title {
  font-size: 24px;
  margin-bottom: 20px;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.board-table th,
.board-table td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.board-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.board-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.board-row:hover {
  background-color: #f8f8f8;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin: 20px 0;
}

.page-btn {
  padding: 8px 12px;
  border: 1px solid #ddd;
  background-color: white;
  cursor: pointer;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
  background-color: #f0f0f0;
}

.page-btn.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.page-btn:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.write-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.write-btn:hover {
  background-color: #0056b3;
}
</style>