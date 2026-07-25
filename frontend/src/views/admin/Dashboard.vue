<!-- <template>
  <div>
    <h2>📊 数据看板</h2>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" style="text-align: center;">
          <h1 style="color: #409EFF;">{{ stats.totalOrders }}</h1>
          <p>总订单数</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="text-align: center;">
          <h1 style="color: #67C23A;">{{ stats.totalProducts }}</h1>
          <p>商品数量</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="text-align: center;">
          <h1 style="color: #E6A23C;">{{ stats.totalUsers }}</h1>
          <p>用户数量</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="text-align: center;">
          <h1 style="color: #F56C6C;">¥{{ stats.totalRevenue }}</h1>
          <p>总收入</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const stats = ref({
  totalOrders: 156,
  totalProducts: 45,
  totalUsers: 89,
  totalRevenue: '128,960'
})
</script> -->



<template>
  <div>
    <h2 style="margin-bottom: 20px;">📊 数据看板</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: 30px;">
      <el-col :span="6" v-for="(item, index) in statsCards" :key="index">
        <el-card 
          shadow="hover" 
          :body-style="{ padding: '25px', cursor: 'pointer' }"
          @click="handleCardClick(item)"
        >
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div>
              <p style="color: #909399; font-size: 14px; margin: 0;">{{ item.title }}</p>
              <h1 :style="{ color: item.color, margin: '10px 0', fontSize: '32px' }">
                <span ref="countRefs">{{ animatedValues[index] }}</span>
              </h1>
              <p style="color: #909399; font-size: 12px; margin: 0;">
                <span :style="{ color: item.trend > 0 ? '#67C23A' : '#F56C6C' }">
                  {{ item.trend > 0 ? '↑' : '↓' }} {{ Math.abs(item.trend) }}%
                </span>
                较上周
              </p>
            </div>
            <div :style="{ 
              width: '64px', 
              height: '64px', 
              borderRadius: '12px', 
              background: item.bgColor,
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center',
              fontSize: '30px'
            }">
              {{ item.icon }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-bottom: 30px;">
      <!-- 订单趋势图 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-weight: bold;">📈 订单趋势（近7天）</span>
              <el-tag size="small">实时更新</el-tag>
            </div>
          </template>
          <div style="height: 300px; position: relative;">
            <!-- 柱状图模拟 -->
            <div style="display: flex; align-items: flex-end; justify-content: space-around; height: 100%; padding: 20px 0;">
              <div v-for="(item, index) in chartData" :key="index" 
                style="display: flex; flex-direction: column; align-items: center; width: 12%;">
                <div 
                  :style="{
                    width: '100%',
                    height: item.value * 3 + 'px',
                    background: `linear-gradient(180deg, ${item.color} 0%, ${item.color}88 100%)`,
                    borderRadius: '6px 6px 0 0',
                    transition: 'height 1.5s cubic-bezier(0.4, 0, 0.2, 1)',
                    boxShadow: `0 2px 8px ${item.color}44`
                  }"
                ></div>
                <p style="font-size: 12px; color: #909399; margin-top: 8px;">{{ item.label }}</p>
                <p style="font-size: 12px; font-weight: bold; color: #303133;">{{ item.displayValue }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 商品分类占比 -->
      <el-col :span="8">
        <el-card shadow="hover" style="height: 100%;">
          <template #header>
            <span style="font-weight: bold;">📦 商品分类占比</span>
          </template>
          <div style="height: 300px; display: flex; flex-direction: column; justify-content: center;">
            <div v-for="(item, index) in categoryData" :key="index" 
              style="display: flex; align-items: center; margin-bottom: 20px;">
              <span style="width: 80px; font-size: 13px;">{{ item.name }}</span>
              <div style="flex: 1; height: 20px; background: #f5f7fa; border-radius: 10px; margin: 0 10px; overflow: hidden;">
                <div 
                  :style="{
                    width: item.percent + '%',
                    height: '100%',
                    background: item.color,
                    borderRadius: '10px',
                    transition: 'width 2s ease-in-out'
                  }"
                ></div>
              </div>
              <span style="font-size: 13px; font-weight: bold; width: 45px;">{{ item.percent }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新订单 -->
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold;">📋 最新订单</span>
          <el-button size="small" @click="$router.push('/admin/orders')">查看全部</el-button>
        </div>
      </template>
      <el-table :data="recentOrders" stripe>
        <el-table-column prop="orderNo" label="订单号" min-width="200" />
        <el-table-column label="金额" width="120">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="时间" width="160" />
      </el-table>
    </el-card>

    <!-- 实时时间 -->
    <div style="text-align: center; margin-top: 20px; color: #909399; font-size: 13px;">
      数据更新时间：{{ currentTime }}
      <el-button size="small" text @click="refreshData">🔄 刷新数据</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// 当前时间
const currentTime = ref('')

// 统计卡片数据
const statsCards = ref([
  { title: '总订单数', value: 156, icon: '📋', color: '#409EFF', bgColor: '#ecf5ff', trend: 12 },
  { title: '商品数量', value: 45, icon: '📦', color: '#67C23A', bgColor: '#f0f9eb', trend: 8 },
  { title: '用户数量', value: 89, icon: '👥', color: '#E6A23C', bgColor: '#fdf6ec', trend: 15 },
  { title: '总收入', value: 128960, icon: '💰', color: '#F56C6C', bgColor: '#fef0f0', trend: -5 }
])

// 动画数字
const animatedValues = ref([0, 0, 0, 0])

// 图表数据
const chartData = ref([
  { label: '周一', value: 80, displayValue: '80', color: '#409EFF' },
  { label: '周二', value: 65, displayValue: '65', color: '#67C23A' },
  { label: '周三', value: 90, displayValue: '90', color: '#E6A23C' },
  { label: '周四', value: 70, displayValue: '70', color: '#F56C6C' },
  { label: '周五', value: 95, displayValue: '95', color: '#409EFF' },
  { label: '周六', value: 55, displayValue: '55', color: '#67C23A' },
  { label: '周日', value: 75, displayValue: '75', color: '#E6A23C' }
])

// 分类数据
const categoryData = ref([
  { name: '手机', percent: 35, color: '#409EFF' },
  { name: '电脑', percent: 25, color: '#67C23A' },
  { name: '平板', percent: 20, color: '#E6A23C' },
  { name: '穿戴', percent: 15, color: '#F56C6C' },
  { name: '配件', percent: 5, color: '#909399' }
])

// 最新订单
const recentOrders = ref([
  { orderNo: 'OD2026050512345678', amount: '17998', status: '已完成', time: '2026-05-05 14:30' },
  { orderNo: 'OD2026050512345679', amount: '8999', status: '待支付', time: '2026-05-05 13:15' },
  { orderNo: 'OD2026050512345680', amount: '2999', status: '已完成', time: '2026-05-05 11:00' },
  { orderNo: 'OD2026050512345681', amount: '4799', status: '已完成', time: '2026-05-05 09:45' }
])

// 数字动画
const animateNumbers = () => {
  statsCards.value.forEach((card, index) => {
    const target = card.value
    const duration = 2000
    const startTime = Date.now()
    const startValue = 0
    
    const timer = setInterval(() => {
      const elapsed = Date.now() - startTime
      const progress = Math.min(elapsed / duration, 1)
      const current = Math.floor(startValue + (target - startValue) * easeOutCubic(progress))
      animatedValues.value[index] = card.title === '总收入' ? '¥' + current.toLocaleString() : current.toLocaleString()
      
      if (progress >= 1) clearInterval(timer)
    }, 16)
  })
}

// 缓动函数
const easeOutCubic = (t) => 1 - Math.pow(1 - t, 3)

// 更新图表
const updateChart = () => {
  chartData.value = chartData.value.map(item => ({
    ...item,
    value: Math.floor(Math.random() * 60 + 40),
    displayValue: Math.floor(Math.random() * 60 + 40)
  }))
}

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN')
}

// 点击卡片
const handleCardClick = (item) => {
  if (item.title === '总订单数') {
    // 可以跳转到订单管理
  }
}

// 刷新数据
const refreshData = () => {
  animateNumbers()
  updateChart()
  updateTime()
}

let timer
onMounted(() => {
  updateTime()
  animateNumbers()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.el-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.el-card:hover {
  transform: translateY(-2px);
}
h1 {
  transition: color 0.3s ease;
}
</style>