<template>
  <div class="profile-page">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon"><User /></el-icon>
        <div class="header-text">
          <h1>个人信息</h1>
          <p>查看个人基本信息</p>
        </div>
      </div>
    </div>

    <div class="glass card animate-in" v-loading="loading">
      <div class="section-header">
        <el-icon><User /></el-icon>
        <h3>基本信息</h3>
      </div>

      <div class="info-grid">
        <div class="info-item">
          <div class="info-label">姓名</div>
          <div class="info-value">{{ profile.name || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">手机号</div>
          <div class="info-value">{{ profile.mobile || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">学员编号</div>
          <div class="info-value">{{ profile.studentNo || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">报考车型</div>
          <div class="info-value">
            <el-tag v-if="profile.carType" type="success" size="small">{{ profile.carType }}</el-tag>
            <span v-else>-</span>
          </div>
        </div>
        <div class="info-item">
          <div class="info-label">已练学时</div>
          <div class="info-value">{{ profile.completedHours || 0 }} 小时</div>
        </div>
        <div class="info-item">
          <div class="info-label">要求学时</div>
          <div class="info-value">{{ profile.requiredHours || 0 }} 小时</div>
        </div>
      </div>

      <div class="tip-section">
        <el-icon><InfoFilled /></el-icon>
        <span>如需修改个人信息，请前往<router-link to="/app/student/settings" class="link">设置中心</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, InfoFilled } from '@element-plus/icons-vue'
import http from '../../api/http'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()
const loading = ref(false)

const profile = reactive({
  name: '',
  mobile: '',
  studentNo: '',
  carType: '',
  completedHours: 0,
  requiredHours: 0,
})

async function loadProfile() {
  try {
    loading.value = true
    const res = await http.get('/api/user/student/profile')
    if (res.data.code === 200) {
      const data = res.data.data
      profile.name = data.userName || ''
      profile.mobile = data.mobile || ''
      profile.studentNo = data.studentNo || ''
      profile.carType = data.carType || ''
      profile.completedHours = data.completedHours || 0
      profile.requiredHours = data.requiredHours || 0
    }
  } catch (error) {
    console.error('加载个人信息失败', error)
    ElMessage.error('加载个人信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 32px;
  color: #409eff;
}

.header-text h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.95);
}

.header-text p {
  margin: 4px 0 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.card {
  padding: 24px;
  border-radius: 12px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.section-header .el-icon {
  font-size: 20px;
  color: #409eff;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.info-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  font-weight: 500;
}

.info-value {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
}

.tip-section {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 24px;
  padding: 12px 16px;
  background: rgba(64, 158, 255, 0.08);
  border-radius: 8px;
  border: 1px solid rgba(64, 158, 255, 0.15);
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
}

.tip-section .el-icon {
  color: #409eff;
  font-size: 16px;
}

.link {
  color: #409eff;
  text-decoration: none;
  margin-left: 4px;
}

.link:hover {
  text-decoration: underline;
}

.animate-in {
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
