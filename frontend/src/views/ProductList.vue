<template>
  <div>
    <h2 style="margin-bottom: 20px; color: #303133;">🏠 商品列表</h2>
    <el-row :gutter="20">
      <el-col :span="8" v-for="item in products" :key="item.id" style="margin-bottom: 20px;">
        <el-card shadow="hover" :body-style="{ padding: '20px' }">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <h3 style="margin: 0; color: #303133;">{{ item.name }}</h3>
              <el-tag :type="item.stock > 10 ? 'success' : 'danger'" effect="plain">
                {{ item.stock > 10 ? '有货' : '库存紧张' }}
              </el-tag>
            </div>
          </template>
          <p style="color: #909399; margin-bottom: 10px;">{{ item.description }}</p>
          <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
            <div>
              <span style="font-size: 22px; color: #f56c6c; font-weight: bold;">¥{{ item.price }}</span>
              <span style="color: #909399; margin-left: 10px; font-size: 13px;">库存：{{ item.stock }}</span>
            </div>
            <el-button type="primary" @click="addCart(item.id)" :icon="ShoppingCart">
              加入购物车
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductList } from '@/api/product'
import { addToCart } from '@/api/cart'
import { ElMessage } from 'element-plus'

const products = ref([])

onMounted(async () => {
  products.value = await getProductList()
})

const addCart = async (productId) => {
  await addToCart({ productId, quantity: 1 })
  ElMessage.success('已加入购物车')
}
</script>