<script setup>
import { computed } from "vue";

const props = defineProps({ currentPage: Number, totalPage: Number });
const emit = defineEmits(["pageChange"]);

const navigationSize = parseInt(import.meta.env.VITE_ARTICLE_NAVIGATION_SIZE);

const startPage = computed(() => {
  return parseInt((props.currentPage - 1) / navigationSize) * navigationSize + 1;
});

const endPage = computed(() => {
  let lastPage =
    parseInt((props.currentPage - 1) / navigationSize) * navigationSize + navigationSize;
  return props.totalPage < lastPage ? props.totalPage : lastPage;
});

const endRange = computed(() => {
  return parseInt((props.totalPage - 1) / navigationSize) * navigationSize < props.currentPage;
});

function range(start, end) {
  const list = [];
  for (let i = start; i <= end; i++) list.push(i);
  return list;
}

function onPageChange(pg) {
  console.log(pg + "로 이동!!!");
  emit("pageChange", pg);
}
</script>
<template>
  <div class="pagination-container">
    <ul class="pagination">
      <li>
        <button @click="onPageChange(1)">최신</button>
      </li>
      <li>
        <button @click="onPageChange(startPage == 1 ? 1 : startPage - 1)">이전</button>
      </li>
      <template v-for="pg in range(startPage, endPage)" :key="pg">
        <li :class="{ active: currentPage === pg }">
          <button @click="onPageChange(pg)">{{ pg }}</button>
        </li>
      </template>
      <li>
        <button @click="onPageChange(endRange ? totalPage : endPage + 1)">다음</button>
      </li>
      <li>
        <button @click="onPageChange(totalPage)">마지막</button>
      </li>
    </ul>
  </div>
</template>
<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.pagination {
  display: flex;
  list-style: none;
  padding: 0;
  gap: 5px;
}

.pagination li {
  margin: 0 2px;
}

.pagination button {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  color: #007bff;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.pagination button:hover {
  background-color: #e9ecef;
}

.pagination li.active button {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}
</style>