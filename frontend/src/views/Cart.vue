<template>
  <div>
    <h2 style="margin-bottom: 20px;">🛒 我的购物车</h2>
    
    <el-card v-if="cart.items && cart.items.length > 0">
      <el-table :data="cart.items" style="width: 100%;">
        <el-table-column label="商品" min-width="300">
          <template #default="scope">
            <div style="display: flex; align-items: center;">
              <el-image :src="scope.row.imageUrl || 'https://via.placeholder.com/80'" style="width: 80px; height: 80px; border-radius: 8px; margin-right: 15px;" fit="cover" />
              <div>
                <p style="font-weight: bold; margin-bottom: 5px;">{{ scope.row.productName }}</p>
                <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.price }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="150">
          <template #default="scope">
            <el-input-number 
              v-model="scope.row.quantity" 
              :min="1" 
              :max="scope.row.stock"
              @change="handleUpdateQuantity(scope.row.cartId, scope.row.quantity)" 
            />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="150">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold; font-size: 16px;">¥{{ scope.row.itemTotal }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="danger" @click="handleRemove(scope.row.cartId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="display: flex; justify-content: flex-end; align-items: center; margin-top: 20px; padding-top: 20px; border-top: 1px solid #EBEEF5;">
        <span style="margin-right: 30px;">
          已选 <b style="color: #409EFF;">{{ cart.items.length }}</b> 件商品，共 <b style="color: #409EFF;">{{ cart.totalCount }}</b> 件
        </span>
        <h2 style="margin: 0 20px; color: #f56c6c;">合计：¥{{ cart.totalAmount }}</h2>
        <el-button type="success" size="large" @click="$router.push('/checkout')">去结算</el-button>
      </div>
    </el-card>

    <el-empty v-else description="购物车空空如也，快去选购吧~">
      <el-button type="primary" @click="$router.push('/')">去购物</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCartList, updateCartItem, removeCartItem } from '@/api/cart'
import { ElMessage, ElMessageBox } from 'element-plus'

const cart = ref({ items: [], totalAmount: 0, totalCount: 0 })

const loadCart = async () => {
  try { cart.value = await getCartList() } catch {}
}

const handleUpdateQuantity = async (cartId, quantity) => {
  await updateCartItem({ cartId, quantity })
  await loadCart()
}

const handleRemove = async (cartId) => {
  await ElMessageBox.confirm('确定要删除该商品吗？', '提示', { type: 'warning' })
  await removeCartItem(cartId)
  await loadCart()
  ElMessage.success('已删除')
}

onMounted(loadCart)
</script>