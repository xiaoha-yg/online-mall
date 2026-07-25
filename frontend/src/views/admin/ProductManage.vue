<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>📦 商品管理</h2>
      <el-button type="primary" @click="handleAdd">添加商品</el-button>
    </div>

    <el-table :data="products" style="width: 100%;" stripe>
      <el-table-column prop="id" label="ID" width="120" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '添加商品'" width="500px">
      <el-form :model="form" label-width="80px">
        <!-- 新增商品时显示 ID 输入框 -->
        <el-form-item label="商品ID" v-if="!isEdit">
          <el-input-number v-model="form.id" :min="1" style="width: 100%;" placeholder="不填则自动生成" />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL，如 /images/xxx.jpg" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductList, addProduct, updateProduct, deleteProduct } from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const products = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ name: '', price: 0, stock: 0, description: '' })

const loadProducts = async () => {
  const list = await getProductList()
  products.value = []
  await new Promise(resolve => setTimeout(resolve, 50))
  products.value = list || []
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, name: '', price: 0, stock: 0, description: '', imageUrl: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  const data = { ...form.value }
  if (!data.id) {
    delete data.id
  }
  
  if (isEdit.value) {
    await updateProduct(data)
    ElMessage.success('商品已更新')
  } else {
    await addProduct(data)
    ElMessage.success('商品已添加')
  }
  dialogVisible.value = false
  await loadProducts()
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', { type: 'warning' })
    await deleteProduct(id)
    ElMessage.success('商品已删除')
    await loadProducts()
  } catch (error) {
    // 用户取消
  }
}

onMounted(loadProducts)
</script>