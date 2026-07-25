<template>
  <div>
    <h2 style="margin-bottom: 20px; color: #303133;">📋 我的订单</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="全部订单" name="all" />
      <el-tab-pane label="待支付" name="pending" />
      <el-tab-pane label="已完成" name="completed" />
    </el-tabs>

    <el-table 
      :data="filteredOrders" 
      style="width: 100%;" 
      stripe 
      v-if="filteredOrders.length > 0"
      :key="activeTab"
      :default-sort="{ prop: 'createTime', order: 'descending' }"
    >
      <el-table-column prop="orderNo" label="订单编号" min-width="260">
        <template #default="scope">
          <span style="font-size: 13px; font-family: monospace;">{{ scope.row.orderNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="金额" width="100" sortable prop="totalAmount">
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="scope">
          <el-tag :type="statusType(scope.row.status)" size="small">
            {{ statusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="170" sortable prop="createTime">
        <template #default="scope">
          <span style="font-size: 13px; color: #909399;">{{ formatTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <div style="display: flex; gap: 2px; flex-wrap: nowrap;">
            <el-button size="small" text type="primary" @click="$router.push(`/orders/${scope.row.orderNo}`)">详情</el-button>
            <el-button v-if="scope.row.status === 0" size="small" text type="success" @click="handleComplete(scope.row.orderNo)">收货</el-button>
            <el-button v-if="scope.row.status === 0" size="small" text type="danger" @click="handleCancel(scope.row.orderNo)">取消</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-else description="暂无订单" />

    <!-- 分页 -->
    <div style="text-align: center; margin-top: 20px;" v-if="filteredOrders.length > 10">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="filteredOrders.length"
        :page-size="10"
        v-model:current-page="currentPage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getOrderList, cancelOrder, completeOrder } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const orders = ref([])
const activeTab = ref('all')
const currentPage = ref(1)

const statusType = (status) => {
  if (status === 0) return 'warning'
  if (status === 1) return 'success'
  return 'info'
}

const statusText = (status) => {
  if (status === 0) return '待支付'
  if (status === 1) return '已完成'
  return '已取消'
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 19)
}

const filteredOrders = computed(() => {
  let list = [...orders.value]
  if (activeTab.value === 'pending') list = list.filter(o => o.status === 0)
  else if (activeTab.value === 'completed') list = list.filter(o => o.status === 1 || o.status === 2)
  // 按时间倒序
  list.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
  return list
})

const handleCancel = async (orderNo) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      type: 'warning', confirmButtonText: '确定', cancelButtonText: '返回'
    })
    await cancelOrder(orderNo)
    ElMessage.success('订单已取消')
    orders.value = await getOrderList()
  } catch (error) {
    if (error !== 'cancel' && error.message !== 'cancel') {}
  }
}

const handleComplete = async (orderNo) => {
  try {
    await ElMessageBox.confirm('确认已收到货物？', '确认收货', {
      type: 'success', confirmButtonText: '确认', cancelButtonText: '返回'
    })
    await completeOrder(orderNo)
    ElMessage.success('订单已完成')
    orders.value = await getOrderList()
  } catch (error) {
    if (error !== 'cancel' && error.message !== 'cancel') {}
  }
}

onMounted(async () => {
  orders.value = await getOrderList()
})
</script>