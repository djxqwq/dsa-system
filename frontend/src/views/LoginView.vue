<template>
  <div class="page login">
    <div class="panel glass">
      <div class="header">
        <div class="title">驾校练车预约数字化管理系统</div>
        <div class="desc">科技感界面 · 统一入口 · 多角色协同</div>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="form">
        <el-form-item label="登录角色" prop="role">
          <el-segmented v-model="form.role" :options="roleOptions" />
        </el-form-item>

        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password clearable />
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
          <el-button type="primary" class="btn" @click="onLogin">登录</el-button>
          <el-button text class="hint" @click="goRegister">去注册</el-button>
        </div>

        <div v-if="isDev" class="dev-entry">
          <el-button type="success" plain size="small" :loading="devLoginLoading" @click="devAdminLogin">
            开发：一键管理员登录
          </el-button>
        </div>

        <div class="tips">
          <div class="tip">学员端：预约练车、查学时、看历史</div>
          <div class="tip">教练端：排班、确认预约、学时记录</div>
          <div class="tip">管理员端：用户/教练/车辆/统计/配置</div>
        </div>
      </el-form>
    </div>

    <div class="bg-grid"></div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import http from '../api/http'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const formRef = ref()
const isDev = import.meta.env.DEV
const devLoginLoading = ref(false)

const form = reactive({
  role: 'student',
  mobile: '',
  password: '',
  captchaId: '',
  captchaCode: '',
})

const captchaImage = ref('')

const roleOptions = [
  { label: '学员', value: 'student' },
  { label: '教练', value: 'coach' },
  { label: '管理员', value: 'admin' },
]

function validateMobile(rule, value, callback) {
  if (!value) {
    callback(new Error('请输入手机号'))
    return
  }
  if (form.role !== 'admin' && !/^1\d{10}$/.test(value)) {
    callback(new Error('手机号格式不正确'))
    return
  }
  callback()
}

const rules = {
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  mobile: [
    { validator: validateMobile, trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

watch(
  () => form.role,
  () => {
    formRef.value?.clearValidate?.(['mobile'])
  },
)

function goRegister() {
  router.replace({ name: 'register' })
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

async function onLogin() {
  await formRef.value?.validate()

  const apiPath =
    form.role === 'coach'
      ? '/api/user/coach/login'
      : form.role === 'admin'
        ? '/api/user/admin/login'
        : '/api/user/student/login'

  try {
    const res = await http.post(apiPath, {
      mobile: form.mobile,
      password: form.password,
      captchaId: form.captchaId,
      captchaCode: form.captchaCode,
    })

    if (res?.data?.code !== 200) {
      ElMessage.error(res?.data?.msg || '登录失败')
      await refreshCaptcha()
      return
    }

    const data = res.data.data || {}
    const token = data.token || ''
    if (!token) {
      ElMessage.error('登录失败：未返回token')
      await refreshCaptcha()
      return
    }

    auth.login({
      token,
      role: form.role,
      profile: {
        name:
          form.role === 'student'
            ? data.userName || '学员'
            : form.role === 'coach'
              ? data.name || '教练'
              : data.name || '管理员',
        mobile: form.mobile,
      },
    })

    ElMessage.success('登录成功')
  } catch {
    ElMessage.error('登录失败')
    await refreshCaptcha()
    return
  }

  const redirect = route.query.redirect
  if (typeof redirect === 'string' && redirect.startsWith('/')) {
    router.replace(redirect)
    return
  }
  router.replace(auth.homePath)
}

const DEV_ADMIN_SECRET = 'dev-admin-2025'

async function devAdminLogin() {
  devLoginLoading.value = true
  try {
    const res = await http.post('/api/user/admin/dev-login', { secret: DEV_ADMIN_SECRET })
    const code = res?.data?.code
    const ok = code === 200 || code === '200'
    if (!ok) {
      const msg = res?.data?.msg || '一键登录失败'
      console.error('[一键登录] 后端返回异常', res?.data)
      ElMessage.error(msg)
      return
    }
    const data = res.data.data || {}
    const token = data.token || ''
    if (!token) {
      console.error('[一键登录] 未返回 token', res?.data)
      ElMessage.error('登录失败：未返回 token')
      return
    }
    auth.login({
      token,
      role: 'admin',
      profile: {
        name: data.name || '管理员',
        mobile: '',
      },
    })
    ElMessage.success('已以管理员身份进入')
    router.replace(auth.homePath)
  } catch (e) {
    const status = e?.response?.status
    const msg = e?.response?.data?.msg || e?.response?.data?.message
    let show = msg || (status ? `请求失败 ${status}` : '网络错误，请确认后端已启动且地址正确')
    if (e?.code === 'ERR_NETWORK') show = '无法连接后端，请确认后端已启动（如 http://localhost:8080）'
    console.error('[一键登录] 请求异常', e?.response?.data || e?.message || e)
    ElMessage.error(show)
  } finally {
    devLoginLoading.value = false
  }
}

onMounted(refreshCaptcha)
</script>

<style scoped>
.login {
  position: relative;
  align-items: center;
  justify-content: center;
  padding: 28px 18px;
}

.panel {
  width: 100%;
  max-width: 440px;
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

.form :deep(.el-segmented) {
  --el-segmented-bg-color: rgba(255, 255, 255, 0.06);
  --el-segmented-item-selected-bg-color: rgba(79, 140, 255, 0.28);
  --el-segmented-item-selected-color: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 4px;
}

.dev-entry {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed rgba(255, 255, 255, 0.14);
}

.btn {
  flex: 1;
}

.hint {
  color: rgba(255, 255, 255, 0.72);
}

/* 悬停/聚焦时保持文字可见：浅色背景 + 高对比度文字 */
.form :deep(.actions .el-button.hint:hover),
.form :deep(.actions .el-button.hint:focus) {
  background-color: rgba(255, 255, 255, 0.12);
  color: rgba(255, 255, 255, 0.98);
}
.form :deep(.actions .el-button.hint:focus-visible) {
  outline: none;
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

.tips {
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px dashed rgba(255, 255, 255, 0.14);
  display: grid;
  gap: 6px;
}

.tip {
  color: rgba(255, 255, 255, 0.56);
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
