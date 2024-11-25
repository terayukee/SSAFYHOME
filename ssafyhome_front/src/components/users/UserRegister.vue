<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const router = useRouter();

const registerUser = ref({
  userName: "",
  email: "",
  userPassword: "",
  passwordConfirm: "",
  userNickname: "",
  phoneNumber: "",
  role: "USER", // 기본값을 개인고객으로 설정
});

const errors = ref({
  userName: "",
  email: "",
  userPassword: "",
  passwordConfirm: "",
  userNickname: "",
  phoneNumber: "",
});

const selectedTab = ref("USER"); // 기본 선택 탭

const switchRole = (role) => {
  selectedTab.value = role;
  registerUser.value.role = role; // 선택된 역할에 따라 role 설정
};

const validateForm = () => {
  let isValid = true;

  if (!registerUser.value.userName) {
    errors.value.userName = "이름을 입력해주세요";
    isValid = false;
  } else {
    errors.value.userName = "";
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(registerUser.value.email)) {
    errors.value.email = "올바른 이메일 형식이 아닙니다";
    isValid = false;
  } else {
    errors.value.email = "";
  }

  if (registerUser.value.userPassword.length < 8) {
    errors.value.userPassword = "비밀번호는 8자 이상이어야 합니다";
    isValid = false;
  } else {
    errors.value.userPassword = "";
  }

  if (registerUser.value.userPassword !== registerUser.value.passwordConfirm) {
    errors.value.passwordConfirm = "비밀번호가 일치하지 않습니다";
    isValid = false;
  } else {
    errors.value.passwordConfirm = "";
  }

  const phoneRegex = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
  if (!phoneRegex.test(registerUser.value.phoneNumber)) {
    errors.value.phoneNumber = "올바른 전화번호 형식이 아닙니다";
    isValid = false;
  } else {
    errors.value.phoneNumber = "";
  }

  return isValid;
};

const register = async () => {
  if (!validateForm()) return;

  try {
    await userStore.userRegister(registerUser.value);
    alert("회원가입이 완료되었습니다.");
    router.push("/login");
  } catch (error) {
    alert(
      error.response?.data?.message || "회원가입 처리 중 오류가 발생했습니다."
    );
    console.error("회원가입 실패:", error);
  }
};

const moveToLogin = () => {
  router.push("/login");
};
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 헤더 -->
      <div class="login-header">
        <h2>회원가입</h2>
        <p class="signup-text">
          이미 계정이 있으신가요?
          <a href="#" class="link" @click.prevent="moveToLogin">로그인</a>
        </p>
      </div>

      <!-- 폼 -->
      <form class="login-form" @submit.prevent="register">
        <!-- 탭 -->
        <div class="role-tabs">
          <div
            class="tab"
            :class="{ active: selectedTab === 'USER' }"
            @click="switchRole('USER')"
          >
            개인고객
          </div>
          <div
            class="tab"
            :class="{ active: selectedTab === 'BUSINESS' }"
            @click="switchRole('BUSINESS')"
          >
            기업고객
          </div>
        </div>
        <div class="form-group">
          <label for="userName">이름</label>
          <input
            id="userName"
            type="text"
            v-model="registerUser.userName"
            required
            placeholder="이름을 입력하세요"
          />
          <span class="error-message" v-if="errors.userName">{{
            errors.userName
          }}</span>
        </div>

        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            type="email"
            v-model="registerUser.email"
            required
            placeholder="example@email.com"
          />
          <span class="error-message" v-if="errors.email">{{
            errors.email
          }}</span>
        </div>

        <div class="form-group">
          <label for="userPassword">비밀번호</label>
          <input
            id="userPassword"
            type="password"
            v-model="registerUser.userPassword"
            required
            placeholder="비밀번호를 입력하세요"
          />
          <span class="error-message" v-if="errors.userPassword">{{
            errors.userPassword
          }}</span>
        </div>

        <div class="form-group">
          <label for="passwordConfirm">비밀번호 확인</label>
          <input
            id="passwordConfirm"
            type="password"
            v-model="registerUser.passwordConfirm"
            required
            placeholder="비밀번호를 다시 입력하세요"
          />
          <span class="error-message" v-if="errors.passwordConfirm">{{
            errors.passwordConfirm
          }}</span>
        </div>

        <div class="form-group">
          <label for="userNickname">닉네임</label>
          <input
            id="userNickname"
            type="text"
            v-model="registerUser.userNickname"
            required
            placeholder="닉네임을 입력하세요"
          />
          <span class="error-message" v-if="errors.userNickname">{{
            errors.userNickname
          }}</span>
        </div>

        <div class="form-group">
          <label for="phoneNumber">휴대폰 번호</label>
          <input
            id="phoneNumber"
            type="tel"
            v-model="registerUser.phoneNumber"
            required
            placeholder="010-0000-0000"
          />
          <span class="error-message" v-if="errors.phoneNumber">{{
            errors.phoneNumber
          }}</span>
        </div>

        <button type="submit" class="login-button">회원가입</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  /* background-color: #f5f5f5; */
  background-image: url("/assets/background/1.png");
  background-size: cover; /* 이미지를 요소 크기에 맞게 조정 */
  background-position: center; /* 이미지를 중앙에 배치 */
  background-repeat: no-repeat; /* 이미지 반복 방지 */
  position: relative;
}

.login-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3); /* 검정색, 투명도 조정 */
  z-index: 1;
  position: absolute;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.signup-text {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.link {
  color: #2c6bed;
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 탭 스타일 */
.role-tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  gap: 10px;
}

.tab {
  flex: 1;
  padding: 10px 20px;
  text-align: center;
  background-color: #f1f1f1;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
}

.tab.active {
  background-color: #2c6bed;
  color: white;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.tab:hover {
  background-color: #d8e6ff;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.form-group input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
  border-color: #2c6bed;
  box-shadow: 0 0 0 3px rgba(44, 107, 237, 0.1);
}

.userNickname-input {
  display: flex;
  gap: 8px;
}

.userNickname-input input {
  flex: 1;
}

.check-button {
  padding: 0 16px;
  background-color: #2c6bed;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.check-button:hover {
  background-color: #1a5ad1;
}

.error-message {
  font-size: 12px;
  color: #dc3545;
}

.login-button {
  background-color: #2c6bed;
  color: white;
  padding: 12px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 10px;
}

.login-button:hover {
  background-color: #1a5ad1;
}
</style>
