<script setup>
import { defineProps, defineEmits, ref, onMounted, watch } from "vue";
import "@/assets/main.css";
import { useUserStore } from "@/stores/userStore";
import {
  fetchUserFavoriteRealEstates,
  toggleFavoriteRealEstate,
} from "@/api/favorite"; // API 호출

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  realestates: {
    type: Array,
    default: () => [],
  },
  houseType: {
    type: String,
  },
});

// 부모로 이벤트를 전달하기 위한 emit 정의
const emit = defineEmits(["cardClick"]);

// 랜덤 인덱스를 추가한 로컬 상태
const realestatesWithRandomIndex = ref([]);

// 관심 목록 상태
const favoriteRealEstateIds = ref(new Set());
const alertMessage = ref(null);

// 랜덤 인덱스 추가 함수
const initializeRealestates = () => {
  realestatesWithRandomIndex.value = props.realestates.map((realestate) => ({
    ...realestate,
    randomIndex: Math.floor(Math.random() * 20) + 1, // 1~20 범위의 랜덤 값 추가
  }));
};

// 관심 목록 불러오기
const fetchFavorites = async () => {
  if (useUserStore().isLogin) {
    const userNo = useUserStore().userInfo.userNo;
    await fetchUserFavoriteRealEstates({ userNo }, (response) => {
      favoriteRealEstateIds.value = new Set(
        response.data
          .filter((item) => item.houseType === props.houseType)
          .map((item) => item.realestateId)
      );
    });
  }
};

// 하트 클릭 핸들러
const handleFavoriteClick = async (realestate, event) => {
  event.stopPropagation(); // 카드 클릭 이벤트 방지

  if (!useUserStore().isLogin) {
    showAlert("관심 매물 선택은 로그인 후 가능합니다.");
    return;
  }

  const param = {
    userNo: useUserStore().userInfo.userNo,
    realestateId: realestate.id,
    dealCategory: realestate.dealCategory,
    houseType: props.houseType,
  };

  await toggleFavoriteRealEstate(param, (response) => {
    const isFavorite = response.data.isFavorite;
    if (isFavorite) favoriteRealEstateIds.value.add(realestate.id);
    else favoriteRealEstateIds.value.delete(realestate.id);
  });
};

// 알림 메시지 표시
const showAlert = (message) => {
  alertMessage.value = message;
  setTimeout(() => {
    alertMessage.value = null;
  }, 3000);
};

// props.realestates 변경 감지
watch(
  () => props.realestates,
  (newRealestates) => {
    initializeRealestates(newRealestates);
  },
  { immediate: true }
);

// props.houseType 변경 시 관심 목록 다시 불러오기
watch(
  () => props.houseType,
  () => {
    fetchFavorites();
  }
);

// 초기화
onMounted(() => {
  initializeRealestates();
  fetchFavorites();
});

// 이미지 경로 생성 함수
function getImagePath(randomIndex) {
  return `/assets/interior/livingroom/${randomIndex}.jpg`;
}

// 카드 클릭 핸들러
const handleCardClick = (realestate) => {
  emit("cardClick", { realestate, randomIndex: realestate.randomIndex });
};
</script>

<template>
  <div class="vertical-nav-content">
    <div
      class="house-card"
      v-for="realestate in realestatesWithRandomIndex"
      :key="realestate.id"
      @click="handleCardClick(realestate)"
    >
      <div class="house-image">
        <img
          :src="getImagePath(realestate.randomIndex)"
          alt="House"
          class="image"
        />
      </div>
      <div class="house-info">
        <p class="deal-space">{{ realestate.excluUseAr }}㎡</p>
        <p class="avg-deal-amount">
          <span v-if="realestate.dealCategory === '월세'">
            보증금 {{ realestate.dealAmount || "N/A" }}만 <br />
            월세 {{ realestate.feeAmount || "N/A" }}만
          </span>
          <span v-else>
            {{ realestate.dealCategory }}
            {{
              (
                parseFloat(realestate.dealAmount.replace(/,/g, "")) * 0.0001
              ).toFixed(2) || "N/A"
            }}억
          </span>
        </p>
      </div>
      <div class="house-favorite">
        <font-awesome-icon
          :icon="
            favoriteRealEstateIds.has(realestate.id)
              ? ['fas', 'heart']
              : ['far', 'heart']
          "
          class="heart-icon"
          :class="{ red: favoriteRealEstateIds.has(realestate.id) }"
          @click="handleFavoriteClick(realestate, $event)"
        ></font-awesome-icon>
      </div>
    </div>
    <div v-if="alertMessage" class="alert-message">
      {{ alertMessage }}
    </div>
  </div>
</template>

<style scoped>
.vertical-nav-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  background-color: #f9f9f9;
  overflow-y: auto;
  height: 100%; /* 스크롤 가능 영역을 유지 */
}

.house-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  position: relative;
  transition: transform 0.2s, box-shadow 0.2s;
}

.house-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.house-image {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.house-info {
  flex: 2;
  display: flex;
  flex-direction: column;
  padding-left: 16px;
}

.house-info h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 8px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.house-info p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 4px 0;
}

.deal-space {
  font-size: 14px;
  font-weight: bold;
  color: #3498db;
}

.avg-deal-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c;
}

/* 하트 아이콘 영역 */
.house-favorite {
  position: absolute;
  top: 16px;
  right: 16px;
  z-index: 1; /* 항상 표시되도록 설정 */
}

.house-favorite .heart-icon {
  font-size: 24px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.house-favorite .heart-icon.red {
  color: #e74c3c;
}

.house-favorite .heart-icon:hover {
  color: #ff6f61; /* 마우스 오버 시 강조 효과 */
}

/* 알림 메시지 스타일 */
.alert-message {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 14px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  animation: fadeInOut 3s ease-in-out;
}

/* 알림 메시지 페이드 효과 */
@keyframes fadeInOut {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  10% {
    opacity: 1;
    transform: translateY(0);
  }
  90% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(20px);
  }
}
</style>
