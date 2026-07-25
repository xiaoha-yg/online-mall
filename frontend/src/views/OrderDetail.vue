<template>
  <div>
    <div style="display: flex; align-items: center; margin-bottom: 20px;">
      <el-button @click="$router.back()" circle>←</el-button>
      <h2 style="margin: 0 0 0 15px;">📋 订单详情</h2>
    </div>
    <el-descriptions v-if="order" border :column="2">
      <el-descriptions-item label="订单号" :span="2">{{ order.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="订单金额">
        <span style="color: #f56c6c; font-weight: bold; font-size: 18px;">¥{{ order.totalAmount }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="order.status === 0 ? 'warning' : order.status === 2 ? 'info' : 'success'">
          {{ order.status === 0 ? '待支付' : order.status === 2 ? '已取消' : '已完成' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ order.createTime || '-' }}</el-descriptions-item>
    </el-descriptions>
    <h3 style="margin: 20px 0;">订单商品</h3>
    <el-table :data="order?.items" stripe v-if="order?.items && order.items.length > 0">
      <el-table-column prop="productName" label="商品" />
      <el-table-column prop="price" label="单价" />
      <el-table-column prop="quantity" label="数量" />
      <el-table-column prop="totalPrice" label="小计">
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.totalPrice }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-else description="暂无商品数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '@/api/order'

const route = useRoute()
const order = ref(null)

onMounted(async () => {
  order.value = await getOrderDetail(route.params.orderNo)
})
</script>