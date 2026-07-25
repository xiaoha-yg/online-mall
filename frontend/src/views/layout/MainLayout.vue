<template>
  <el-container>
    <el-header style="background: #409EFF; padding: 0; box-shadow: 0 2px 8px rgba(0,0,0,0.1); height: 60px;">
      <div style="display: flex; align-items: center; justify-content: space-between; height: 100%; padding: 0 20px; max-width: 1400px; margin: 0 auto;">
        <h1 style="color: white; margin: 0; font-size: 20px; cursor: pointer;" @click="$router.push('/home')">🛒 在线商城系统</h1>
        <el-menu 
          mode="horizontal" 
          router 
          :default-active="$route.path"
          background-color="#409EFF"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="border-bottom: none;"
        >
          <el-menu-item index="/home">🏠 商品列表</el-menu-item>
          <el-menu-item index="/cart">🛒 购物车</el-menu-item>
          <el-menu-item index="/orders">📋 我的订单</el-menu-item>
          <el-menu-item index="/user">👤 个人中心</el-menu-item>
          <el-menu-item @click="handleLogout">🚪 退出</el-menu-item>
        </el-menu>
      </div>
    </el-header>
    <el-main style="min-height: calc(100vh - 60px); background: #f5f7fa; padding: 20px;">
      <div style="max-width: 1400px; margin: 0 auto;">
        <router-view />
      </div>
    </el-main>
  </el-container>
</template>
<script>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>