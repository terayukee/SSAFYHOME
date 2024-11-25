<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

defineProps({
  article: Object,
  currentPage: Number,
  index: Number
});

const route = useRoute();
const router = useRouter();
</script>

<template>
  <router-link
    :to="{ name: 'board-view', params: { boardNo: article.boardNo, page: currentPage } }"
    custom
    v-slot="{ navigate }"
  >
    <tr class="board-row" @click="navigate" role="link">
      <td>{{ index }}</td>
      <td class="text-start">{{ article.subject }}</td>
      <td>{{ article.userNickname }}</td>
      <td>{{ article.hit }}</td>
    </tr>
  </router-link>
</template>

<style scoped>
.text-start {
  padding-left: 10px !important;
  text-align: left !important;
}

.board-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.board-row:hover {
  background-color: #f8f8f8;
  color: #007bff;
}

td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: center;
}
</style>