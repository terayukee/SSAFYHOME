<template>
  <div class="category-container">
    <div class="category-row">
      <div
        v-for="(category, index) in categories"
        :key="index"
        class="category-item"
        @click="navigateToCategory(category.type)"
      >
        <div class="category-top">
          <p class="category-title">{{ category.name }}</p>
          <p class="category-description">{{ category.description }}</p>
        </div>

        <div class="category-bottom">
          <img :src="category.icon" alt="" class="category-icon" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

import ApartmentIcon from "@/assets/icons/residential.png";
import VillaIcon from "@/assets/icons/villa.png";
import OfficetelIcon from "@/assets/icons/office.png";
import NewsIcon from "@/assets/icons/paper.png";

const categories = [
  {
    name: "아파트",
    description: "풍부한 단지정보 및 거래 정보까지!",
    type: "apartment",
    icon: ApartmentIcon,
  },
  {
    name: "주택/빌라",
    description: "전월세부터 매매까지 모~든 정보!",
    type: "villa",
    icon: VillaIcon,
  },
  {
    name: "오피스텔",
    description: "다양한 정보!",
    type: "officetel",
    icon: OfficetelIcon,
  },
  {
    name: "부동산 뉴스",
    description: "전국의 모든 부동산 뉴스 확인 가능!",
    type: "news",
    icon: NewsIcon,
  },
];

const router = useRouter();

const navigateToCategory = (type) => {
  if (type === "news") {
    // "news"는 아무 작업도 하지 않음
    return;
  } else {
    router.push({
      path: "/map",
      query: {
        housetype: type, // category.type을 housetype으로 전달
      },
    });
  }
};
</script>

<style scoped>
.category-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
  margin-left: 5%;
  margin-top: 5%;
  margin-right: 5%;
  width: 600px;
  height: 400px;
}

.category-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: space-between;
}

.category-item {
  cursor: pointer;
  text-align: left;
  border: 1px solid #4791fe;
  border-radius: 10px;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s;
  width: 40%;
  height: 40%;
}

.category-item:hover {
  background-color: #f9f9f9;
}

.category-title {
  font-weight: bold;
  margin-bottom: 8px;
  font-size: 22px;
  color: rgb(79, 152, 255);
}

.category-description {
  font-size: 14px;
  color: #666;
}

.category-bottom {
  display: flex; /* 플렉스 컨테이너로 설정 */
  justify-content: flex-end; /* 내용물을 오른쪽 정렬 */
  align-items: center; /* 세로 가운데 정렬 */
}

.category-icon {
  width: 50px;
  height: 50px;
}
</style>
