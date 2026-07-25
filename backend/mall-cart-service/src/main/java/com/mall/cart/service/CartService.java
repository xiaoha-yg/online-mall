package com.mall.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.cart.feign.ProductFeignClient;
import com.mall.cart.mapper.CartMapper;
import com.mall.common.dto.CartVO;
import com.mall.common.dto.Result;
import com.mall.common.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        Result<Integer> stockResult = productFeignClient.getStock(productId);
        if (stockResult.getCode() != 200 || stockResult.getData() < quantity) {
            throw new RuntimeException("库存不足");
        }

        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("product_id", productId);
        CartItem existItem = cartMapper.selectOne(wrapper);

        if (existItem != null) {
            existItem.setQuantity(existItem.getQuantity() + quantity);
            cartMapper.updateById(existItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartMapper.insert(cartItem);
        }
    }

    public CartVO getCartDetail(Long userId) {
        List<CartItem> cartItems = cartMapper.selectCartDetailByUserId(userId);

        CartVO cartVO = new CartVO();
        List<CartVO.CartItemDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCount = 0;

        // 商品图片映射
        Map<Long, String> imageMap = new HashMap<>();
        imageMap.put(1L, "/images/iphone15.jpg");
        imageMap.put(2L, "/images/macbook.jpg");
        imageMap.put(3L, "/images/ipad.jpg");
        imageMap.put(4L, "/images/watch.jpg");
        imageMap.put(5L, "/images/airpods.jpg");

        for (CartItem item : cartItems) {
            CartVO.CartItemDTO dto = new CartVO.CartItemDTO();
            dto.setCartId(item.getId());
            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setItemTotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            dto.setImageUrl(imageMap.getOrDefault(item.getProductId(), ""));

            Result<Integer> stockResult = productFeignClient.getStock(item.getProductId());
            dto.setStock(stockResult.getData());

            itemDTOs.add(dto);
            totalAmount = totalAmount.add(dto.getItemTotal());
            totalCount += item.getQuantity();
        }

        cartVO.setItems(itemDTOs);
        cartVO.setTotalAmount(totalAmount);
        cartVO.setTotalCount(totalCount);
        return cartVO;
    }

    @Transactional
    public void updateQuantity(Long cartId, Integer quantity) {
        CartItem cartItem = cartMapper.selectById(cartId);
        if (cartItem == null) {
            throw new RuntimeException("购物车项不存在");
        }

        Result<Integer> stockResult = productFeignClient.getStock(cartItem.getProductId());
        if (stockResult.getData() < quantity) {
            throw new RuntimeException("库存不足");
        }

        cartItem.setQuantity(quantity);
        cartMapper.updateById(cartItem);
    }

    public void removeFromCart(Long cartId) {
        cartMapper.deleteById(cartId);
    }

    public void clearCart(Long userId) {
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        cartMapper.delete(wrapper);
    }
}