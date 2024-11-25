<script setup>
import { ref } from "vue";
import { getByAptNm } from "@/api/house"; // aptSeq, aptNm 검색 API 호출
import { registerRealEstate } from "@/api/realestate"; // 매물 등록 API 호출

// 검색 상태
const searchQuery = ref("");
const apartmentResults = ref([]);
const villaResults = ref([]);
const officetelResults = ref([]);
const selectedHouse = ref(null);
const selectedHouseType = ref("");

// 폼 데이터
const formData = ref({
  aptSeq: "",
  aptNm: "",
  excluUseAr: null,
  maxFloor: null,
  thisFloor: null,
  dealAmount: "",
  feeAmount: "",
  maintenanceCost: "",
  dealCategory: "",
  houseType: "",
});

// 주택 이름 검색
const fetchHouseNames = async () => {
  try {
    // 기존 검색 결과 초기화
    apartmentResults.value = [];
    villaResults.value = [];
    officetelResults.value = [];

    getByAptNm(
      {
        aptNm: searchQuery.value,
        houseType: "apartment",
      },
      (response) => {
        response.data.forEach((property) => {
          apartmentResults.value.push(property);
        });
      },
      (error) => {
        console.error("집 데이터를 가져오는 중 오류 발생:", error);
      }
    );
    getByAptNm(
      {
        aptNm: searchQuery.value,
        houseType: "villa",
      },
      (response) => {
        response.data.forEach((property) => {
          villaResults.value.push(property);
        });
      },
      (error) => {
        console.error("집 데이터를 가져오는 중 오류 발생:", error);
      }
    );
    getByAptNm(
      {
        aptNm: searchQuery.value,
        houseType: "officetel",
      },
      (response) => {
        response.data.forEach((property) => {
          officetelResults.value.push(property);
        });
      },
      (error) => {
        console.error("집 데이터를 가져오는 중 오류 발생:", error);
      }
    );
  } catch (error) {
    console.error("주택 이름 검색 실패:", error);
  }
};

// 주택 선택
const selectHouse = (aptSeq, type) => {
  let house = null;

  if (type === "apartment") {
    house = apartmentResults.value.find((result) => result.aptSeq === aptSeq);
    selectedHouseType.value = "아파트";
  } else if (type === "villa") {
    house = villaResults.value.find((result) => result.aptSeq === aptSeq);
    selectedHouseType.value = "빌라";
  } else if (type === "officetel") {
    house = officetelResults.value.find((result) => result.aptSeq === aptSeq);
    selectedHouseType.value = "오피스텔";
  }

  if (house) {
    console.log("선택한 주택: ", house);
    selectedHouse.value = house;
    formData.value.aptSeq = house.aptSeq;
    formData.value.aptNm = house.aptNm;
    formData.value.houseType = selectedHouseType;
    searchQuery.value = house.aptNm; // 선택한 주택 이름으로 검색어 설정
    console.log("selectedHouse: ", selectedHouse);
  }
};

// 매물 등록
const submitForm = async () => {
  try {
    const response = await registerRealEstate(formData.value);
    alert("매물이 성공적으로 등록되었습니다.");
    console.log("formData : ", formData);
  } catch (error) {
    console.error("매물 등록 실패:", error);
    alert("매물 등록 중 오류가 발생했습니다.");
  }
};
</script>

<template>
  <div class="real-estate-upload">
    <h1>부동산 매물 등록</h1>

    <!-- 주택 이름 검색 -->
    <div class="form-group search-box">
      <label for="searchAptNm">주택 이름 검색</label>
      <input
        id="searchAptNm"
        type="text"
        v-model.lazy="searchQuery"
        @change="fetchHouseNames"
        placeholder="주택 이름을 입력하세요"
      />
    </div>

    <!-- 검색 결과 -->
    <div class="search-results">
      <div class="result-column">
        <h3>아파트</h3>
        <select @change="(e) => selectHouse(e.target.value, 'apartment')">
          <option value="" disabled selected>아파트를 선택하세요</option>
          <option
            v-for="result in apartmentResults"
            :key="result.aptSeq"
            :value="result.aptSeq"
          >
            {{ result.aptNm }} - {{ result.roadNm }} {{ result.umdNm }}
          </option>
        </select>
      </div>

      <div class="result-column">
        <h3>빌라</h3>
        <select @change="(e) => selectHouse(e.target.value, 'villa')">
          <option value="" disabled selected>빌라를 선택하세요</option>
          <option
            v-for="result in villaResults"
            :key="result.aptSeq"
            :value="result.aptSeq"
          >
            {{ result.aptNm }} - {{ result.roadNm }} {{ result.umdNm }}
          </option>
        </select>
      </div>

      <div class="result-column">
        <h3>오피스텔</h3>
        <select @change="(e) => selectHouse(e.target.value, 'officetel')">
          <option value="" disabled selected>오피스텔을 선택하세요</option>
          <option
            v-for="result in officetelResults"
            :key="result.aptSeq"
            :value="result.aptSeq"
          >
            {{ result.aptNm }} - {{ result.roadNm }} {{ result.umdNm }}
          </option>
        </select>
      </div>
    </div>

    <!-- 선택된 주택 정보 -->
    <div v-if="selectedHouse" class="selected-house">
      <h3>선택된 주택</h3>
      <p><strong>이름:</strong> {{ selectedHouse.aptNm }}</p>
      <p>
        <strong>주소:</strong> {{ selectedHouse.roadNm }}
        {{ selectedHouse.umdNm }}
      </p>
      <p><strong>종류:</strong> {{ selectedHouseType }}</p>
    </div>

    <!-- 매물 정보 입력 -->
    <form @submit.prevent="submitForm" class="form-details">
      <div class="form-row">
        <div class="form-group">
          <label for="excluUseAr">전용 면적 (㎡)</label>
          <input
            id="excluUseAr"
            type="text"
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
      </div>

      <div class="form-row">
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
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="maxFloor">최고 층수</label>
          <input
            id="maxFloor"
            type="number"
            v-model="formData.maxFloor"
            placeholder="최고 층수를 입력하세요"
          />
        </div>

        <div class="form-group">
          <label for="thisFloor">현재 층수</label>
          <input
            id="thisFloor"
            type="number"
            v-model="formData.thisFloor"
            placeholder="현재 층수를 입력하세요"
          />
        </div>
      </div>

      <div class="form-group">
        <label>거래 유형</label>
        <div class="radio-group">
          <label>
            <input
              type="radio"
              value="매매"
              v-model="formData.dealCategory"
              required
            />
            매매
          </label>
          <label>
            <input
              type="radio"
              value="전세"
              v-model="formData.dealCategory"
              required
            />
            전세
          </label>
          <label>
            <input
              type="radio"
              value="월세"
              v-model="formData.dealCategory"
              required
            />
            월세
          </label>
        </div>
      </div>

      <button type="submit">매물 등록</button>
    </form>
  </div>
</template>

<style scoped>
.real-estate-upload {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px;
  background-color: #ffffff;
  border: 1px solid #e3f2fd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  font-family: "Roboto", Arial, sans-serif;
}

h1 {
  text-align: center;
  font-size: 28px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 25px;
  border-bottom: 2px solid #e3f2fd;
  padding-bottom: 10px;
}

.search-box {
  margin-bottom: 20px;
}

.search-box label {
  font-size: 16px;
  font-weight: bold;
  color: #555;
}

input,
select,
button {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

input:focus,
select:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
  outline: none;
}

.search-results {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 20px;
}

.result-column {
  flex: 1;
  text-align: center;
}

.result-column h3 {
  margin-bottom: 10px;
  font-size: 18px;
  color: #007bff;
}

.result-column select {
  height: 40px;
}

.selected-house {
  background: #f9f9f9;
  border: 1px solid #e3f2fd;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.selected-house h3 {
  font-size: 20px;
  color: #007bff;
  margin-bottom: 10px;
}

.selected-house p {
  margin: 5px 0;
  font-size: 16px;
  color: #333;
}

.form-details {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  width: 775px;
}

.form-group label {
  font-size: 16px;
  font-weight: bold;
  color: #555;
  margin-bottom: 5px;
  display: block;
}

.radio-group {
  display: flex;
  gap: 30px;
}

.radio-group label {
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #555;
}

.radio-group input[type="radio"] {
  margin-right: 8px;
  accent-color: #007bff;
  width: 20px;
}

button {
  background-color: #007bff;
  color: #fff;
  padding: 12px 15px;
  font-size: 18px;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

button:hover {
  background-color: #0056b3;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
}
</style>
