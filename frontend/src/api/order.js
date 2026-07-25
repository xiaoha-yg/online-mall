import request from '@/utils/request'

export function createOrder(data) {
  return request({ url: '/order/create', method: 'post', data })
}

export function getOrderList(params) {
  return request({ url: '/order/list', method: 'get', params })
}

export function getOrderDetail(orderNo) {
  return request({ url: `/order/detail/${orderNo}`, method: 'get' })
}

export function cancelOrder(orderNo) {
  return request({ url: `/order/cancel/${orderNo}`, method: 'put' })
}

export function completeOrder(orderNo) {
  return request({ url: `/order/complete/${orderNo}`, method: 'put' })
}