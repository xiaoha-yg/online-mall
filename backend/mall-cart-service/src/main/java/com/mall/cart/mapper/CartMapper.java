package com.mall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.common.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<CartItem> {

    @Select("SELECT c.id, c.user_id, c.product_id, c.quantity, c.create_time, c.update_time, " +
            "p.name as product_name, p.price, p.image_url " +
            "FROM cart c " +
            "LEFT JOIN mall_product.product p ON c.product_id = p.id " +
            "WHERE c.user_id = #{userId}")
    List<CartItem> selectCartDetailByUserId(@Param("userId") Long userId);
}