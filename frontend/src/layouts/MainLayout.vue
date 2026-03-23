<template>
  <div class="layout">
    <aside class="sider">
      <div class="brand">
        <div class="brand-dot"></div>
        <div class="brand-title">驾校预约系统</div>
      </div>

      <el-scrollbar class="menu-scroll">
        <el-menu
          :default-active="activeMenu"
          class="menu"
          router
          :collapse-transition="false"
          background-color="#0b1226"
          text-color="rgba(255,255,255,0.82)"
          active-text-color="#4f8cff"
        >
          <template v-for="item in menus" :key="item.path">
            <el-menu-item :index="item.path">
              <span>{{ item.label }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>

      <div class="sider-footer">
        <div class="user-line">
          <div class="avatar">{{ initials }}</div>
          <div class="user-meta">
            <div class="user-name">
              {{ auth.profile.name }}
              <el-tag v-if="auth.role === 'student' && auth.profile.carType" size="small" type="success" style="margin-left: 6px">{{ auth.profile.carType }}</el-tag>
            </div>
            <div class="user-role">{{ roleLabel }}</div>
          </div>
        </div>
        <el-button size="small" type="primary" plain @click="onLogout">退出登录</el-button>
      </div>
    </aside>

    <main class="main">
      <header class="topbar">
        <div class="topbar-left">
          <div class="title">{{ currentTitle }}</div>
          <div class="subtitle">科技感界面 · 高效预约 · 清晰管理</div>
        </div>
        <div class="topbar-right">
          <el-tag effect="dark" type="info" class="tag">{{ roleLabel }}</el-tag>
        </div>
      </header>

      <section class="content">
        <router-view />
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const roleLabel = computed(() => {
  if (auth.role === 'coach') return '教练端'
  if (auth.role === 'admin') return '管理员端'
  return '学员端'
})

const initials = computed(() => (auth.profile.name || 'U').slice(0, 1))

const menus = computed(() => {
  if (auth.role === 'coach') {
    return [
      { label: '个人信息', path: '/app/coach/profile' },
      { label: '排班管理', path: '/app/coach/schedule' },
      { label: '预约确认', path: '/app/coach/confirm' },
      { label: '预约记录', path: '/app/coach/appointments' },
      { label: '学时管理', path: '/app/coach/hours' },
      { label: '学员管理', path: '/app/coach/students' },
      { label: '车辆管理', path: '/app/coach/vehicles' },
      { label: '设置中心', path: '/app/coach/settings' },
    ]
  }
  if (auth.role === 'admin') {
    return [
      { label: '学员管理', path: '/app/admin/users' },
      { label: '教练管理', path: '/app/admin/coaches' },
      { label: '车辆管理', path: '/app/admin/vehicles' },
      { label: '数据统计分析', path: '/app/admin/stats' },
      { label: '系统配置', path: '/app/admin/settings' },
    ]
  }
  return [
    { label: '个人信息', path: '/app/student/profile' },
    { label: '练车预约', path: '/app/student/booking' },
    { label: '学时查询', path: '/app/student/hours' },
    { label: '预约记录', path: '/app/student/history' },
    { label: '设置中心', path: '/app/student/settings' },
  ]
})

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '控制台')

function onLogout() {
  auth.logout()
  router.replace({ name: 'login' })
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 264px 1fr;
  background: rgba(255, 255, 255, 0.02);
}

.sider {
  background: linear-gradient(180deg, #0b1226 0%, #070b14 100%);
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
}

.brand {
  padding: 18px 18px 12px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #4f8cff;
  box-shadow: 0 0 18px rgba(79, 140, 255, 0.8);
}

.brand-title {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 700;
  letter-spacing: 0.6px;
}

.menu-scroll {
  flex: 1;
  padding: 8px 10px 10px;
}

.menu {
  border-right: none;
}

.sider-footer {
  padding: 14px 14px 18px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-line {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  color: rgba(255, 255, 255, 0.92);
  background: rgba(79, 140, 255, 0.18);
  border: 1px solid rgba(79, 140, 255, 0.28);
}

.user-meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
}

.user-role {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.main {
  display: flex;
  flex-direction: column;
}

.topbar {
  padding: 18px 22px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  background: rgba(0, 0, 0, 0.12);
}

.title {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 700;
  font-size: 16px;
}

.subtitle {
  margin-top: 4px;
  color: rgba(255, 255, 255, 0.55);
  font-size: 12px;
}

.tag {
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.content {
  padding: 18px 22px 26px;
}
</style>
