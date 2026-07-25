import request from '@/utils/request'

export function getCartList() {
  return request({ url: '/cart/detail', method: 'get' })
}

export function addToCart(data) {
  return request({ url: '/cart/add', method: 'post', data })
}

export function updateCartItem(data) {
  return request({ url: '/cart/update', method: 'put', data })
}

export function removeCartItem(cartId) {
  return request({ url: `/cart/remove/${cartId}`, method: 'delete' })
}

export function clearCart() {
  return request({ url: '/cart/clear', method: 'delete' })
}