<script setup>
import { ref, computed, onMounted, reactive, watch } from "vue";
import { fetchUserFavoriteHouses } from "@/api/favorite";
import { getBySeq } from "@/api/house";
import { useUserStore } from "@/stores/userStore";

// 집 타입들
const houseTypes = ["apartment", "villa", "officetel"];

const userStore = useUserStore();
const userNo = userStore.userInfo.userNo;

const groupedByType = reactive({ apartment: [], villa: [], officetel: [] });

// 관심 단지 데이터 상태
const favoriteHouses = ref({
  apartment: [],
  villa: [],
  officetel: [],
});

// 카드 인덱스 상태
const cardIndexes = reactive({
  apartment: 0,
  villa: 0,
  officetel: 0,
});

const visibleCardsCount = 4; // 한 번에 보여줄 카드 개수
const currentCards = reactive({ apartment: [], villa: [], officetel: [] });
watch(
  [() => groupedByType, () => cardIndexes],
  () => {
    setTimeout(() => {
      houseTypes.forEach((type) => {
        currentCards[type] = groupedByType[type].slice(
          cardIndexes[type],
          cardIndexes[type] + visibleCardsCount
        );
      });
    }, 1000); // 1초 대기 후 업데이트
  },
  { immediate: true, deep: true }
);

// 좌측 스크롤 버튼 표시 여부
const showLeftButton = (type) => cardIndexes[type] > 0;

// 우측 스크롤 버튼 표시 여부
const showRightButton = (type) =>
  cardIndexes[type] + visibleCardsCount < groupedByType[type].length;

// 좌측 스크롤
const scrollLeft = (type) => {
  if (cardIndexes[type] > 0) {
    cardIndexes[type] -= 1;
  }
};

// 우측 스크롤
const scrollRight = (type) => {
  if (cardIndexes[type] + visibleCardsCount < groupedByType[type].length) {
    cardIndexes[type] += 1;
  }
};

// 관심 단지 데이터 가져오기
const fetchFavorites = () => {
  fetchUserFavoriteHouses(
    { userNo }, // 로그인한 사용자의 ID를 전달
    (response) => {
      try {
        // 타입별로 데이터 분류
        // const groupedByType = { apartment: [], villa: [], officetel: [] };

        // API 응답 처리
        response.data.forEach((favorite) => {
          // houseType 값을 기준으로 데이터 분류
          const houseType =
            favorite.houseType === "apartment"
              ? "apart"
              : favorite.houseType === "villa"
              ? "villa"
              : "officetel";

          // 랜덤 인덱스 생성
          const randomIndex = Math.floor(Math.random() * 20) + 1; // 1~20 범위

          // getBySeq를 통해 주택 정보 가져오기
          getBySeq(
            { aptSeq: favorite.aptSeq, houseType: favorite.houseType },
            (houseResponse) => {
              if (houseResponse && houseResponse.data) {
                console.log("houseResponse : ", houseResponse);
                groupedByType[favorite.houseType].push({
                  aptNm: houseResponse.data.aptNm || "알 수 없음", // 가져온 주택 이름
                  location: houseResponse.data.jibun || "위치 정보 없음", // 가져온 위치 정보
                  image: `/assets/${houseType}/high${randomIndex}.jpg`,
                });
              } else {
                console.warn(
                  `Invalid response for aptSeq ${favorite.aptSeq}:`,
                  houseResponse
                );
              }
            },
            (error) => {
              console.error(
                `Failed to fetch details for aptSeq ${favorite.aptSeq}:`,
                error
              );
            }
          );
        });

        favoriteHouses.value = groupedByType; // 관심 단지 상태 업데이트
        console.log("Updated favorite houses:", favoriteHouses.value);
        console.log("groupedByType : ", groupedByType);
      } catch (error) {
        console.error("Error while processing favorite houses:", error);
      }
    },
    (error) => {
      console.error("Failed to fetch favorite houses:", error);
    }
  );
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchFavorites();
  // console.log("유저의 관심 주택 목록: ", favoriteHouses);
  const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

  (async () => {
    await sleep(1000); // 1초 대기
    console.log("cardIndexes", cardIndexes);
    console.log(groupedByType["apartment"].length);
    console.log(groupedByType["villa"].length);
    console.log(groupedByType["officetel"].length);
  })();
});
</script>

<template>
  <div class="favorite-house">
    <!-- <h2 class="title">관심단지</h2> -->
    <div
      v-for="(houses, type) in groupedByType"
      :key="type"
      class="house-section"
    >
      <h3 class="type-title">
        {{
          type === "apartment"
            ? "아파트"
            : type === "villa"
            ? "빌라"
            : "오피스텔"
        }}
      </h3>
      <!-- 좌측 스크롤 버튼 -->
      <button
        v-if="showLeftButton(type)"
        class="scroll-button left"
        @click="scrollLeft(type)"
      >
        ‹
      </button>
      <div class="cards">
        <div v-for="(house, index) in houses" :key="index" class="card">
          <div class="card-image">
            <img :src="house.image" alt="House Image" />
          </div>
          <div class="card-info">
            <h4>{{ house.aptNm }}</h4>
            <!-- <p>{{ house.location }}</p> -->
          </div>
        </div>
      </div>
      <!-- 우측 스크롤 버튼 -->
      <button
        v-if="showRightButton(type)"
        class="scroll-button right"
        @click="scrollRight(type)"
      >
        ›
      </button>
    </div>
  </div>
</template>

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
  padding: 20px;
  background-color: aliceblue;
}

.type-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  color: blue;
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
