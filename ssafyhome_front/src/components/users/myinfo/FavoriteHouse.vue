<script setup>
import { ref, computed, onMounted, reactive, watch } from "vue";
import { fetchUserFavoriteHouses } from "@/api/favorite";
import { getBySeq } from "@/api/house";
import { useUserStore } from "@/stores/userStore";

// 컴포넌트
import HouseDetailCard from "@/components/map/HouseDetailCard.vue";

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
                  aptSeq: houseResponse.data.aptSeq || "알 수 없음", // 주택 고유 번호
                  sggCd: houseResponse.data.sggCd || "알 수 없음", // 시군구 코드
                  umdCd: houseResponse.data.umdCd || "알 수 없음", // 읍면동 코드
                  umdNm: houseResponse.data.umdNm || "알 수 없음", // 읍면동 이름
                  jibun: houseResponse.data.jibun || "알 수 없음", // 지번 주소
                  roadNm: houseResponse.data.roadNm || "알 수 없음", // 도로명 주소
                  aptNm: houseResponse.data.aptNm || "알 수 없음", // 아파트 이름
                  buildYear: houseResponse.data.buildYear || "알 수 없음", // 건축 연도
                  latitude: houseResponse.data.latitude || "알 수 없음", // 위도
                  longitude: houseResponse.data.longitude || "알 수 없음", // 경도
                  image: `/assets/${houseType}/high${randomIndex}.jpg`, // 랜덤 이미지 경로
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

// 좌(<) 우(>) 이동 버튼 관련 코드
// 카드 인덱스 상태
const apartmentCardIndex = ref(0);
const villaCardIndex = ref(0);
const officetelCardIndex = ref(0);

const visibleCardsCount = 5; // 한 번에 보여줄 카드 개수

const showLeftButton1 = computed(() => apartmentCardIndex.value > 0);
const showLeftButton2 = computed(() => villaCardIndex.value > 0);
const showLeftButton3 = computed(() => officetelCardIndex.value > 0);

const showRightButton1 = computed(
  () =>
    apartmentCardIndex.value + visibleCardsCount <
    groupedByType["apartment"].length
);
const showRightButton2 = computed(
  () => villaCardIndex.value + visibleCardsCount < groupedByType["villa"].length
);
const showRightButton3 = computed(
  () =>
    officetelCardIndex.value + visibleCardsCount <
    groupedByType["officetel"].length
);

const scrollLeft = (index) => {
  if (index == 1) {
    if (apartmentCardIndex.value > 0) {
      apartmentCardIndex.value -= 1;
    }
  } else if (index == 2) {
    if (villaCardIndex.value > 0) {
      villaCardIndex.value -= 1;
    }
  } else {
    if (officetelCardIndex.value > 0) {
      officetelCardIndex.value -= 1;
    }
  }
};

const scrollRight = (index) => {
  if (index == 1) {
    if (
      apartmentCardIndex.value + visibleCardsCount <
      groupedByType["apartment"].length
    ) {
      apartmentCardIndex.value += 1;
    }
  } else if (index == 2) {
    if (
      villaCardIndex.value + visibleCardsCount <
      groupedByType["villa"].length
    ) {
      villaCardIndex.value += 1;
    }
  } else {
    if (
      officetelCardIndex.value + visibleCardsCount <
      groupedByType["officetel"].length
    ) {
      officetelCardIndex.value += 1;
    }
  }
};
// 현재 화면에 표시될 카드 계산
const currentApartmentCards = computed(() =>
  groupedByType["apartment"].slice(
    apartmentCardIndex.value,
    apartmentCardIndex.value + visibleCardsCount
  )
);

const currentVillaCards = computed(() =>
  groupedByType["villa"].slice(
    villaCardIndex.value,
    villaCardIndex.value + visibleCardsCount
  )
);

const currentOfficetelCards = computed(() =>
  groupedByType["officetel"].slice(
    officetelCardIndex.value,
    officetelCardIndex.value + visibleCardsCount
  )
);

// 모달 상태
const isModalOpen = ref(false); // 모달 열림/닫힘 여부
const selectedHouse = ref(null); // 선택된 주택 정보
const selectedHouseType = ref(""); // 선택된 주택 타입

// (Modal) 주택 상세정보 띄우기
const openHouseDetailCard = (houseType, house) => {
  selectedHouseType.value = houseType; // 주택 타입 설정
  selectedHouse.value = house; // 선택된 주택 정보 설정
  isModalOpen.value = true; // 모달 열기
  console.log("selectedHouseType.value", selectedHouseType.value);
  console.log("selectedHouse.value", selectedHouse.value);
};

const closeModal = () => {
  isModalOpen.value = false; // 모달 닫기
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchFavorites();
  // console.log("유저의 관심 주택 목록: ", favoriteHouses);
  const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

  (async () => {
    await sleep(1000); // 1초 대기
    console.log("apartmentCardIndex", apartmentCardIndex.value);
    console.log("villaCardIndex", villaCardIndex.value);
    console.log("officetelCardIndex", officetelCardIndex.value);
    console.log("아파트 배열 length", groupedByType["apartment"].length);
    console.log("빌라 배열 length", groupedByType["villa"].length);
    console.log("오피스텔 배열 length", groupedByType["officetel"].length);
  })();
});
</script>

<template>
  <div class="favorite-house">
    <!-- 아파트 -->
    <h3 class="type-title">아파트</h3>
    <div class="apartment-container house-container">
      <button
        v-if="showLeftButton1"
        class="scroll-button left"
        @click="scrollLeft(1)"
      >
        ‹
      </button>
      <div class="cards">
        <div
          v-for="(house, index) in currentApartmentCards"
          :key="index"
          class="card"
          @click="openHouseDetailCard(`apartment`, house)"
        >
          <div class="card-image">
            <img :src="house.image" alt="House Image" />
          </div>
          <div class="card-info">
            <h4>{{ house.aptNm }}</h4>
            <!-- <p>{{ house.location }}</p> -->
          </div>
        </div>
      </div>
      <button
        v-if="showRightButton1"
        class="scroll-button right"
        @click="scrollRight(1)"
      >
        ›
      </button>
    </div>
    <!-- 빌라 -->
    <h3 class="type-title">빌라</h3>
    <div class="villa-container house-container">
      <button
        v-if="showLeftButton2"
        class="scroll-button left"
        @click="scrollLeft(2)"
      >
        ‹
      </button>
      <div class="cards">
        <div
          v-for="(house, index) in currentVillaCards"
          :key="index"
          class="card"
          @click="openHouseDetailCard(`villa`, house)"
        >
          <div class="card-image">
            <img :src="house.image" alt="House Image" />
          </div>
          <div class="card-info">
            <h4>{{ house.aptNm }}</h4>
            <!-- <p>{{ house.location }}</p> -->
          </div>
        </div>
      </div>
      <button
        v-if="showRightButton2"
        class="scroll-button right"
        @click="scrollRight(2)"
      >
        ›
      </button>
    </div>
    <!-- 오피스텔 -->
    <h3 class="type-title">오피스텔</h3>
    <div class="officetel-container house-container">
      <button
        v-if="showLeftButton3"
        class="scroll-button left"
        @click="scrollLeft(3)"
      >
        ‹
      </button>
      <div class="cards">
        <div
          v-for="(house, index) in currentOfficetelCards"
          :key="index"
          class="card"
          @click="openHouseDetailCard(`officetel`, house)"
        >
          <div class="card-image">
            <img :src="house.image" alt="House Image" />
          </div>
          <div class="card-info">
            <h4>{{ house.aptNm }}</h4>
            <!-- <p>{{ house.location }}</p> -->
          </div>
        </div>
      </div>
      <button
        v-if="showRightButton3"
        class="scroll-button right"
        @click="scrollRight(3)"
      >
        ›
      </button>
    </div>

    <!-- 모달 -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-button-container">
          <button class="close-button" @click="closeModal">닫기</button>
        </div>

        <HouseDetailCard
          :selectedHouse="selectedHouse"
          :houseType="selectedHouseType"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.favorite-house {
  padding: 20px;
}

.house-container {
  margin-bottom: 100px;
}

.apartment-container {
  position: relative; /* 부모 요소에 위치 기준을 잡아줌 */
}

.villa-container {
  position: relative;
}

.officetel-container {
  position: relative;
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
  color: rgb(24, 115, 252);
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

/* 좌 우 스크롤 버튼 */
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

/* 모달 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  z-index: 1000;
  overflow-y: scroll;
  padding: 20px;
}

.modal-button-container {
  display: flex;
  justify-content: end;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
  margin-top: 20px; /* 추가적인 상단 마진 */
}

.close-button {
  margin-top: 20px;
  padding: 10px 20px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.close-button:hover {
  background: #0056b3;
}
</style>
