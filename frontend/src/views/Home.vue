<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-input v-model="searchKeyword" placeholder="搜索商品..." clearable />
      </el-col>
      <el-col :span="4">
        <el-select v-model="sortBy" placeholder="排序方式">
          <el-option label="默认" value="default" />
          <el-option label="价格从低到高" value="price-asc" />
          <el-option label="价格从高到低" value="price-desc" />
        </el-select>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="6" v-for="item in filteredProducts" :key="item.id" style="margin-bottom: 20px;">
        <el-card shadow="hover" :body-style="{ padding: '0' }" style="cursor: pointer;">
          <img 
            :src="item.imageUrl || 'https://via.placeholder.com/300x200/E6E8EB/CCCCCC?text=暂无图片'"
            style="width: 100%; height: 200px; object-fit: cover;"
          />
          <div style="padding: 15px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
              <h3 style="margin: 0; font-size: 16px;">{{ item.name }}</h3>
              <el-tag :type="item.stock > 10 ? 'success' : 'danger'" size="small">库存 {{ item.stock }}</el-tag>
            </div>
            <p style="color: #909399; font-size: 13px; margin-bottom: 10px;">{{ item.description }}</p>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-size: 20px; color: #f56c6c; font-weight: bold;">¥{{ item.price }}</span>
              <el-button type="primary" size="small" @click.stop="handleAddCart(item.id)">加入购物车</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getProductList } from '@/api/product'
import { addToCart } from '@/api/cart'
import { ElMessage } from 'element-plus'

const products = ref([])
const searchKeyword = ref('')
const sortBy = ref('default')

const filteredProducts = computed(() => {
  let list = [...products.value]
  if (searchKeyword.value) {
    list = list.filter(p => p.name.includes(searchKeyword.value))
  }
  if (sortBy.value === 'price-asc') list.sort((a, b) => a.price - b.price)
  else if (sortBy.value === 'price-desc') list.sort((a, b) => b.price - a.price)
  return list
})

const handleAddCart = async (productId) => {
  await addToCart({ productId, quantity: 1 })
  ElMessage.success('已加入购物车')
}

onMounted(async () => {
  try {
    products.value = await getProductList()
  } catch (e) {
    ElMessage.error('加载商品失败')
  }
})
</script>