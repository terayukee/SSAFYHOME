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
  <select v-model="key" class="form-select" @change="onSelect">
    <!-- Placeholder 역할 -->
    <option value="" disabled selected hidden>{{ placeholder }}</option>
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

<style scoped>
/* 셀렉트 박스 기본 스타일 */
.form-select {
  display: inline-block;
  width: max-content; /* 글씨 내용에 맞게 크기 조정 */
  min-width: 150px; /* 최소 너비 설정 */
  max-width: 250px; /* 최대 너비 설정 */
  padding: 10px 16px; /* 더 넉넉한 패딩으로 터치 영역 확대 */
  font-size: 14px;
  font-weight: 500;
  color: #333;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  appearance: none;
  transition: all 0.2s ease-in-out;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 4 5'%3E%3Cpath fill='%2394959c' d='M2 0L0 2h4zm0 5L0 3h4z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  background-size: 12px;
  cursor: pointer;
}

/* Hover 스타일 */
.form-select:hover {
  border-color: #bbb;
}

/* 포커스 스타일 */
.form-select:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
  outline: none;
}

/* Placeholder 스타일 */
.form-select option[disabled][selected] {
  color: #888;
}

/* 활성화된 상태 */
.form-select:active {
  background-color: #f8f9fa;
}

/* 옵션 리스트 커스텀 */
.form-select option {
  padding: 10px;
  font-weight: 400;
  font-size: 14px;
}
</style>
