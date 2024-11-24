<template>
    <div class="my-page">
      

      <!-- 네비게이션 메뉴 -->
      <nav class="nav-menu">
        <div class="menu-item active">내 정보</div>
      </nav>
  
      <!-- 프로필 섹션 -->
      <div class="profile-section">


        <!-- 프로필 정보 -->
        <div class="profile-info">
  
          <div class="info-row">
            <span class="label">닉네임</span>
            <span class="value">심플한 발란스</span>
            <span class="action-link">></span>
          </div>
  
          <div class="info-row">
            <span class="label">이름</span>
            <span class="value">박준현</span>
            <span class="action-link">></span>
          </div>
  
          <div class="info-row">
            <span class="label">휴대폰번호</span>
            <div class="value">
              <span>010-3736-9910</span>
            </div>
            <span class="action-link">></span>
          </div>
        </div>
  
        <!-- 회원탈퇴 링크 -->
        <div class="withdrawal-link">
          <a href="#" @click="handleWithdrawal">회원탈퇴</a>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MyPage',
    data() {
      return {
        userProfile: {
          nickname: '심플한 발란스',
          name: '박준현',
          phone: '010-3736-9910',
          isVerified: true,
          hasSocialAccount: true
        }
      }
    },
    methods: {
      handleWithdrawal() {
        // 회원탈퇴 로직 구현
        if (confirm('정말 탈퇴하시겠습니까?')) {
          console.log('회원탈퇴 처리')
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .my-page {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .page-title {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 30px;
  }
  
  .nav-menu {
    display: flex;
    border-bottom: 1px solid #eee;
    margin-bottom: 40px;
  }
  
  .menu-item {
    padding: 15px 30px;
    cursor: pointer;
  }
  
  .menu-item.active {
    background-color: #222;
    color: white;
  }
  
  .profile-section {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  }
  
  .profile-image {
    width: 100px;
    height: 100px;
    margin: 0 auto 30px;
  }
  
  .default-profile {
    width: 100%;
    height: 100%;
  }
  
  .profile-info {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .info-row {
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #eee;
  }
  
  .label {
    width: 150px;
    color: #666;
  }
  
  .value {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .social-icon {
    width: 24px;
    height: 24px;
  }
  
  .verification-badge {
    background-color: #e1e1e1;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 12px;
  }
  
  .action-link {
    color: #999;
    cursor: pointer;
  }
  
  .withdrawal-link {
    text-align: right;
    margin-top: 30px;
  }
  
  .withdrawal-link a {
    color: #999;
    text-decoration: none;
    font-size: 14px;
  }
  </style>