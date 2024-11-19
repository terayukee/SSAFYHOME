<script setup>
import { ref } from 'vue';
import { useRouter , useRoute } from 'vue-router';

defineProps({ article: Object });

const route = useRoute()
const router = useRouter()

</script>

<template>
  <tr>
    <!-- 번호를  수 있도록 router-link로 감싸기 -->
    <td>
      <router-link
        :to="{ name: 'article-view', params: { articleno: article.boardNo } }"
        class="link-cell"
      >
        {{ article.boardNo }}
      </router-link>
    </td>

    <!-- 제목을 클릭할 수 있도록 router-link로 감싸기 -->
    <td class="text-start">
      <router-link
        :to="{ name: 'article-view', params: { articleno: article.boardNo } }"
        class="article-title"
      >
        {{ article.subject }}
      </router-link>
    </td>

    <!-- 작성자를 클릭할 수 있도록 router-link로 감싸기 -->
    <td>
      <router-link
        :to="{ name: 'article-view', params: { articleno: article.boardNo } }"
        class="link-cell"
      >
        {{ article.userNickname }}
      </router-link>
    </td>

    <!-- 조회수를 클릭할 수 있도록 router-link로 감싸기 -->
    <td>
      <router-link
        :to="{ name: 'article-view', params: { articleno: article.boardNo } }"
        class="link-cell"
      >
        {{ article.hit }}
      </router-link>
    </td>
  </tr>
</template>

<style scoped>
.text-start {
  padding-left: 10px !important;
}

.article-title {
  text-decoration: none;
  color: #333;
  display: block;
  padding: 4px 0;
}


.link-cell {
  text-decoration: none;
  color: inherit;
  display: block;
  padding: 4px 0;
}

</style>
