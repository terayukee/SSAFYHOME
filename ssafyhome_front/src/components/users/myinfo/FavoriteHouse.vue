<template>
  <div class="favorite-house">
    <h2 class="title">관심단지</h2>
    <div v-for="type in houseTypes" :key="type" class="house-section">
      <h3 class="type-title">{{ type }}</h3>
      <div class="card-container">
        <button
          v-if="showLeftButton(type)"
          class="scroll-button left"
          @click="scrollLeft(type)"
        >
          ‹
        </button>
        <div class="cards">
          <div
            v-for="(card, index) in currentCards(type)"
            :key="index"
            class="card"
          >
            <div class="card-image">
              <img :src="card.image" alt="House" />
            </div>
            <div class="card-info">
              <h4>{{ card.aptNm }}</h4>
              <p>{{ card.location }}</p>
            </div>
          </div>
        </div>
        <button
          v-if="showRightButton(type)"
          class="scroll-button right"
          @click="scrollRight(type)"
        >
          ›
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { fetchUserFavorites } from "@/api/favorite";

// 집 타입들
const houseTypes = ["apartment", "villa", "officetel"];

// 관심 단지 데이터 상태
const favoriteHouses = ref({
  apartment: [],
  villa: [],
  officetel: [],
});

// 카드 인덱스 상태
const cardIndexes = ref({
  apartment: 0,
  villa: 0,
  officetel: 0,
});

const visibleCardsCount = 5; // 한 번에 보여줄 카드 개수

// 현재 화면에 표시될 카드 계산
const currentCards = (type) =>
  computed(() =>
    favoriteHouses.value[type].slice(
      cardIndexes.value[type],
      cardIndexes.value[type] + visibleCardsCount
    )
  ).value;

// 좌측 스크롤 버튼 표시 여부
const showLeftButton = (type) => cardIndexes.value[type] > 0;

// 우측 스크롤 버튼 표시 여부
const showRightButton = (type) =>
  cardIndexes.value[type] + visibleCardsCount <
  favoriteHouses.value[type].length;

// 좌측 스크롤
const scrollLeft = (type) => {
  if (cardIndexes.value[type] > 0) {
    cardIndexes.value[type] -= 1;
  }
};

// 우측 스크롤
const scrollRight = (type) => {
  if (
    cardIndexes.value[type] + visibleCardsCount <
    favoriteHouses.value[type].length
  ) {
    cardIndexes.value[type] += 1;
  }
};

// 관심 단지 데이터 가져오기
const fetchFavorites = () => {
  fetchUserFavorites(
    { userNo: 1 }, // 여기에 로그인한 사용자의 ID를 넣어야 합니다.
    (response) => {
      // 타입별로 데이터 분류
      const groupedByType = { apartment: [], villa: [], officetel: [] };
      response.data.forEach((favorite) => {
        groupedByType[favorite.houseType].push({
          aptNm: favorite.aptNm,
          location: favorite.location,
          image: favorite.image || `/assets/houses/${favorite.houseType}.jpg`,
        });
      });
      favoriteHouses.value = groupedByType;
    },
    (error) => {
      console.error("Failed to fetch favorites:", error);
    }
  );
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchFavorites();
});
</script>

<style scoped>
.favorite-house {
  padding: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.house-section {
  margin-bottom: 40px;
}

.type-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.card-container {
  position: relative;
  display: flex;
  align-items: center;
}

.cards {
  display: flex;
  gap: 16px;
  overflow: hidden;
  width: 100%;
}

.card {
  flex: 0 0 200px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.card-image img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.card-info {
  padding: 10px;
}

.card-info h4 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.card-info p {
  font-size: 14px;
  color: #7f8c8d;
}

.scroll-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 24px;
  color: #333;
  cursor: pointer;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.scroll-button.left {
  left: -20px;
}

.scroll-button.right {
  right: -20px;
}

.scroll-button:hover {
  background: #f0f0f0;
}
</style>
