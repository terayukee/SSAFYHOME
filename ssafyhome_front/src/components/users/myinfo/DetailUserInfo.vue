<template>
  <div class="profile-info">
    <div class="info-row">
      <div class="label">닉네임</div>
      <div v-if="!isEditing.nickname" class="value">
        {{ userInfo.userNickname }}
      </div>
      <input
        v-else
        v-model="editedInfo.userNickname"
        class="edit-input"
        type="text"
      />
      <button class="edit-button" @click="handleEdit('nickname')">
        {{ isEditing.nickname ? "저장" : "수정" }}
      </button>
    </div>

    <div class="info-row">
      <div class="label">이름</div>
      <div class="value">
        {{ userInfo.userName }}
      </div>
    </div>

    <div class="info-row">
      <div class="label">이메일</div>
      <div class="value">
        {{ userInfo.email }}
      </div>
    </div>

    <div class="info-row">
      <div class="label">휴대폰번호</div>
      <div class="value">
        {{ userInfo.phoneNumber }}
      </div>
    </div>

    <div class="withdrawal">
      <button @click="handleWithdraw" class="withdrawal-button">
        회원탈퇴
      </button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();
const { userInfo } = userStore;
const { updateUserInfo, withdrawUser } = userStore;

const isEditing = reactive({
  nickname: false,
});

const editedInfo = reactive({
  userNickname: userInfo.userNickname || "",
});

const handleEdit = async (field) => {
  if (field !== "nickname") return;

  if (!isEditing.nickname) {
    isEditing.nickname = true;
    return;
  }

  try {
    const updatedData = {
      ...userInfo,
      userNickname: editedInfo.userNickname,
    };

    await updateUserInfo(updatedData);
    isEditing.nickname = false;
  } catch (error) {
    console.error("닉네임 수정 실패:", error);
    editedInfo.userNickname = userInfo.userNickname || "";
  }
};

const handleWithdraw = async () => {
  if (!confirm("정말 탈퇴하시겠습니까?")) {
    return;
  }

  try {
    await withdrawUser();
    alert("회원 탈퇴가 완료되었습니다.");
  } catch (error) {
    alert("회원 탈퇴 처리 중 오류가 발생했습니다.");
    console.error(error);
  }
};
</script>

<style scoped>
/* 위 CSS 코드 중 profile-info 관련 스타일만 복사 */
.profile-info {
  background: white;
  border-radius: 16px;
  padding: 40px;
  max-width: 450px;
  margin: 0 auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.info-row {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
  min-height: 60px;
}

.label {
  width: 120px;
  color: #333;
  font-size: 15px;
  font-weight: 500;
}

.value {
  flex: 1;
  font-size: 15px;
  color: #666;
}

.withdrawal {
  text-align: right;
  margin-top: 40px;
}

.withdrawal-button {
  color: #999;
  background: none;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.withdrawal-button:hover {
  background-color: #f8f8f8;
}

.edit-button {
  padding: 6px 12px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 10px;
  transition: all 0.3s ease;
}

.edit-button:hover {
  background-color: #555;
}

.edit-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 15px;
  color: #333;
  margin-right: 10px;
}

.edit-input:focus {
  outline: none;
  border-color: #333;
  box-shadow: 0 0 0 2px rgba(51, 51, 51, 0.1);
}
</style>
