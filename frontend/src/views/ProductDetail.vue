<template>
  <div>
    <el-button @click="$router.back()" circle style="margin-bottom: 20px;">←</el-button>
    <el-row :gutter="30" v-if="product">
      <el-col :span="10">
        <el-image :src="product.imageUrl || 'https://via.placeholder.com/400'" fit="cover" style="width: 100%; border-radius: 10px;" />
      </el-col>
      <el-col :span="14">
        <h1>{{ product.name }}</h1>
        <p style="color: #909399; margin: 15px 0;">{{ product.description }}</p>
        <p style="font-size: 28px; color: #f56c6c; font-weight: bold;">¥{{ product.price }}</p>
        <p style="margin: 10px 0;">库存：{{ product.stock }}</p>
        <el-input-number v-model="quantity" :min="1" :max="product.stock" style="margin: 15px 0;" />
        <br/>
        <el-button type="primary" size="large" @click="addToCart">加入购物车</el-button>
        <el-button type="success" size="large" @click="buyNow">立即购买</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductById } from '@/api/product'
import { addToCart as addCartAPI } from '@/api/cart'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const quantity = ref(1)

onMounted(async () => { product.value = await getProductById(route.params.id) })

const addToCart = async () => {
  await addCartAPI({ productId: product.value.id, quantity: quantity.value })
  ElMessage.success('已加入购物车')
}

const buyNow = async () => {
  await addCartAPI({ productId: product.value.id, quantity: quantity.value })
  router.push('/checkout')
}
</script>