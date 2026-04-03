<template>
  <div class="page login">
    <div class="login-grid">
      <div class="hero" aria-hidden="true">
        <div class="hero-inner">
          <div class="hero-brand">
            <div class="brand-icon"></div>
            <div class="brand-text">驾校系统</div>
          </div>

          <div class="hero-stage">
            <div class="characters" style="width: 550px; height: 400px">
              <div
                ref="purpleRef"
                class="character purple"
                :style="{
                  height: (isTyping || form.password) ? '440px' : '400px',
                  transform: isLookingAtEachOther
                    ? `skewX(${(purplePos.bodySkew || 0) - 12}deg) translateX(40px)`
                    : `skewX(${purplePos.bodySkew || 0}deg)`,
                }"
              >
                <div
                  class="eyes"
                  :style="{
                    left: (form.password && showPassword) ? `${20}px` : isLookingAtEachOther ? `${55}px` : `${45 + purplePos.faceX}px`,
                    top: (form.password && showPassword) ? `${35}px` : isLookingAtEachOther ? `${65}px` : `${40 + purplePos.faceY}px`,
                  }"
                >
                  <div class="eyeball" :class="{ blink: isPurpleBlinking }">
                    <div
                      class="pupil"
                      :style="{
                        transform: `translate(${(form.password && showPassword) ? (isPurplePeeking ? 4 : -4) : isLookingAtEachOther ? 3 : purplePupil.x}px, ${(form.password && showPassword) ? (isPurplePeeking ? 5 : -4) : isLookingAtEachOther ? 4 : purplePupil.y}px)`,
                      }"
                    ></div>
                  </div>
                  <div class="eyeball" :class="{ blink: isPurpleBlinking }">
                    <div
                      class="pupil"
                      :style="{
                        transform: `translate(${(form.password && showPassword) ? (isPurplePeeking ? 4 : -4) : isLookingAtEachOther ? 3 : purplePupil.x}px, ${(form.password && showPassword) ? (isPurplePeeking ? 5 : -4) : isLookingAtEachOther ? 4 : purplePupil.y}px)`,
                      }"
                    ></div>
                  </div>
                </div>
              </div>

              <div
                ref="blackRef"
                class="character black"
                :style="{
                  transform: isLookingAtEachOther
                    ? `skewX(${(blackPos.bodySkew || 0) * 1.5 + 10}deg) translateX(20px)`
                    : `skewX(${blackPos.bodySkew || 0}deg)`,
                }"
              >
                <div
                  class="eyes eyes--black"
                  :style="{
                    left: (form.password && showPassword) ? `${10}px` : isLookingAtEachOther ? `${32}px` : `${26 + blackPos.faceX}px`,
                    top: (form.password && showPassword) ? `${28}px` : isLookingAtEachOther ? `${12}px` : `${32 + blackPos.faceY}px`,
                  }"
                >
                  <div class="eyeball eyeball--sm" :class="{ blink: isBlackBlinking }">
                    <div
                      class="pupil pupil--sm"
                      :style="{
                        transform: `translate(${(form.password && showPassword) ? -4 : isLookingAtEachOther ? 0 : blackPupil.x}px, ${(form.password && showPassword) ? -4 : isLookingAtEachOther ? -4 : blackPupil.y}px)`,
                      }"
                    ></div>
                  </div>
                  <div class="eyeball eyeball--sm" :class="{ blink: isBlackBlinking }">
                    <div
                      class="pupil pupil--sm"
                      :style="{
                        transform: `translate(${(form.password && showPassword) ? -4 : isLookingAtEachOther ? 0 : blackPupil.x}px, ${(form.password && showPassword) ? -4 : isLookingAtEachOther ? -4 : blackPupil.y}px)`,
                      }"
                    ></div>
                  </div>
                </div>
              </div>

              <div
                ref="orangeRef"
                class="character orange"
                :style="{ transform: `skewX(${orangePos.bodySkew || 0}deg)` }"
              >
                <div
                  class="pupils"
                  :style="{ left: (form.password && showPassword) ? `${50}px` : `${82 + (orangePos.faceX || 0)}px`, top: (form.password && showPassword) ? `${85}px` : `${90 + (orangePos.faceY || 0)}px` }"
                >
                  <div class="pupil-only" :style="{ transform: `translate(${(form.password && showPassword) ? -5 : orangePupil.x}px, ${(form.password && showPassword) ? -4 : orangePupil.y}px)` }"></div>
                  <div class="pupil-only" :style="{ transform: `translate(${(form.password && showPassword) ? -5 : orangePupil.x}px, ${(form.password && showPassword) ? -4 : orangePupil.y}px)` }"></div>
                </div>
              </div>

              <div
                ref="yellowRef"
                class="character yellow"
                :style="{ transform: `skewX(${yellowPos.bodySkew || 0}deg)` }"
              >
                <div
                  class="pupils pupils--yellow"
                  :style="{ left: (form.password && showPassword) ? `${20}px` : `${52 + (yellowPos.faceX || 0)}px`, top: (form.password && showPassword) ? `${35}px` : `${40 + (yellowPos.faceY || 0)}px` }"
                >
                  <div class="pupil-only" :style="{ transform: `translate(${(form.password && showPassword) ? -5 : yellowPupil.x}px, ${(form.password && showPassword) ? -4 : yellowPupil.y}px)` }"></div>
                  <div class="pupil-only" :style="{ transform: `translate(${(form.password && showPassword) ? -5 : yellowPupil.x}px, ${(form.password && showPassword) ? -4 : yellowPupil.y}px)` }"></div>
                </div>
                <div
                  class="mouth"
                  :style="{ left: (form.password && showPassword) ? `${10}px` : `${40 + (yellowPos.faceX || 0)}px`, top: `${88 + (yellowPos.faceY || 0)}px` }"
                ></div>
              </div>
            </div>
          </div>

          <div class="hero-footer">
            <a href="#" class="hero-link">Privacy Policy</a>
            <a href="#" class="hero-link">Terms of Service</a>
            <a href="#" class="hero-link">Contact</a>
          </div>
        </div>

        <div class="hero-grid"></div>
        <div class="hero-glow hero-glow--a"></div>
        <div class="hero-glow hero-glow--b"></div>
      </div>

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
            <el-input v-model="form.mobile" placeholder="请输入手机号" clearable @focus="onTypingStart" @blur="onTypingEnd" />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input 
              v-model="form.password" 
              placeholder="请输入密码" 
              :type="showPassword ? 'text' : 'password'"
              clearable 
              @focus="onTypingStart" 
              @blur="onTypingEnd"
            >
              <template #suffix>
                <div class="password-toggle" @click="togglePasswordVisibility">
                  <svg v-if="showPassword" class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                    <line x1="1" y1="1" x2="23" y2="23"></line>
                  </svg>
                  <svg v-else class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                    <circle cx="12" cy="12" r="3"></circle>
                  </svg>
                </div>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="captchaCode">
            <div class="captcha-row">
              <el-input v-model="form.captchaCode" placeholder="请输入验证码" clearable @focus="onTypingStart" @blur="onTypingEnd" />
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
            <div class="dev-row">
              <el-button type="success" plain size="small" :loading="devLoginLoading" @click="devAdminLogin">
                开发：一键管理员登录
              </el-button>
            </div>
            <div class="dev-row">
              <el-button type="warning" plain size="small" :loading="devStudentLoginLoading" @click="openStudentDialog">
                开发：一键学员登录
              </el-button>
              <el-button type="primary" plain size="small" :loading="devCoachLoginLoading" @click="openCoachDialog">
                开发：一键教练登录
              </el-button>
            </div>
          </div>

          <div class="tips">
            <div class="tip">学员端：预约练车、查学时、看历史</div>
            <div class="tip">教练端：排班、确认预约、学时管理</div>
            <div class="tip">管理员端：用户/教练/车辆/统计/配置</div>
          </div>
        </el-form>
      </div>
    </div>

    <el-dialog v-model="studentDialogVisible" title="选择学员" width="400px" :close-on-click-modal="false">
      <div v-if="studentListLoading" class="dialog-loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>
      <div v-else-if="studentList.length === 0" class="dialog-empty">暂无可用学员账号</div>
      <div v-else class="user-list">
        <div
          v-for="student in studentList"
          :key="student.id"
          class="user-item"
          @click="onSelectStudent(student.id)"
        >
          <div class="user-name">{{ student.userName || '未命名' }}</div>
          <div class="user-info">
            <span>{{ student.mobile }}</span>
            <span v-if="student.carType" class="car-type">{{ student.carType }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="coachDialogVisible" title="选择教练" width="400px" :close-on-click-modal="false">
      <div v-if="coachListLoading" class="dialog-loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>
      <div v-else-if="coachList.length === 0" class="dialog-empty">暂无可用教练账号</div>
      <div v-else class="user-list">
        <div
          v-for="coach in coachList"
          :key="coach.id"
          class="user-item"
          @click="onSelectCoach(coach.id)"
        >
          <div class="user-name">{{ coach.name || '未命名' }}</div>
          <div class="user-info">
            <span>{{ coach.mobile }}</span>
            <span v-if="coach.coachNo" class="coach-no">{{ coach.coachNo }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

    <div class="bg-grid"></div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'
import http from '../api/http'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const formRef = ref()
const isDev = import.meta.env.DEV
const devLoginLoading = ref(false)
const devStudentLoginLoading = ref(false)
const devCoachLoginLoading = ref(false)

const studentDialogVisible = ref(false)
const coachDialogVisible = ref(false)
const studentList = ref([])
const coachList = ref([])
const studentListLoading = ref(false)
const coachListLoading = ref(false)
const selectedStudentId = ref(null)
const selectedCoachId = ref(null)

const mouseX = ref(0)
const mouseY = ref(0)
const purpleRef = ref(null)
const blackRef = ref(null)
const yellowRef = ref(null)
const orangeRef = ref(null)

const isPurpleBlinking = ref(false)
const isBlackBlinking = ref(false)
const isTyping = ref(false)
const isLookingAtEachOther = ref(false)
const isPurplePeeking = ref(false)
const showPassword = ref(false)

let removeMouseListener = null
let purpleBlinkTimeout = null
let blackBlinkTimeout = null
let purplePeekTimeout = null

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

function onTypingStart() {
  isTyping.value = true
}

function onTypingEnd() {
  isTyping.value = false
}

function scheduleBlink(targetRef, onRef, setBlinking) {
  const getRandomBlinkInterval = () => Math.random() * 4000 + 3000
  const timeout = setTimeout(() => {
    setBlinking(true)
    setTimeout(() => {
      setBlinking(false)
      scheduleBlink(targetRef, onRef, setBlinking)
    }, 150)
  }, getRandomBlinkInterval())
  onRef(timeout)
}

function calcPos(el) {
  if (!el) return { faceX: 0, faceY: 0, bodySkew: 0 }

  const rect = el.getBoundingClientRect()
  const centerX = rect.left + rect.width / 2
  const centerY = rect.top + rect.height / 3
  const dx = mouseX.value - centerX
  const dy = mouseY.value - centerY

  const faceX = Math.max(-15, Math.min(15, dx / 20))
  const faceY = Math.max(-10, Math.min(10, dy / 30))
  const bodySkew = Math.max(-6, Math.min(6, -dx / 120))
  return { faceX, faceY, bodySkew }
}

const purplePos = computed(() => calcPos(purpleRef.value))
const blackPos = computed(() => calcPos(blackRef.value))
const yellowPos = computed(() => calcPos(yellowRef.value))
const orangePos = computed(() => calcPos(orangeRef.value))

function calcPupil(el, maxDistance) {
  if (!el) return { x: 0, y: 0 }
  const rect = el.getBoundingClientRect()
  const cx = rect.left + rect.width / 2
  const cy = rect.top + rect.height / 2
  const dx = mouseX.value - cx
  const dy = mouseY.value - cy
  const dist = Math.min(Math.sqrt(dx * dx + dy * dy), maxDistance)
  const ang = Math.atan2(dy, dx)
  return { x: Math.cos(ang) * dist, y: Math.sin(ang) * dist }
}

const purplePupil = computed(() => calcPupil(purpleRef.value, 5))
const blackPupil = computed(() => calcPupil(blackRef.value, 4))
const yellowPupil = computed(() => calcPupil(yellowRef.value, 5))
const orangePupil = computed(() => calcPupil(orangeRef.value, 5))

watch(
  () => isTyping.value,
  (v) => {
    if (v) {
      isLookingAtEachOther.value = true
      setTimeout(() => {
        isLookingAtEachOther.value = false
      }, 800)
    } else {
      isLookingAtEachOther.value = false
    }
  },
)

watch(
  () => [form.password, showPassword.value],
  () => {
    if (form.password && showPassword.value) {
      const schedulePeek = () => {
        const peekTimeout = setTimeout(() => {
          isPurplePeeking.value = true
          setTimeout(() => {
            isPurplePeeking.value = false
          }, 800)
          purplePeekTimeout = schedulePeek()
        }, Math.random() * 3000 + 2000)
        return peekTimeout
      }
      purplePeekTimeout = schedulePeek()
    } else {
      isPurplePeeking.value = false
      if (purplePeekTimeout) {
        clearTimeout(purplePeekTimeout)
        purplePeekTimeout = null
      }
    }
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
        carType: data.carType,
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

async function openStudentDialog() {
  studentDialogVisible.value = true
  studentListLoading.value = true
  studentList.value = []
  try {
    const res = await http.get('/api/user/dev/students', { params: { secret: DEV_ADMIN_SECRET } })
    if (res?.data?.code === 200) {
      studentList.value = res.data.data || []
    } else {
      ElMessage.error(res?.data?.msg || '获取学员列表失败')
    }
  } catch (e) {
    ElMessage.error('获取学员列表失败')
  } finally {
    studentListLoading.value = false
  }
}

async function openCoachDialog() {
  coachDialogVisible.value = true
  coachListLoading.value = true
  coachList.value = []
  try {
    const res = await http.get('/api/user/dev/coaches', { params: { secret: DEV_ADMIN_SECRET } })
    if (res?.data?.code === 200) {
      coachList.value = res.data.data || []
    } else {
      ElMessage.error(res?.data?.msg || '获取教练列表失败')
    }
  } catch (e) {
    ElMessage.error('获取教练列表失败')
  } finally {
    coachListLoading.value = false
  }
}

function onSelectStudent(studentId) {
  selectedStudentId.value = studentId
  devStudentLogin()
}

function onSelectCoach(coachId) {
  selectedCoachId.value = coachId
  devCoachLogin()
}

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

async function devStudentLogin() {
  devStudentLoginLoading.value = true
  try {
    const res = await http.post('/api/user/student/dev-login', { 
      secret: DEV_ADMIN_SECRET,
      studentId: selectedStudentId.value 
    })
    const code = res?.data?.code
    const ok = code === 200 || code === '200'
    if (!ok) {
      const msg = res?.data?.msg || '一键登录失败'
      console.error('[学员一键登录] 后端返回异常', res?.data)
      ElMessage.error(msg)
      return
    }
    const data = res.data.data || {}
    const token = data.token || ''
    if (!token) {
      console.error('[学员一键登录] 未返回 token', res?.data)
      ElMessage.error('登录失败：未返回 token')
      return
    }
    auth.login({
      token,
      role: 'student',
      profile: {
        name: data.userName || '学员',
        mobile: data.mobile || '',
        carType: data.carType,
      },
    })
    ElMessage.success('已以学员身份进入')
    studentDialogVisible.value = false
    router.replace(auth.homePath)
  } catch (e) {
    const status = e?.response?.status
    const msg = e?.response?.data?.msg || e?.response?.data?.message
    let show = msg || (status ? `请求失败 ${status}` : '网络错误，请确认后端已启动且地址正确')
    if (e?.code === 'ERR_NETWORK') show = '无法连接后端，请确认后端已启动（如 http://localhost:8080）'
    console.error('[学员一键登录] 请求异常', e?.response?.data || e?.message || e)
    ElMessage.error(show)
  } finally {
    devStudentLoginLoading.value = false
  }
}

async function devCoachLogin() {
  devCoachLoginLoading.value = true
  try {
    const res = await http.post('/api/user/coach/dev-login', { 
      secret: DEV_ADMIN_SECRET,
      coachId: selectedCoachId.value 
    })
    const code = res?.data?.code
    const ok = code === 200 || code === '200'
    if (!ok) {
      const msg = res?.data?.msg || '一键登录失败'
      console.error('[教练一键登录] 后端返回异常', res?.data)
      ElMessage.error(msg)
      return
    }
    const data = res.data.data || {}
    const token = data.token || ''
    if (!token) {
      console.error('[教练一键登录] 未返回 token', res?.data)
      ElMessage.error('登录失败：未返回 token')
      return
    }
    auth.login({
      token,
      role: 'coach',
      profile: {
        name: data.name || '教练',
        mobile: data.mobile || '',
      },
    })
    ElMessage.success('已以教练身份进入')
    coachDialogVisible.value = false
    router.replace(auth.homePath)
  } catch (e) {
    const status = e?.response?.status
    const msg = e?.response?.data?.msg || e?.response?.data?.message
    let show = msg || (status ? `请求失败 ${status}` : '网络错误，请确认后端已启动且地址正确')
    if (e?.code === 'ERR_NETWORK') show = '无法连接后端，请确认后端已启动（如 http://localhost:8080）'
    console.error('[教练一键登录] 请求异常', e?.response?.data || e?.message || e)
    ElMessage.error(show)
  } finally {
    devCoachLoginLoading.value = false
  }
}

function togglePasswordVisibility() {
  showPassword.value = !showPassword.value
}

onMounted(() => {
  refreshCaptcha()
  const onMouseMove = (e) => {
    mouseX.value = e.clientX
    mouseY.value = e.clientY
  }
  window.addEventListener('mousemove', onMouseMove)
  removeMouseListener = () => window.removeEventListener('mousemove', onMouseMove)

  scheduleBlink(purpleRef, (t) => { purpleBlinkTimeout = t }, (v) => { isPurpleBlinking.value = v })
  scheduleBlink(blackRef, (t) => { blackBlinkTimeout = t }, (v) => { isBlackBlinking.value = v })
})

onBeforeUnmount(() => {
  removeMouseListener?.()
  if (purpleBlinkTimeout) clearTimeout(purpleBlinkTimeout)
  if (blackBlinkTimeout) clearTimeout(blackBlinkTimeout)
  if (purplePeekTimeout) clearTimeout(purplePeekTimeout)
})
</script>

<style scoped>
.login {
  position: relative;
  align-items: center;
  justify-content: center;
  padding: 28px 18px;
}

.login-grid {
  width: 100%;
  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr;
  gap: 18px;
  align-items: center;
  position: relative;
  z-index: 2;
}

.hero {
  display: none;
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  min-height: 620px;
}

.hero-inner {
  position: relative;
  z-index: 2;
  height: 100%;
  padding: 38px 34px;
  color: rgba(255, 255, 255, 0.92);
  background: linear-gradient(135deg, rgba(79, 140, 255, 0.85), rgba(79, 140, 255, 0.65) 50%, rgba(79, 140, 255, 0.55));
}

.hero-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
}

.brand-icon {
  width: 32px;
  height: 32px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.10);
  backdrop-filter: blur(8px);
}

.brand-text {
  font-size: 16px;
  letter-spacing: 0.3px;
}

.hero-stage {
  height: 500px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.characters {
  position: relative;
}

.character {
  position: absolute;
  bottom: 0;
  transition: all 0.7s ease-in-out;
  transform-origin: bottom center;
}

.character.purple {
  left: 70px;
  width: 180px;
  background: #6c3ff5;
  border-radius: 10px 10px 0 0;
  z-index: 1;
}

.character.black {
  left: 240px;
  width: 120px;
  height: 310px;
  background: #2d2d2d;
  border-radius: 8px 8px 0 0;
  z-index: 2;
}

.character.orange {
  left: 0;
  width: 240px;
  height: 200px;
  background: #ff9b6b;
  border-radius: 120px 120px 0 0;
  z-index: 3;
}

.character.yellow {
  left: 310px;
  width: 140px;
  height: 230px;
  background: #e8d754;
  border-radius: 70px 70px 0 0;
  z-index: 4;
}

.eyes {
  position: absolute;
  display: flex;
  gap: 18px;
  transition: all 0.7s ease-in-out;
}

.eyes--black {
  gap: 12px;
}

.eyeball {
  width: 18px;
  height: 18px;
  border-radius: 999px;
  background: #fff;
  overflow: hidden;
  display: grid;
  place-items: center;
  transition: all 0.15s;
}

.eyeball--sm {
  width: 16px;
  height: 16px;
}

.eyeball.blink {
  height: 2px;
}

.pupil {
  width: 7px;
  height: 7px;
  border-radius: 999px;
  background: #2d2d2d;
  transition: transform 0.1s ease-out;
}

.pupil--sm {
  width: 6px;
  height: 6px;
}

.pupils {
  position: absolute;
  display: flex;
  gap: 24px;
  transition: all 0.2s ease-out;
}

.pupils--yellow {
  gap: 18px;
}

.pupil-only {
  width: 12px;
  height: 12px;
  border-radius: 999px;
  background: #2d2d2d;
  transition: transform 0.1s ease-out;
}

.mouth {
  position: absolute;
  width: 80px;
  height: 4px;
  border-radius: 999px;
  background: #2d2d2d;
  transition: all 0.2s ease-out;
}

.hero-footer {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.65);
}

.hero-link {
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
}

.hero-link:hover {
  color: rgba(255, 255, 255, 0.92);
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(255, 255, 255, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.06) 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.25;
  mask-image: radial-gradient(circle at center, black 40%, transparent 70%);
}

.hero-glow {
  position: absolute;
  border-radius: 999px;
  filter: blur(60px);
  background: rgba(255, 255, 255, 0.12);
}

.hero-glow--a {
  width: 260px;
  height: 260px;
  top: 18%;
  right: 20%;
}

.hero-glow--b {
  width: 360px;
  height: 360px;
  bottom: 14%;
  left: 18%;
  background: rgba(255, 255, 255, 0.08);
}

.password-toggle {
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.password-toggle:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.icon {
  width: 16px;
  height: 16px;
  color: #909399;
}

@media (min-width: 1024px) {
  .login {
    padding: 40px 24px;
  }

  .login-grid {
    grid-template-columns: 1.2fr 0.9fr;
    gap: 22px;
  }

  .hero {
    display: block;
  }
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

.dev-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.dev-row:first-child {
  margin-top: 0;
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

.dialog-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 40px;
  color: #666;
}

.dialog-empty {
  text-align: center;
  padding: 40px;
  color: #999;
}

.user-list {
  max-height: 320px;
  overflow-y: auto;
}

.user-item {
  padding: 12px 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.user-item:hover {
  border-color: #409eff;
  background-color: #f5f7fa;
}

.user-item:last-child {
  margin-bottom: 0;
}

.user-name {
  font-weight: 500;
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #909399;
}

.car-type, .coach-no {
  background: #f0f2f5;
  padding: 2px 6px;
  border-radius: 4px;
}
</style>
