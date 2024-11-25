<script setup>
import { ref, onMounted } from 'vue';
import { listArticle } from '@/api/board';
import { useRoute, useRouter } from "vue-router";
import VPageNavigation from '../common/VPageNavigation.vue';
import BoardListItem from './item/BoardListItem.vue';
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from 'pinia';

const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore); 

const router = useRouter();
const route = useRoute();

onMounted(()=>{
  param.value.pgno = route.params.page;
  getArticleList();
})

const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;

const articles = ref([]);
const currentPage = ref(1);
const totalPage = ref(0);

const param = ref({
  pgno: currentPage.value,
  spp: VITE_ARTICLE_LIST_SIZE,
  key: "",
  word: "",
});

const getArticleList = () => {
  console.log("서버에서 글목록 얻어오자!!!", param.value);
  listArticle(
    param.value,
    ({ data }) => {
      articles.value = data.articles;
      currentPage.value = data.currentPage;
      totalPage.value = data.totalPageCount;
    },
    (error) => {
      console.log(error);
    }
  );
};

const onPageChange = (val) => {
  currentPage.value = val;
  param.value.pgno = val;
  getArticleList();
  router.push({ name: "board-list-page", params: { page: val } });
};

const moveWrite = (val = 1) => {
  router.push({ name: "board-write" , params: { page : val }});
};
</script>

<template>
  <div class="board-container">
    <table class="board-table">
      <thead>
        <tr>
          <th class="col-no">번호</th>
          <th class="col-title">제목</th>
          <th class="col-author">작성자</th>
          <th class="col-hit">조회수</th>
        </tr>
      </thead>
      <tbody>
        <BoardListItem 
          v-for="(article, index) in articles"
          :key="article.boardNo"
          :article="article"
          :current-page="currentPage"
          :index="(currentPage - 1) * Number(VITE_ARTICLE_LIST_SIZE) + index + 1"
        />
      </tbody>
    </table>
    <div class="button-container">
      <button class="pagination-button" @click="moveWrite(param.pgno)" v-if="userInfo && userInfo.role === 'ADMIN'">게시판 글쓰기</button>
    </div>
  </div>  
  <VPageNavigation
    :current-page="currentPage"
    :total-page="totalPage"
    @pageChange="onPageChange"
  />
</template>


<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

/* 컬럼 너비 설정 */
.col-no { width: 7%; }
.col-title { width: 63%; }
.col-author { width: 15%; }
.col-hit { width: 15%; }

/* 테이블 헤더 스타일 */
.board-table th {
  padding: 12px;
  background-color: #f5f5f5;
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  text-align: center;
  font-weight: bold;
}

/* 테이블 셀 공통 스타일 */
.board-table td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: center; /* 모든 셀을 중앙 정렬로 변경 */
}

/* 제목 열만 왼쪽 정렬 */
.board-table .text-start {
  text-align: left !important;
  padding-left: 10px !important;
}

.board-table tr {
  height: 50px;
}

.board-table tbody tr:hover {
  background-color: #f8f8f8;
}

.board-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.board-row:hover {
  background-color: #f8f8f8;
}

/* 링크 스타일 */
.article-title {
  text-decoration: none;
  color: #333;
  display: block;
  padding: 4px 0;
}

.article-title:hover {
  color: #007bff;
  text-decoration: underline;
}
/* 여기까지 */
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

.writing-container {
    position: relative;
    display: inline-block;
}

.pagination-button {
    background-color: #f8f9fa;      /* 버튼 배경색 */
    border: 1px solid #dee2e6;      /* 버튼 테두리 */
    color: #007bff;                 /* 기본 텍스트 색 */
    padding: 8px 12px;              /* 패딩 */
    margin-right: 10px;
    cursor: pointer;                /* 마우스 커서 손 모양 */
    border-radius: 4px;             /* 둥근 모서리 */
    transition: all 0.3s ease;      /* 부드러운 전환 효과 */
}

/* 마우스를 올렸을 때 버튼 스타일 */
.pagination-button:hover {
    background-color: #e9ecef;      /* 호버 시 배경색 */
}

/* 버튼이 활성화되었을 때 (클릭된 상태) */
.pagination-button:active {
    background-color: #007bff;      /* 클릭 시 배경색 */
    color: white;                   /* 텍스트 색 변경 */
    border-color: #007bff;          /* 테두리 색 변경 */
}

/* 버튼에 포커스를 주었을 때 스타일 */
.pagination-button:focus {
    outline: none;                  /* 기본 외곽선 제거 */
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.5); /* 포커스 시 그림자 효과 */
}
</style>