import { defineStore } from "pinia";

export const useCategoryStore = defineStore("category", {
  state: () => ({
    selectedCategory: "",
  }),
  actions: {
    selectCategory(category) {
      this.selectedCategory = category;
    },
  },
});
