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

        <div class="actions">
          <el-button type="primary" class="btn" @click="onLogin">登录</el-button>
          <el-button text class="hint" @click="fillDemo">一键填充演示账号</el-button>
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
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const formRef = ref()

const form = reactive({
  role: 'student',
  mobile: '',
  password: '',
})

const roleOptions = [
  { label: '学员', value: 'student' },
  { label: '教练', value: 'coach' },
  { label: '管理员', value: 'admin' },
]

const rules = {
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
}

function fillDemo() {
  if (form.role === 'coach') {
    form.mobile = '13900139000'
  } else if (form.role === 'admin') {
    form.mobile = '18800188000'
  } else {
    form.mobile = '13800138000'
  }
  form.password = 'Aa123456'
}

async function onLogin() {
  await formRef.value?.validate()

  auth.login({
    token: `demo_${Date.now()}`,
    role: form.role,
    profile: {
      name: form.role === 'student' ? '学员用户' : form.role === 'coach' ? '教练用户' : '管理员',
      mobile: form.mobile,
    },
  })

  ElMessage.success('登录成功')

  const redirect = route.query.redirect
  if (typeof redirect === 'string' && redirect.startsWith('/')) {
    router.replace(redirect)
    return
  }
  router.replace(auth.homePath)
}
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

.btn {
  flex: 1;
}

.hint {
  color: rgba(255, 255, 255, 0.72);
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
