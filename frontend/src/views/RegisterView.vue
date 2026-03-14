<template>
  <div class="page register">
    <div class="panel glass">
      <div class="header">
        <div class="title">学员注册</div>
        <div class="desc">注册后使用手机号+密码登录</div>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="form">
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" clearable />
        </el-form-item>

        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password clearable />
        </el-form-item>

        <div class="pw-meter">
          <div class="pw-meta">
            <div class="pw-text">密码强度：{{ strengthLabel }}</div>
            <div class="pw-rules">8-20位，必须包含字母和数字</div>
          </div>
          <el-progress :percentage="strengthPercent" :status="strengthStatus" :stroke-width="10" />
        </div>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" placeholder="请再次输入密码" show-password clearable />
        </el-form-item>

        <el-form-item label="验证码" prop="captchaCode">
          <div class="captcha-row">
            <el-input v-model="form.captchaCode" placeholder="请输入验证码" clearable />
            <div class="captcha-box" @click="refreshCaptcha" title="点击刷新">
              <img v-if="captchaImage" :src="captchaImage" alt="captcha" />
              <div v-else class="captcha-loading">加载中</div>
            </div>
          </div>
        </el-form-item>

        <div class="actions">
          <el-button type="primary" class="btn" @click="onRegister">注册</el-button>
          <el-button class="btn" @click="goLogin">返回登录</el-button>
        </div>
      </el-form>
    </div>

    <div class="bg-grid"></div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '../api/http'

const router = useRouter()

const formRef = ref()

const form = reactive({
  mobile: '',
  userName: '',
  password: '',
  confirmPassword: '',
  captchaId: '',
  captchaCode: '',
})

const captchaImage = ref('')

const strengthPercent = computed(() => {
  const p = form.password || ''
  let score = 0
  if (p.length >= 8) score += 30
  if (p.length >= 12) score += 10
  if (/[A-Za-z]/.test(p)) score += 30
  if (/\d/.test(p)) score += 30
  return Math.min(100, score)
})

const strengthLabel = computed(() => {
  const s = strengthPercent.value
  if (s >= 80) return '强'
  if (s >= 60) return '中'
  if (s >= 30) return '弱'
  return '很弱'
})

const strengthStatus = computed(() => {
  const s = strengthPercent.value
  if (s >= 80) return 'success'
  if (s >= 60) return 'warning'
  return 'exception'
})

const rules = {
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/, message: '密码长度需8-20位，包含字母和数字', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_, val, cb) => {
        if (val !== form.password) cb(new Error('两次输入的密码不一致'))
        else cb()
      },
      trigger: 'blur',
    },
  ],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

async function refreshCaptcha() {
  try {
    const res = await http.get('/api/captcha/image')
    if (res?.data?.code !== 200) {
      ElMessage.error(res?.data?.msg || '获取验证码失败')
      return
    }
    form.captchaId = res.data.data.captchaId
    captchaImage.value = res.data.data.imageBase64
    form.captchaCode = ''
  } catch {
    ElMessage.error('获取验证码失败')
  }
}

function goLogin() {
  router.replace({ name: 'login' })
}

async function onRegister() {
  await formRef.value?.validate()

  try {
    const payload = {
      mobile: form.mobile,
      userName: form.userName,
      password: form.password,
      captchaId: form.captchaId,
      captchaCode: form.captchaCode,
    }
    const res = await http.post('/api/user/student/register', payload)
    if (res?.data?.code !== 200) {
      ElMessage.error(res?.data?.msg || '注册失败')
      await refreshCaptcha()
      return
    }

    ElMessage.success('注册成功，请登录')
    router.replace({ name: 'login' })
  } catch {
    ElMessage.error('注册失败')
    await refreshCaptcha()
  }
}

onMounted(refreshCaptcha)
</script>

<style scoped>
.register {
  position: relative;
  align-items: center;
  justify-content: center;
  padding: 28px 18px;
}

.panel {
  width: 100%;
  max-width: 520px;
  padding: 22px;
  position: relative;
  z-index: 2;
}

.header {
  margin-bottom: 18px;
}

.title {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
  letter-spacing: 0.6px;
  font-size: 18px;
}

.desc {
  margin-top: 8px;
  color: rgba(255, 255, 255, 0.62);
  font-size: 12px;
}

.form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.75);
}

.form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.form :deep(.el-input__inner) {
  color: rgba(255, 255, 255, 0.88);
}

.actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 4px;
}

.btn {
  flex: 1;
}

.pw-meter {
  margin: -4px 0 12px;
  padding: 12px 12px 10px;
  border: 1px dashed rgba(255, 255, 255, 0.14);
  border-radius: 12px;
}

.pw-meta {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 8px;
}

.pw-text {
  color: rgba(255, 255, 255, 0.82);
  font-size: 12px;
}

.pw-rules {
  color: rgba(255, 255, 255, 0.55);
  font-size: 12px;
}

.captcha-row {
  display: grid;
  grid-template-columns: 1fr 130px;
  gap: 10px;
  align-items: center;
}

.captcha-box {
  height: 40px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.10);
  background: rgba(255, 255, 255, 0.06);
  display: grid;
  place-items: center;
  cursor: pointer;
}

.captcha-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.captcha-loading {
  color: rgba(255, 255, 255, 0.65);
  font-size: 12px;
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(255, 255, 255, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.06) 1px, transparent 1px);
  background-size: 32px 32px;
  mask-image: radial-gradient(circle at center, black 40%, transparent 70%);
  opacity: 0.35;
}
</style>
