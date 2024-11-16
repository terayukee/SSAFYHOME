<template>
  <div class="category-page">
    <h2>{{ categoryName }}</h2>
    <p>여기에 {{ categoryName }} 관련 매물 리스트가 표시됩니다.</p>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";

const route = useRoute();
const categoryName = route.params.type;
</script>

<style scoped>
.category-page {
  padding: 16px;
}
</style>
