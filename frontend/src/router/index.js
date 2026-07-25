import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/MainLayout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('@/views/Home.vue'), meta: { title: '商品列表' } },
      { path: 'product/:id', name: 'ProductDetail', component: () => import('@/views/ProductDetail.vue'), meta: { title: '商品详情' } },
      { path: 'cart', name: 'Cart', component: () => import('@/views/Cart.vue'), meta: { title: '购物车' } },
      { path: 'checkout', name: 'Checkout', component: () => import('@/views/Checkout.vue'), meta: { title: '确认订单' } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/OrderList.vue'), meta: { title: '我的订单' } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/OrderDetail.vue'), meta: { title: '订单详情' } },
      { path: 'user', name: 'UserCenter', component: () => import('@/views/UserCenter.vue'), meta: { title: '个人中心' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/views/layout/AdminLayout.vue'),
    meta: { requireAuth: true, role: 'admin' },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '数据看板' } },
      { path: 'products', name: 'ProductManage', component: () => import('@/views/admin/ProductManage.vue'), meta: { title: '商品管理' } },
      { path: 'orders', name: 'OrderManage', component: () => import('@/views/admin/OrderManage.vue'), meta: { title: '订单管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 在线商城` : '在线商城系统'
  const token = getToken()
  if (to.path === '/login' || to.path === '/register') {
    if (token) next('/')
    else next()
  } else {
    next()
  }
})

export default router