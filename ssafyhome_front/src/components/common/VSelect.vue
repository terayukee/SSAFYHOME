<script setup>
import { ref } from "vue";

defineProps({
  selectOption: {
    type: Array,
    default: () => [],
  },
  placeholder: {
    type: String,
    default: "옵션을 선택하세요",
  },
});

const emit = defineEmits(["onKeySelect"]);

const key = ref("");

const onSelect = () => {
  emit("onKeySelect", key.value);
};
</script>

<template>
  <select
    v-model="key"
    class="form-select form-select-sm w-50"
    @change="onSelect"
  >
    <!-- Placeholder 역할 -->
    <option value="" disabled selected>{{ placeholder }}</option>
    <!-- 옵션 목록 -->
    <option
      v-for="option in selectOption"
      :key="option.value"
      :value="option.value"
    >
      {{ option.text }}
    </option>
  </select>
</template>

<style scoped></style>
