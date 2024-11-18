<script setup>
defineProps({ article: Object });
</script>

<template>
    <div>
    <tr class="text-center">
        <th scope="row">{{ article.articleNo }}</th>
        <td class="text-start">
        <router-link
            :to="{ name: 'article-view', params: { articleno: article.articleNo } }"
            class="article-title link-dark"
        >
            {{ article.subject }}
        </router-link>
        </td>
        <td>{{ article.userName }}</td>
        <td>{{ article.hit }}</td>
        <td>{{ article.registerTime }}</td>
  </tr>
    </div>
</template>

<style scoped>

</style>