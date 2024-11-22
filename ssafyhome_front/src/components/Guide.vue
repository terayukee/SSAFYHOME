<template>
  <div class="guide-area">
    <div class="guide">
      <h2 class="guide-title">SSAFYHOME이 알려주는 부동산 가이드</h2>
      <div class="guide-cards-container">
        <button
          v-if="showLeftButton"
          class="scroll-button left"
          @click="scrollLeft"
        >
          ‹
        </button>
        <!-- 현재 화면에 표시될 카드만 렌더링 -->
        <div class="guide-cards">
          <div
            v-for="(card, index) in currentCards"
            :key="index"
            class="guide-card"
          >
            <a :href="card.url" target="_blank" class="guide-link">
              <p class="guide-category">{{ card.category }}</p>
              <p class="guide-content">{{ card.content }}</p>
              <div class="guide-bottom">
                <span class="guide-icon">></span>
              </div>
            </a>
          </div>
        </div>
        <button
          v-if="showRightButton"
          class="scroll-button right"
          @click="scrollRight"
        >
          ›
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { getGuides } from "@/api/article";

const guideCards = ref([]);

const cardIndex = ref(0);
const visibleCardsCount = 5;

// 현재 화면에 표시될 카드 계산
const currentCards = computed(() =>
  guideCards.value.slice(cardIndex.value, cardIndex.value + visibleCardsCount)
);

const showLeftButton = computed(() => cardIndex.value > 0);
const showRightButton = computed(
  () => cardIndex.value + visibleCardsCount < guideCards.value.length
);

const scrollLeft = () => {
  if (cardIndex.value > 0) {
    cardIndex.value -= 1;
  }
};

const scrollRight = () => {
  if (cardIndex.value + visibleCardsCount < guideCards.value.length) {
    cardIndex.value += 1;
  }
};

// 가이드 카드 데이터 불러오기
const fetchGuides = () => {
  getGuides(
    (response) => {
      guideCards.value = response.data;
    },
    (error) => {
      console.error("Failed to fetch guides:", error);
    }
  );
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchGuides();
});
</script>

<style scoped>
.guide-area {
  display: flex;
  justify-content: center;
}

.guide {
  margin-top: 40px;
  padding: 40px 150px;
  background-color: #f9f9f9;
}

.guide-title {
  font-size: 20px;
  font-weight: bold;
  color: #1a73e8;
  margin-bottom: 24px;
}

.guide-cards-container {
  position: relative;
  display: flex;
  align-items: center;
}

.guide-cards {
  display: flex;
  gap: 16px;
  overflow: hidden;
  width: 1200px;
}

.guide-card {
  flex: 0 0 200px;
  background-color: #f5f5f5;
  border-top: 2px solid blue;
  border-bottom: 1px solid #ddd;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: left;
  transition: transform 0.3s, box-shadow 0.3s;
}

.guide-card:hover {
  border-top: 2px solid rgb(183, 0, 255);
  transform: translateY(4px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.guide-link {
  text-decoration: none;
  color: inherit;
}

.guide-category {
  color: #1a73e8;
  font-size: 14px;
  margin-bottom: 8px;
}

.guide-content {
  height: 200px;
}

.guide-bottom {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.guide-icon {
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid #1a73e8;
  border-radius: 50%;
  font-size: 16px;
  color: #1a73e8;
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
