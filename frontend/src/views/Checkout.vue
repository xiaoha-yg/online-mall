<template>
  <div>
    <h2 style="margin-bottom: 20px;">📝 确认订单</h2>
    <el-card>
      <h3>订单商品</h3>
      <el-table :data="cart.items" style="width: 100%; margin: 20px 0;">
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="price" label="单价" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="itemTotal" label="小计" />
      </el-table>
      <div style="text-align: right;">
        <h2 style="color: #f56c6c;">应付金额：¥{{ cart.totalAmount }}</h2>
      </div>
      <div style="text-align: right; margin-top: 20px;">
        <el-button @click="$router.back()">返回修改</el-button>
        <el-button type="primary" size="large" @click="submitOrder" :loading="submitting">确认下单</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCartList } from '@/api/cart'
import { createOrder } from '@/api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cart = ref({ items: [], totalAmount: 0 })
const submitting = ref(false)

onMounted(async () => { cart.value = await getCartList() })

const submitOrder = async () => {
  submitting.value = true
  try {
    const cartIds = cart.value.items.map(i => i.cartId)
    await createOrder({ cartIds })
    ElMessage.success('下单成功')
    router.push('/orders')
  } catch {} finally { submitting.value = false }
}
</script>