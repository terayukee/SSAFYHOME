<script setup>
import { ref } from "vue";
import { getBySeq } from "@/api/house"; // aptSeq, aptNm 검색 API 호출
import { registerRealEstate } from "@/api/realestate"; // 매물 등록 API 호출

// 검색 상태
const searchQuery = ref("");
const searchResults = ref([]);
const selectedHouse = ref(null);

// 폼 데이터
const formData = ref({
  aptSeq: "",
  aptNm: "",
  excluUseAr: null,
  dealAmount: "",
  feeAmount: "",
  maintenanceCost: "",
  dealCategory: "",
  houseType: "",
});

// 주택 이름 검색
const fetchHouseNames = async () => {
  if (searchQuery.value.trim().length === 0) {
    searchResults.value = [];
    return;
  }

  try {
    // 각각의 houseType별 검색 결과
    const [apartmentResponse, villaResponse, officetelResponse] =
      await Promise.all([
        getBySeq({ aptNm: searchQuery.value, houseType: "apartment" }),
        getBySeq({ aptNm: searchQuery.value, houseType: "villa" }),
        getBySeq({ aptNm: searchQuery.value, houseType: "officetel" }),
      ]);

    // 검색 결과를 하나의 배열로 합치기
    searchResults.value = [
      ...(apartmentResponse.data || []),
      ...(villaResponse.data || []),
      ...(officetelResponse.data || []),
    ];
  } catch (error) {
    console.error("주택 이름 검색 실패:", error);
  }
};

// 주택 선택
const selectHouse = (house) => {
  selectedHouse.value = house;
  formData.value.aptSeq = house.aptSeq;
  formData.value.aptNm = house.aptNm;
  searchResults.value = [];
  searchQuery.value = house.aptNm; // 선택한 주택 이름으로 검색어 설정
};

// 매물 등록
const submitForm = async () => {
  try {
    const response = await registerRealEstate(formData.value);
    alert("매물이 성공적으로 등록되었습니다.");
    // 폼 초기화
    formData.value = {
      aptSeq: "",
      aptNm: "",
      excluUseAr: null,
      dealAmount: "",
      feeAmount: "",
      maintenanceCost: "",
      dealCategory: "",
      houseType: "",
    };
    selectedHouse.value = null;
    searchQuery.value = "";
  } catch (error) {
    console.error("매물 등록 실패:", error);
    alert("매물 등록 중 오류가 발생했습니다.");
  }
};
</script>

<template>
  <div class="real-estate-upload">
    <h2>부동산 매물 등록</h2>

    <!-- 주택 이름 검색 -->
    <div class="form-group">
      <label for="searchAptNm">주택 이름</label>
      <input
        id="searchAptNm"
        type="text"
        v-model.lazy="searchQuery"
        @change="fetchHouseNames"
        placeholder="주택 이름을 입력하세요"
      />
      <ul v-if="searchResults.length > 0" class="search-results">
        <li
          v-for="result in searchResults"
          :key="result.aptSeq"
          @click="selectHouse(result)"
          class="search-result"
        >
          {{ result.aptNm }}
        </li>
      </ul>
    </div>

    <!-- 선택된 주택 정보 -->
    <div v-if="selectedHouse">
      <p><strong>선택된 주택:</strong> {{ selectedHouse.aptNm }}</p>
      <p><strong>aptSeq:</strong> {{ selectedHouse.aptSeq }}</p>
    </div>

    <!-- 매물 정보 입력 -->
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="excluUseAr">전용 면적 (㎡)</label>
        <input
          id="excluUseAr"
          type="number"
          v-model="formData.excluUseAr"
          placeholder="전용 면적을 입력하세요"
          required
        />
      </div>

      <div class="form-group">
        <label for="dealAmount">거래 금액 (만원)</label>
        <input
          id="dealAmount"
          type="text"
          v-model="formData.dealAmount"
          placeholder="거래 금액을 입력하세요"
          required
        />
      </div>

      <div class="form-group">
        <label for="feeAmount">월세 (만원)</label>
        <input
          id="feeAmount"
          type="text"
          v-model="formData.feeAmount"
          placeholder="월세를 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="maintenanceCost">관리비 (만원)</label>
        <input
          id="maintenanceCost"
          type="text"
          v-model="formData.maintenanceCost"
          placeholder="관리비를 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="dealCategory">거래 유형</label>
        <select id="dealCategory" v-model="formData.dealCategory" required>
          <option value="매매">매매</option>
          <option value="전세">전세</option>
          <option value="월세">월세</option>
        </select>
      </div>

      <div class="form-group">
        <label for="houseType">주택 유형</label>
        <select id="houseType" v-model="formData.houseType" required>
          <option value="아파트">아파트</option>
          <option value="빌라">빌라</option>
          <option value="오피스텔">오피스텔</option>
        </select>
      </div>

      <button type="submit">매물 등록</button>
    </form>
  </div>
</template>

<style scoped>
.real-estate-upload {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

input,
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.search-results {
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-top: 5px;
  list-style: none;
  padding: 0;
}

.search-result {
  padding: 10px;
  cursor: pointer;
}

.search-result:hover {
  background-color: #f0f0f0;
}

button {
  background-color: #007bff;
  color: #fff;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
