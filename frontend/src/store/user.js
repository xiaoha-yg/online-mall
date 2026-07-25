import { defineStore } from 'pinia'
import { getToken, setToken, removeToken, getUser, setUser } from '@/utils/auth'
import { login as loginAPI, register as registerAPI } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: getUser() || null
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    async login(loginForm) {
      try {
        const res = await loginAPI(loginForm)
        this.token = res.token
        this.userInfo = res.user
        setToken(res.token)
        setUser(res.user)
        return { code: 200 }
      } catch (error) {
        return { code: 500, message: '登录失败' }
      }
    },
    async register(registerForm) {
      try {
        await registerAPI(registerForm)
        return { code: 200 }
      } catch (error) {
        return { code: 500, message: '注册失败' }
      }
    },
    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
    }
  }
})