import { defineStore } from 'pinia'

const LS_KEY = 'dsa_auth'

function safeParse(json) {
  try {
    return JSON.parse(json)
  } catch {
    return null
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => {
    const cached = safeParse(localStorage.getItem(LS_KEY) || '') || {}
    return {
      token: cached.token || '',
      role: cached.role || 'student',
      profile: cached.profile || {
        name: cached.profile?.name || '未命名用户',
        mobile: cached.profile?.mobile || '',
      },
    }
  },
  getters: {
    isAuthed: (s) => !!s.token,
    homePath: (s) => {
      if (s.role === 'coach') return { path: '/app/coach/profile' }
      if (s.role === 'admin') return { path: '/app/admin/users' }
      return { path: '/app/student/profile' }
    },
  },
  actions: {
    persist() {
      localStorage.setItem(
        LS_KEY,
        JSON.stringify({ token: this.token, role: this.role, profile: this.profile })
      )
    },
    login({ token, role, profile }) {
      this.token = token
      this.role = role
      this.profile = profile
      this.persist()
    },
    logout() {
      this.token = ''
      this.persist()
    },
  },
})
