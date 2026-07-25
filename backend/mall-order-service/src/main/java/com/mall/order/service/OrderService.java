package com.mall.order.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.hutool.core.util.IdUtil;
import com.mall.common.dto.OrderDTO;
import com.mall.common.dto.Result;
import com.mall.common.dto.StockDeductDTO;
import com.mall.common.entity.Order;
import com.mall.common.entity.OrderItem;
import com.mall.order.feign.CartFeignClient;
import com.mall.order.feign.StockFeignClient;
import com.mall.order.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StockFeignClient stockFeignClient;

    @Autowired
    private CartFeignClient cartFeignClient;

    /**
     * 创建订单（核心功能3 - 保证事务一致性）
     */
    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderDTO orderDTO) {
        Long userId = orderDTO.getUserId();

        // 1. 通过 Feign 获取购物车详情
        Result<Map<String, Object>> cartResult = cartFeignClient.getCartDetail(userId);
        if (cartResult.getCode() != 200 || cartResult.getData() == null) {
            throw new RuntimeException("获取购物车失败");
        }

        Map<String, Object> cartData = cartResult.getData();
        List<Map<String, Object>> items = (List<Map<String, Object>>) cartData.get("items");

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 2. 计算总金额并验证库存
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            String productName = (String) item.get("productName");
            BigDecimal price = new BigDecimal(item.get("price").toString());
            Integer quantity = (Integer) item.get("quantity");

            // ✅ 改成 stockFeignClient
            Result<Integer> stockResult = stockFeignClient.getStock(productId);
            if (stockResult.getCode() != 200 || stockResult.getData() == null) {
                throw new RuntimeException("获取商品库存失败");
            }

            Integer stock = stockResult.getData();
            if (stock < quantity) {
                throw new RuntimeException("商品【" + productName + "】库存不足");
            }

            BigDecimal itemTotal = price.multiply(new BigDecimal(quantity));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setProductName(productName);
            orderItem.setPrice(price);
            orderItem.setQuantity(quantity);
            orderItem.setTotalPrice(itemTotal);
            orderItems.add(orderItem);
        }

        // 3. 创建订单
        Order order = new Order();
        order.setOrderNo(IdUtil.fastSimpleUUID());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        orderMapper.insert(order);

        // 4. 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderMapper.insertOrderItem(item);
        }

        // 5. 远程调用扣减库存
        for (OrderItem item : orderItems) {
            StockDeductDTO deductDTO = new StockDeductDTO();
            deductDTO.setProductId(item.getProductId());
            deductDTO.setQuantity(item.getQuantity());

            Result<Boolean> result = stockFeignClient.deductStock(deductDTO);
            if (result.getCode() != 200 || !result.getData()) {
                throw new RuntimeException("扣减库存失败");
            }
        }

        // 6. 远程调用清空购物车
        Result<Void> clearResult = cartFeignClient.clearCart(userId);
        if (clearResult.getCode() != 200) {
            throw new RuntimeException("清空购物车失败");
        }

        order.setItems(orderItems);
        return order;
    }

    public Order getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            List<OrderItem> items = orderMapper.selectOrderItemsByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("create_time");
        List<Order> orders = orderMapper.selectList(wrapper);

        // 为每个订单查询订单项
        for (Order order : orders) {
            List<OrderItem> items = orderMapper.selectOrderItemsByOrderId(order.getId());
            order.setItems(items);
        }

        return orders;
    }

    public void cancelOrder(Long orderId) {
        // 先用 ID 查，找不到再用 orderNo 查
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            // 如果传入的是 orderNo（字符串转过来的），尝试按 order_no 查询
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderId.toString());
            order = orderMapper.selectOne(wrapper);
        }
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(2);
        orderMapper.updateById(order);
    }

    public void cancelByOrderNo(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(2);
        orderMapper.updateById(order);
    }

    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(wrapper);
        if (order != null) {
            List<OrderItem> items = orderMapper.selectOrderItemsByOrderId(order.getId());
            order.setItems(items);
        }
        return order;
    }

    public void completeByOrderNo(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(wrapper);
        if (order != null) {
            order.setStatus(1);
            orderMapper.updateById(order);
        }
    }

}