<script setup>
import { defineProps, defineEmits, ref, onMounted, watch } from "vue";
import "@/assets/main.css";
import { useUserStore } from "@/stores/userStore";
import { fetchUserFavoriteHouses, toggleFavorite } from "@/api/favorite"; // API 호출

// Props로 전달된 집 정보를 받습니다.
const props = defineProps({
  houses: {
    type: Array,
    default: () => [],
  },
  houseType: {
    type: String,
  },
});

function getImagePath(avgDealAmount, dealSpace, dealCategory) {
  let category = "";

  // 주택 타입
  let houseType = "";
  if (props.houseType == "apartment") {
    houseType = "apart";
  } else if (props.houseType == "villa") {
    houseType = "villa";
  } else if (props.houseType == "officetel") {
    houseType = "officetel";
  }

  // 평당 가격
  let amountPerSpace = avgDealAmount / dealSpace;

  if (dealCategory == "월세") {
    amountPerSpace = amountPerSpace * 10;
    console.log("amountPerSpace : ", amountPerSpace);
  }

  // 아파트 가격 기준으로 카테고리 설정
  if (amountPerSpace > 3000) {
    category = "high";
  } else if (amountPerSpace > 1000) {
    category = "mid";
  } else {
    category = "low";
  }

  const randomIndex = Math.floor(Math.random() * 20) + 1; // 1~20 범위의 숫자
  return `/assets/${houseType}/${category}${randomIndex}.jpg`;
}

// 부모로 이벤트를 전달하기 위한 emit 정의
const emit = defineEmits(["cardClick"]);
const userStore = useUserStore();

const favoriteAptSeqs = ref(new Set());
const alertMessage = ref(null);

// 카드 클릭 핸들러
const handleCardClick = (house) => {
  emit("cardClick", house); // 선택된 house 정보를 부모로 emit
};

// 관심 단지 불러오기
const fetchFavorites = async () => {
  if (userStore.isLogin) {
    const userNo = userStore.userInfo.userNo;
    await fetchUserFavoriteHouses({ userNo }, (response) => {
      favoriteAptSeqs.value = new Set(
        response.data
          .filter((item) => item.houseType === props.houseType)
          .map((item) => item.aptSeq)
      );
    });
  }
};

// 하트 클릭 핸들러
const handleFavoriteClick = async (house, event) => {
  event.stopPropagation();

  if (!userStore.isLogin) {
    showAlert("관심 단지 선택은 로그인 후 가능합니다.");
    return;
  }

  const param = {
    userNo: userStore.userInfo.userNo,
    aptSeq: house.aptSeq,
    houseType: props.houseType,
  };
  await toggleFavorite(param, (response) => {
    const isFavorite = response.data.isFavorite;
    if (isFavorite) favoriteAptSeqs.value.add(house.aptSeq);
    else favoriteAptSeqs.value.delete(house.aptSeq);
  });
};

// 알림 메시지 표시
const showAlert = (message) => {
  alertMessage.value = message;
  setTimeout(() => {
    alertMessage.value = null;
  }, 3000);
};

// 컴포넌트 마운트 시 관심 단지 불러오기
onMounted(() => {
  fetchFavorites();
});

// props.houseType이 변경될 때 fetchFavorites 실행
watch(
  () => props.houseType,
  () => {
    fetchFavorites();
  }
);
</script>

<template>
  <div class="vertical-nav-content">
    <div
      class="house-card"
      v-for="house in houses"
      :key="house.aptSeq"
      @click="handleCardClick(house)"
    >
      <div class="house-image">
        <img
          :src="
            getImagePath(
              house.avgDealAmount,
              house.dealSpace,
              house.dealCategory
            )
          "
          alt="House"
          class="image"
        />
      </div>
      <div class="house-info">
        <h3>{{ house.aptNm }}</h3>
        <p class="deal-space">{{ house.dealSpace }}평</p>
        <p class="avg-deal-amount">
          <span v-if="house.dealCategory == '월세'">
            보증금 {{ house.avgDealAmount || "N/A" }} / 월세
            {{ house.avgFeeAmount || "N/A" }}만
          </span>
          <span v-else>
            {{ house.dealCategory }}
            {{ (house.avgDealAmount * 0.0001).toFixed(2) }}억
          </span>
        </p>
      </div>
      <div class="house-favorite">
        <font-awesome-icon
          :icon="
            favoriteAptSeqs.has(house.aptSeq)
              ? ['fas', 'heart']
              : ['far', 'heart']
          "
          class="heart-icon"
          :class="{ red: favoriteAptSeqs.has(house.aptSeq) }"
          @click="handleFavoriteClick(house, $event)"
        ></font-awesome-icon>
      </div>
    </div>
    <div v-if="alertMessage" class="alert-message">
      {{ alertMessage }}
    </div>
  </div>
</template>

<style scoped>
.house-card {
  display: flex;
  position: relative;
  padding: 10px;
  background-color: #fff;
  margin-bottom: 10px;
  border-radius: 5px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.house-image {
  flex: 1;
}

.image {
  width: 160px;
  height: 160px;
}

.house-info {
  flex: 2;
  padding-left: 20px;
}

.house-info h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50; /* 어두운 파란색 톤 */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.house-info p {
  font-size: 14px;
  color: #7f8c8d; /* 회색 톤 */
  line-height: 1.5;
}

/* 거래 유형과 금액 강조 */
.avg-deal-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c; /* 붉은색 톤 */
}

.deal-space {
  font-size: 14px;
  font-weight: bold;
  color: #3498db; /* 밝은 파란색 */
}

.house-favorite {
  position: absolute;
  top: 10px;
  right: 10px;
}

.house-favorite i {
  font-size: 24px;
  cursor: pointer;
}

.house-favorite .red {
  color: #e74c3c;
}

.alert-message {
  position: fixed;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
