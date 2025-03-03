<script setup>
import { ref, computed, reactive, onMounted } from "vue";
import { fetchUserFavoriteRealEstates } from "@/api/favorite";
import { getById } from "@/api/realestate";
import { getBySeq } from "@/api/house";
import { useUserStore } from "@/stores/userStore";

// 컴포넌트
import ForSaleDetailCard from "@/components/map/ForSaleDetailCard.vue";

// 집 타입들
const realEstateTypes = ["아파트", "빌라", "오피스텔"];

const userStore = useUserStore();
const userNo = userStore.userInfo.userNo;

const groupedRealEstatesByType = reactive({
  아파트: [],
  빌라: [],
  오피스텔: [],
});

// 관심 매물 데이터 가져오기
const fetchFavoriteRealEstates = () => {
  fetchUserFavoriteRealEstates(
    { userNo },
    (response) => {
      try {
        response.data.forEach((favorite) => {
          let houseType = "";
          if (favorite.houseType == "apartment") {
            houseType = "아파트";
          } else if (favorite.houseType == "villa") {
            houseType = "빌라";
          } else {
            houseType = "오피스텔";
          }

          getById(
            { realestateId: favorite.realestateId },
            (realEstateResponse) => {
              if (realEstateResponse && realEstateResponse.data) {
                const randomIndex = ref(Math.floor(Math.random() * 20) + 1);
                const realEstateInfo = {
                  id: realEstateResponse.data.id || "알 수 없음",
                  aptSeq: realEstateResponse.data.aptSeq || "알 수 없음",
                  maxFloor: realEstateResponse.data.maxFloor || "알 수 없음",
                  thisFloor: realEstateResponse.data.thisFloor || "알 수 없음",
                  registerYear: realEstateResponse.data.registerYear || 0,
                  registerMonth: realEstateResponse.data.registerMonth || 0,
                  registerDay: realEstateResponse.data.registerDay || 0,
                  excluUseAr: realEstateResponse.data.excluUseAr || 0.0,
                  dealAmount:
                    realEstateResponse.data.dealAmount || "알 수 없음",
                  feeAmount: realEstateResponse.data.feeAmount || "알 수 없음",
                  maintenanceCost:
                    realEstateResponse.data.maintenanceCost || "알 수 없음",
                  dealCategory:
                    realEstateResponse.data.dealCategory || "알 수 없음",
                  houseType: realEstateResponse.data.houseType || "알 수 없음",
                  randomIndex: randomIndex.value,
                  image: `/assets/interior/livingroom/${randomIndex.value}.jpg`,
                  aptNm: "", // 기본값 추가
                };

                // 추가: `getBySeq` 호출
                getBySeq(
                  {
                    aptSeq: realEstateInfo.aptSeq,
                    houseType: favorite.houseType,
                  },
                  (houseResponse) => {
                    if (houseResponse && houseResponse.data) {
                      realEstateInfo.aptNm =
                        houseResponse.data.aptNm || "알 수 없음";
                    }
                    groupedRealEstatesByType[houseType].push(realEstateInfo);
                  },
                  (error) => {
                    console.error(
                      `Failed to fetch aptNm for aptSeq ${realEstateInfo.aptSeq}:`,
                      error
                    );
                    groupedRealEstatesByType[houseType].push(realEstateInfo);
                  }
                );
              }
            },
            (error) => {
              console.error(
                `Failed to fetch details for realestateId ${favorite.realestateId}:`,
                error
              );
            }
          );
        });
      } catch (error) {
        console.error("Error processing favorite real estates:", error);
      }
    },
    (error) => {
      console.error("Failed to fetch favorite real estates:", error);
    }
  );
};

// 카드 인덱스 상태
const apartmentCardIndex = ref(0);
const villaCardIndex = ref(0);
const officetelCardIndex = ref(0);

const visibleCardsCount = 5;

const getCardIndex = (type) => {
  if (type === "아파트") return apartmentCardIndex.value;
  if (type === "빌라") return villaCardIndex.value;
  if (type === "오피스텔") return officetelCardIndex.value;
  return 0; // 기본값
};

const showLeftButton = (type) => {
  if (type === "아파트") return apartmentCardIndex.value > 0;
  if (type === "빌라") return villaCardIndex.value > 0;
  if (type === "오피스텔") return officetelCardIndex.value > 0;
};

const showRightButton = (type) => {
  if (type === "아파트")
    return (
      apartmentCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["아파트"].length
    );
  if (type === "빌라")
    return (
      villaCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["빌라"].length
    );
  if (type === "오피스텔")
    return (
      officetelCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["오피스텔"].length
    );
};

const scrollLeft = (type) => {
  if (type === "아파트" && apartmentCardIndex.value > 0) {
    apartmentCardIndex.value -= 1;
  } else if (type === "빌라" && villaCardIndex.value > 0) {
    villaCardIndex.value -= 1;
  } else if (type === "오피스텔" && officetelCardIndex.value > 0) {
    officetelCardIndex.value -= 1;
  }
};

const scrollRight = (type) => {
  if (
    type === "아파트" &&
    apartmentCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["아파트"].length
  ) {
    apartmentCardIndex.value += 1;
  } else if (
    type === "빌라" &&
    villaCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["빌라"].length
  ) {
    villaCardIndex.value += 1;
  } else if (
    type === "오피스텔" &&
    officetelCardIndex.value + visibleCardsCount <
      groupedRealEstatesByType["오피스텔"].length
  ) {
    officetelCardIndex.value += 1;
  }
};

// 모달 상태
const isModalOpen = ref(false);
const realEstateInfo = ref(null);
const selectedHouseType = ref("");
const selectedRandomIndex = ref(null);

const openRealEstateDetailCard = (selectedRealEstate) => {
  realEstateInfo.value = selectedRealEstate;
  selectedHouseType.value = selectedRealEstate.houseType;
  selectedRandomIndex.value = selectedRealEstate.randomIndex;
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchFavoriteRealEstates();
  console.log("groupedRealEstatesByType : ", groupedRealEstatesByType);
  console.log("apartmentCardIndex:", apartmentCardIndex.value);
  console.log("villaCardIndex:", villaCardIndex.value);
  console.log("officetelCardIndex:", officetelCardIndex.value);
});
</script>

<template>
  <div class="favorite-real-estate">
    <div
      v-for="type in realEstateTypes"
      :key="type"
      class="real-estate-section"
    >
      <h3 class="type-title">
        {{ type }}
      </h3>
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
            v-for="(realEstate, index) in groupedRealEstatesByType[type].slice(
              getCardIndex(type),
              getCardIndex(type) + visibleCardsCount
            )"
            :key="index"
            class="card"
            @click="openRealEstateDetailCard(realEstate)"
          >
            <div class="card-image">
              <img :src="realEstate.image" alt="Real Estate Image" />
            </div>
            <div class="card-info">
              <h4>{{ realEstate.aptNm }}</h4>
              <p>{{ realEstate.excluUseAr }}㎡</p>
              <p>{{ realEstate.dealCategory }}</p>
              <template v-if="realEstate.dealCategory === '월세'">
                {{ realEstate.dealAmount || "N/A" }}만원 /
                {{ realEstate.feeAmount }}만원
              </template>
              <template v-else>
                {{
                  realEstate.dealAmount
                    ? (
                        parseFloat(realEstate.dealAmount.replace(/,/g, "")) *
                        0.0001
                      ).toFixed(2) + "억"
                    : "N/A"
                }}
              </template>
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
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-button-container">
          <button class="close-button" @click="closeModal">닫기</button>
        </div>
        <ForSaleDetailCard
          :realestates="realEstateInfo"
          :houseType="selectedHouseType"
          :selectedCard="realEstateInfo"
          :randomIndex="selectedRandomIndex"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.favorite-real-estate {
  padding: 20px;
}

.real-estate-section {
  margin-bottom: 20px;
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
  width: 1070px;
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
  /* width: 400px; */
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
