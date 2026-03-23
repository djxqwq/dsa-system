import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const LoginView = () => import('../views/LoginView.vue')
const RegisterView = () => import('../views/RegisterView.vue')
const MainLayout = () => import('../layouts/MainLayout.vue')

const StudentProfile = () => import('../views/student/ProfileView.vue')
const StudentSettings = () => import('../views/student/SettingsView.vue')
const StudentBooking = () => import('../views/student/BookingView.vue')
const StudentHours = () => import('../views/student/HoursView.vue')
const StudentHistory = () => import('../views/student/HistoryView.vue')

const CoachProfile = () => import('../views/coach/ProfileView.vue')
const CoachSettings = () => import('../views/coach/SettingsView.vue')
const CoachSchedule = () => import('../views/coach/ScheduleView.vue')
const CoachConfirm = () => import('../views/coach/ConfirmView.vue')
const CoachHours = () => import('../views/coach/HoursRecordView.vue')
const CoachAppointments = () => import('../views/coach/AppointmentsView.vue')
const CoachVehicles = () => import('../views/coach/VehiclesView.vue')
const CoachStudents = () => import('../views/coach/StudentsView.vue')

const AdminUsers = () => import('../views/admin/UsersView.vue')
const AdminCoaches = () => import('../views/admin/CoachesView.vue')
const AdminVehicles = () => import('../views/admin/VehiclesView.vue')
const AdminStats = () => import('../views/admin/StatsView.vue')
const AdminSettings = () => import('../views/admin/SettingsView.vue')

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'login', component: LoginView, meta: { public: true } },
  { path: '/register', name: 'register', component: RegisterView, meta: { public: true } },
  {
    path: '/app',
    component: MainLayout,
    children: [
      { path: 'student/settings', name: 'student-settings', component: StudentSettings, meta: { role: 'student', title: '设置中心' } },
      { path: 'student/profile', name: 'student-profile', component: StudentProfile, meta: { role: 'student', title: '个人信息' } },
      { path: 'student/booking', name: 'student-booking', component: StudentBooking, meta: { role: 'student', title: '练车预约' } },
      { path: 'student/hours', name: 'student-hours', component: StudentHours, meta: { role: 'student', title: '学时查询' } },
      { path: 'student/history', name: 'student-history', component: StudentHistory, meta: { role: 'student', title: '预约记录' } },

      { path: 'coach/settings', name: 'coach-settings', component: CoachSettings, meta: { role: 'coach', title: '设置中心' } },
      { path: 'coach/profile', name: 'coach-profile', component: CoachProfile, meta: { role: 'coach', title: '个人信息' } },
      { path: 'coach/schedule', name: 'coach-schedule', component: CoachSchedule, meta: { role: 'coach', title: '排班管理' } },
      { path: 'coach/confirm', name: 'coach-confirm', component: CoachConfirm, meta: { role: 'coach', title: '预约确认' } },
      { path: 'coach/appointments', name: 'coach-appointments', component: CoachAppointments, meta: { role: 'coach', title: '预约记录' } },
      { path: 'coach/hours', name: 'coach-hours', component: CoachHours, meta: { role: 'coach', title: '学时管理' } },
      { path: 'coach/vehicles', name: 'coach-vehicles', component: CoachVehicles, meta: { role: 'coach', title: '车辆管理' } },
      { path: 'coach/students', name: 'coach-students', component: CoachStudents, meta: { role: 'coach', title: '学员管理' } },

      { path: 'admin/users', name: 'admin-users', component: AdminUsers, meta: { role: 'admin', title: '学员管理' } },
      { path: 'admin/coaches', name: 'admin-coaches', component: AdminCoaches, meta: { role: 'admin', title: '教练管理' } },
      { path: 'admin/vehicles', name: 'admin-vehicles', component: AdminVehicles, meta: { role: 'admin', title: '车辆管理' } },
      { path: 'admin/stats', name: 'admin-stats', component: AdminStats, meta: { role: 'admin', title: '数据统计分析' } },
      { path: 'admin/settings', name: 'admin-settings', component: AdminSettings, meta: { role: 'admin', title: '系统配置' } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.public) return true

  if (!auth.isAuthed) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  const requiredRole = to.meta.role
  if (requiredRole && auth.role !== requiredRole) {
    return auth.homePath
  }

  return true
})

export default router
