<template>
  <div>
    <h2 style="margin-bottom: 20px;">👤 个人中心</h2>
    <el-button type="danger" @click="logout" style="margin-bottom: 20px;">退出登录</el-button>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header><h3>基本信息</h3></template>
          <p>用户名：hyg</p>
          <p>角色：普通用户</p>
          <p>注册时间：2026-01-01</p>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header><h3>快速入口</h3></template>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover" @click="$router.push('/orders')" style="cursor: pointer; text-align: center;">
                <h3>📋</h3><p>我的订单</p>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" @click="$router.push('/cart')" style="cursor: pointer; text-align: center;">
                <h3>🛒</h3><p>购物车</p>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" @click="$router.push('/home')" style="cursor: pointer; text-align: center;">
                <h3>🏠</h3><p>继续购物</p>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ElMessageBox, ElMessage } from 'element-plus'

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.clear()
    ElMessage.success('已退出登录')
    location.href = '/login'
  }).catch(() => {})
}
</script>