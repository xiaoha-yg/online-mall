import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from './auth'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const token = getToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
}, error => Promise.reject(error))

request.interceptors.response.use(response => {
  const res = response.data
  // ✅ 后端返回的 code 是数字 200，不是字符串
  if (res.code === 200) {
    return res.data   // ← 直接返回 data（商品数组）
  } else {
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  }
}, error => {
  ElMessage.error('网络错误，请稍后重试')
  return Promise.reject(error)
})

export default request