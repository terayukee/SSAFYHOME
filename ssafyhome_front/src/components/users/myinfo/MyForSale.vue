<script setup>
import { ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/userStore";
import { getByUserNo, getById } from "@/api/realestate";
import { getBySeq } from "@/api/house";

// Pinia Store에서 사용자 정보 가져오기
const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore);

// 매물 데이터를 저장할 상태
const forSaleProperties = ref([]);

// 에러 메시지 상태
const errorMessage = ref("");

// 등록 매물 데이터 가져오기
const fetchForSaleProperties = () => {
  forSaleProperties.value = []; // 데이터 초기화
  errorMessage.value = ""; // 에러 메시지 초기화

  getByUserNo(
    { registerUserNo: userInfo.value.userNo },
    (response) => {
      console.log("response.data : ", response.data);
      try {
        // 응답 데이터 처리
        response.data.forEach((property) => {
          console.log("property : ", property);

          // 기본 데이터 추가
          const realEstateInfo = {
            id: property.id,
            aptSeq: property.aptSeq,
            maxFloor: property.maxFloor,
            thisFloor: property.thisFloor,
            registerYear: property.registerYear,
            registerMonth: property.registerMonth,
            registerDay: property.registerDay,
            excluUseAr: property.excluUseAr,
            dealAmount: property.dealAmount,
            feeAmount: property.feeAmount,
            maintenanceCost: property.maintenanceCost,
            dealCategory: property.dealCategory,
            houseType: property.houseType,
            image: `/assets/interior/livingroom/${
              Math.floor(Math.random() * 20) + 1
            }.jpg`,
            aptNm: "불러오는 중...", // 기본값
          };

          // forSaleProperties에 일단 추가
          forSaleProperties.value.push(realEstateInfo);

          // `getBySeq`로 `aptNm` 가져오기
          getBySeq(
            { aptSeq: property.aptSeq, houseType: property.houseType },
            (houseResponse) => {
              if (houseResponse && houseResponse.data) {
                realEstateInfo.aptNm = houseResponse.data.aptNm || "알 수 없음";
              } else {
                realEstateInfo.aptNm = "알 수 없음";
              }
              // 업데이트된 `aptNm` 반영
              forSaleProperties.value = [...forSaleProperties.value];
            },
            (error) => {
              console.error(
                `Failed to fetch aptNm for aptSeq ${property.aptSeq}:`,
                error
              );
              realEstateInfo.aptNm = "알 수 없음";
              // 에러 처리 후 업데이트
              forSaleProperties.value = [...forSaleProperties.value];
            }
          );
        });

        console.log("forSaleProperties : ", forSaleProperties.value);
      } catch (error) {
        console.error("매물 데이터를 처리하는 중 오류 발생:", error);
        errorMessage.value = "매물을 가져오는 데 실패했습니다.";
      }
    },
    (error) => {
      console.error("매물 데이터를 가져오는 중 오류 발생:", error);
      errorMessage.value = "매물을 가져오는 데 실패했습니다.";
    }
  );
};

// 컴포넌트 마운트 시 매물 데이터 가져오기
onMounted(() => {
  fetchForSaleProperties();
});
</script>

<template>
  <div class="my-for-sale">
    <!-- 에러 메시지 -->
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <!-- 매물 리스트 -->
    <div v-else-if="forSaleProperties.length > 0" class="property-list">
      <div
        v-for="property in forSaleProperties"
        :key="property.id"
        class="property-card"
      >
        <img :src="property.image" alt="매물 이미지" class="property-image" />
        <div class="property-info">
          <h3 class="property-name">{{ property.aptNm }}</h3>
          <p class="property-size">{{ property.excluUseAr }}㎡</p>
          <p class="property-price">
            {{ property.dealCategory }}
            <template v-if="property.dealCategory === '월세'">
              {{ property.dealAmount || "N/A" }}만원 /
              {{ property.feeAmount || "N/A" }}만원
            </template>
            <template v-else>
              {{
                property.dealAmount
                  ? (
                      parseFloat(property.dealAmount.replace(/,/g, "")) * 0.0001
                    ).toFixed(2) + "억"
                  : "N/A"
              }}
            </template>
          </p>
          <p class="property-maintenance">
            관리비: {{ property.maintenanceCost || "N/A" }}만원
          </p>
        </div>
      </div>
    </div>

    <!-- 매물 없음 -->
    <p v-else>등록된 매물이 없습니다.</p>
  </div>
</template>

<style scoped>
.my-for-sale {
  padding: 20px;
}

h2 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.error-message {
  color: red;
  font-size: 14px;
}

.property-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.property-card {
  flex: 0 0 300px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.property-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.property-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.property-info {
  padding: 15px;
}

.property-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.property-size,
.property-price,
.property-deal-category,
.property-maintenance {
  font-size: 14px;
  margin-bottom: 5px;
  color: #555;
}
</style>
