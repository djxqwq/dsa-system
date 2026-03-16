<template>
  <div class="wrap">
    <div class="glass card">
      <div class="head">
        <div class="h">个人信息</div>
        <div class="s">维护教练资料与联系方式</div>
      </div>

      <el-form :model="form" label-width="96px" class="form" v-loading="loading">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="手机号">
              <el-input v-model="form.mobile" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="工号">
              <el-input v-model="form.coachNo" placeholder="请输入工号" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="actions">
          <el-button type="primary" @click="onSave" :loading="saving">保存</el-button>
          <el-button @click="onReset">重置</el-button>
        </div>
      </el-form>
    </div>

    <div class="glass card">
      <div class="head">
        <div class="h">修改密码</div>
        <div class="s">定期修改密码可以提高账户安全性</div>
      </div>

      <el-form :model="passwordForm" label-width="96px" class="form" :rules="passwordRules" ref="passwordFormRef">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
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
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../../api/http'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()
const loading = ref(false)
const saving = ref(false)

const form = reactive({
  name: '',
  mobile: '',
  coachNo: '',
})

async function loadProfile() {
  loading.value = true
  try {
    const res = await http.get('/api/user/coach/profile')
    if (res.data.code === 200) {
      const data = res.data.data
      form.name = data.name || ''
      form.mobile = data.mobile || ''
      form.coachNo = data.coachNo || ''
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (error) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

async function onSave() {
  if (!form.name?.trim()) {
    ElMessage.warning('请输入姓名')
    return
  }
  if (!form.mobile?.trim()) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(form.mobile)) {
    ElMessage.warning('手机号格式不正确')
    return
  }
  try {
    saving.value = true
    const res = await http.put('/api/user/coach/profile', {
      name: form.name,
      mobile: form.mobile,
      coachNo: form.coachNo,
    })
    if (res.data.code === 200) {
      ElMessage.success('保存成功')
      authStore.profile.name = form.name
      authStore.profile.mobile = form.mobile
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

function onReset() {
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
.wrap {
  display: grid;
  gap: 16px;
}

.card {
  padding: 18px;
}

.head {
  margin-bottom: 14px;
}

.h {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
}

.s {
  margin-top: 6px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.72);
}

.form :deep(.el-input__wrapper),
.form :deep(.el-select__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
