<template>
  <div>
    <el-container style="min-height: 100vh;">
      <el-aside width="220px" style="background: #304156;">
        <div style="height: 60px; display: flex; align-items: center; justify-content: center;">
          <h2 style="color: white; font-size: 18px;">📊 管理后台</h2>
        </div>
        <el-menu 
          router 
          :default-active="$route.path"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/home">🏠 返回前台</el-menu-item>
          <el-menu-item index="/admin/dashboard">📊 数据看板</el-menu-item>
          <el-menu-item index="/admin/products">📦 商品管理</el-menu-item>
          <el-menu-item index="/admin/orders">📋 订单管理</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="background: #f5f7fa;">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>