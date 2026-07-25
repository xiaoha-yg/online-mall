package com.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.common.entity.Order;
import com.mall.common.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    int insertOrderItem(OrderItem orderItem);

    int batchInsertOrderItems(@Param("items") List<OrderItem> items);

    List<OrderItem> selectOrderItemsByOrderId(@Param("orderId") Long orderId);
}