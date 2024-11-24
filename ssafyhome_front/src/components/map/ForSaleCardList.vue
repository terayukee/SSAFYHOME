<script setup>
import { ref, onMounted, watch } from "vue";

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

// 랜덤 인덱스 추가 함수
const initializeRealestates = () => {
  realestatesWithRandomIndex.value = props.realestates.map((realestate) => ({
    ...realestate,
    randomIndex: Math.floor(Math.random() * 20) + 1, // 1~20 범위의 랜덤 값 추가
  }));
};

// `props.realestates` 변경 감지
watch(
  () => props.realestates,
  (newRealestates) => {
    initializeRealestates(newRealestates); // 새롭게 랜덤 인덱스 추가
  },
  { immediate: true } // 초기화 시점에도 실행
);

// 초기화
onMounted(() => {
  initializeRealestates();
});

// 카드 클릭 핸들러
const handleCardClick = (realestate) => {
  emit("cardClick", { realestate, randomIndex: realestate.randomIndex }); // randomIndex 추가
};

// 이미지 경로 생성 함수
function getImagePath(randomIndex) {
  return `/assets/interior/livingroom/${randomIndex}.jpg`;
}
</script>

<template>
  <div class="vertical-nav-content">
    <div
      class="house-card"
      v-for="realestate in realestatesWithRandomIndex"
      :key="realestate.aptSeq"
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
        <h3>{{ realestate.aptNm }}</h3>
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
    </div>
  </div>
</template>

<style scoped>
.vertical-nav-content {
  display: flex;
  flex-direction: column;
  padding: 16px;
  background-color: #f9f9f9;
  overflow-y: scroll;
}

.house-card {
  display: flex;
  margin-bottom: 16px;
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.house-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
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
}

.house-info {
  flex: 2;
  padding-left: 16px;
}

.house-info h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50; /* 어두운 파란색 */
  margin-bottom: 8px;
}

.house-info p {
  font-size: 14px;
  color: #7f8c8d; /* 회색 톤 */
  margin: 4px 0;
}

.deal-space {
  font-size: 14px;
  font-weight: bold;
  color: #3498db; /* 밝은 파란색 */
}

.avg-deal-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c; /* 붉은색 */
}
</style>
