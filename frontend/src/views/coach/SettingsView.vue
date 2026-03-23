<template>
  <div class="settings-page">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon"><Setting /></el-icon>
        <div class="header-text">
          <h1>设置中心</h1>
          <p>管理个人信息与账户安全</p>
        </div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="settings-tabs">
      <el-tab-pane label="个人信息" name="profile">
        <div class="glass card animate-in">
          <div class="section-header">
            <el-icon><User /></el-icon>
            <h3>基本信息</h3>
          </div>
          <el-form :model="profileForm" label-width="100px" class="form" v-loading="loading">
            <el-row :gutter="16">
              <el-col :xs="24" :sm="12">
                <el-form-item label="姓名">
                  <el-input v-model="profileForm.name" placeholder="请输入姓名" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="手机号">
                  <el-input v-model="profileForm.mobile" placeholder="请输入手机号" />
                </el-form-item>
              </el-col>
            </el-row>

            <div class="actions">
              <el-button type="primary" @click="onSaveProfile" :loading="saving">保存修改</el-button>
              <el-button @click="onResetProfile">重置</el-button>
            </div>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="修改密码" name="password">
        <div class="glass card animate-in" style="--delay: 0.1s">
          <div class="section-header">
            <el-icon><Lock /></el-icon>
            <h3>密码安全</h3>
          </div>
          <el-form :model="passwordForm" label-width="100px" class="form" :rules="passwordRules" ref="passwordFormRef">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（8-20位，包含字母和数字）" show-password />
              <div class="form-tip">密码长度需8-20位，必须包含字母和数字</div>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>

            <div class="actions">
              <el-button type="primary" @click="onChangePassword" :loading="passwordLoading">修改密码</el-button>
              <el-button @click="onResetPassword">重置</el-button>
            </div>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Setting, User, Lock } from '@element-plus/icons-vue'
import http from '../../api/http'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()

const activeTab = ref('profile')
const loading = ref(false)
const saving = ref(false)

const profileForm = reactive({
  name: '',
  mobile: '',
})

async function loadProfile() {
  try {
    loading.value = true
    const res = await http.get('/api/user/coach/profile')
    if (res.data.code === 200) {
      const data = res.data.data
      profileForm.name = data.name || ''
      profileForm.mobile = data.mobile || ''
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (error) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

async function onSaveProfile() {
  if (!profileForm.name?.trim()) {
    ElMessage.warning('请输入姓名')
    return
  }
  if (!profileForm.mobile?.trim()) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(profileForm.mobile)) {
    ElMessage.warning('手机号格式不正确')
    return
  }
  try {
    saving.value = true
    const res = await http.put('/api/user/coach/profile', {
      name: profileForm.name,
      mobile: profileForm.mobile,
    })
    if (res.data.code === 200) {
      ElMessage.success('保存成功')
      authStore.profile.name = profileForm.name
      authStore.profile.mobile = profileForm.mobile
      authStore.persist()
    } else {
      ElMessage.error(res.data.msg || '保存失败')
    }
  } catch (error) {
    const msg = error.response?.data?.msg || error.response?.data?.message
    if (msg) {
      ElMessage.error(msg)
    } else {
      ElMessage.error('保存失败，请稍后重试')
    }
  } finally {
    saving.value = false
  }
}

function onResetProfile() {
  loadProfile()
}

const passwordFormRef = ref(null)
const passwordLoading = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入新密码'))
  } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(value)) {
    callback(new Error('密码长度需8-20位，包含字母和数字'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, validator: validatePassword, trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
}

async function onChangePassword() {
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    const res = await http.put('/api/user/coach/password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })
    if (res.data.code === 200) {
      ElMessage.success('密码修改成功')
      onResetPassword()
    } else {
      ElMessage.error(res.data.msg || '密码修改失败')
    }
  } catch (error) {
    const msg = error.response?.data?.msg || error.response?.data?.message
    if (msg) {
      ElMessage.error(msg)
    } else if (error !== false) {
      ElMessage.error('密码修改失败，请稍后重试')
    }
  } finally {
    passwordLoading.value = false
  }
}

function onResetPassword() {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.resetFields()
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.settings-page {
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

.settings-tabs {
  background: transparent;
}

.settings-tabs :deep(.el-tabs__header) {
  margin-bottom: 16px;
}

.settings-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.settings-tabs :deep(.el-tabs__item) {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

.settings-tabs :deep(.el-tabs__item.is-active) {
  color: #409eff;
}

.settings-tabs :deep(.el-tabs__active-bar) {
  background-color: #409eff;
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

.form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.72);
}

.form :deep(.el-input__wrapper),
.form :deep(.el-textarea__inner),
.form :deep(.el-select__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.form :deep(.el-input__inner) {
  color: rgba(255, 255, 255, 0.9);
}

.form-tip {
  margin-top: 6px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 24px;
}

.animate-in {
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
  animation-delay: var(--delay, 0s);
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
  .page-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .actions {
    flex-direction: column;
  }

  .actions .el-button {
    width: 100%;
  }
}
</style>
