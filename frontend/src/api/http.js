import axios from 'axios'
import { useAuthStore } from '../stores/auth'

// 开发时用相对路径走 Vite 代理，生产用环境变量或默认 8080
const baseURL = import.meta.env.VITE_API_BASE ?? (import.meta.env.DEV ? '' : 'http://localhost:8080')
const http = axios.create({
  baseURL,
  timeout: 15000,
})

http.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers = config.headers || {}
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

export default http
