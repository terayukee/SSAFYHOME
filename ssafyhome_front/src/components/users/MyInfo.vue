<template>
    <div class="my-page">
      <div class="tab-container">
        <div class="tab active">
          내 정보
        </div>
        <div class="tab">
          관심매물
        </div>
      </div>
  
      <div class="content-container" v-if="userInfo">  <!-- v-if 추가 -->
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
            <button 
              class="edit-button"
              @click="handleEdit('nickname')"
            >
              {{ isEditing.nickname ? '저장' : '수정' }}
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
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive, onBeforeUnmount } from 'vue'  // onBeforeUnmount 추가
  import { useUserStore } from '@/stores/userStore';
  import { storeToRefs } from "pinia"
  import { useRouter } from "vue-router"
  
  const userStore = useUserStore();
  const { userInfo } = storeToRefs(userStore);
  const { updateUserInfo , withdrawUser } = userStore;
  const router = useRouter();
  
  const isEditing = reactive({
    nickname: false
  });
  
  const editedInfo = reactive({
    userNickname: userInfo.value?.userNickname || ''  // optional chaining 추가
  });
  
  const handleEdit = async (field) => {
    if (field !== 'nickname') return;
  
    if (!isEditing.nickname) {
      isEditing.nickname = true;
      return;
    }
  
    try {
      const updatedData = {
        ...userInfo.value,
        userNickname: editedInfo.userNickname
      };
  
      await updateUserInfo(updatedData);
      isEditing.nickname = false;
    } catch (error) {
      console.error('닉네임 수정 실패:', error);
      editedInfo.userNickname = userInfo.value?.userNickname || '';  // 실패시 원래 값으로 복구
    }
  };

  const handleWithdraw = async () => {
    if (!confirm("정말 탈퇴하시겠습니까?")) {
      return;
    }

    try {
      await withdrawUser();
      alert("회원 탈퇴가 완료되었습니다.");
      router.push({ name: "login" });
    } catch (error) {
      alert("회원 탈퇴 처리 중 오류가 발생했습니다.");
      console.error(error);
    }
  };

  // 컴포넌트가 언마운트되기 전에 처리
  onBeforeUnmount(() => {
    if (!userInfo.value) {
      router.push({ name: "login" });
    }
  });
  </script>
  
  <style scoped>
  .my-page {
    width: 100%;
    max-width: 1024px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .tab-container {
    justify-content: center;
    display: flex;
    gap: 20px;
    margin-top: 50px;
    margin-bottom: 20px;
  }
  
  .tab {
    padding: 15px 40px;
    border: none;
    cursor: pointer;
    font-size: 16px;
    min-width: 200px;
    text-align: center;
    transition: all 0.3s ease;
    background: white;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    border-radius: 8px;
  }
  
  .tab.active {
    background-color: #333;
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  }
  
  .content-container {
    padding: 40px;
    margin-top: 50px;
    min-height: 500px;
  }
  
  .profile-info {
    background: white;
    border-radius: 16px;
    padding: 40px;
    max-width: 450px;
    margin: 0 auto;
    box-shadow: 0 4px 6px rgba(0,0,0,0.05);
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
  
  .verified-badge {
    background-color: #f8f8f8;
    color: #666;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
  }
  
  .arrow {
    color: #ddd;
    padding: 0 10px;
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