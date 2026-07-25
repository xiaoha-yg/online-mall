<template>
  <div>
    <h2 style="margin-bottom: 20px;">📋 订单管理</h2>
    <el-table :data="orders" style="width: 100%;" stripe>
      <el-table-column prop="orderNo" label="订单编号" min-width="250" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'warning' : 'success'">
            {{ scope.row.status === 0 ? '待支付' : '已完成' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default>
          <el-button size="small">详情</el-button>
          <el-button size="small" type="success">发货</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrderList } from '@/api/order'

const orders = ref([])
onMounted(async () => { orders.value = await getOrderList() })
</script>