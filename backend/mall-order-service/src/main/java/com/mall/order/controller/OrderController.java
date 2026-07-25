package com.mall.order.controller;
import org.springframework.web.bind.annotation.PathVariable;
import com.mall.common.dto.OrderCreateDTO;
import com.mall.common.dto.OrderDTO;
import com.mall.common.dto.Result;
import com.mall.common.entity.Order;
import com.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Long DEFAULT_USER_ID = 1L;

    /**
     * 创建订单（核心功能3）
     */
    @PostMapping("/create")
    public Result<Order> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        // 将 OrderCreateDTO 转换为 OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(dto.getUserId() != null ? dto.getUserId() : DEFAULT_USER_ID);
        orderDTO.setCartIds(dto.getCartIds());
        return Result.success("下单成功", orderService.createOrder(orderDTO));
    }

    @GetMapping("/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderDetail(orderId));
    }

    @GetMapping("/list")
    public Result<List<Order>> getUserOrders() {
        return Result.success(orderService.getUserOrders(DEFAULT_USER_ID));
    }

    @PutMapping("/cancel/{orderNo}")
    public Result<Void> cancelOrder(@PathVariable String orderNo) {
        orderService.cancelByOrderNo(orderNo);
        return Result.success("订单已取消", null);
    }

    @GetMapping("/detail/{orderNo}")
    public Result<Order> getOrderDetailByOrderNo(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByOrderNo(orderNo));
    }

    @PutMapping("/complete/{orderNo}")
    public Result<Void> completeOrder(@PathVariable String orderNo) {
        orderService.completeByOrderNo(orderNo);
        return Result.success("订单已完成", null);
    }

}

