package com.mall.cart.controller;

import com.mall.cart.service.CartService;
import com.mall.common.dto.CartAddDTO;
import com.mall.common.dto.CartVO;
import com.mall.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    private static final Long DEFAULT_USER_ID = 1L;
//    @PostMapping("/add")
//    public Result<Void> addToCart(@RequestBody Map<String, Object> params) {
//        Long userId = params.containsKey("userId") ?
//                Long.valueOf(params.get("userId").toString()) : DEFAULT_USER_ID;
//        Long productId = Long.valueOf(params.get("productId").toString());
//        Integer quantity = Integer.valueOf(params.get("quantity").toString());
//        cartService.addToCart(userId, productId, quantity);
//        return Result.success("添加成功", null);
//    }
@PostMapping("/add")
public Result<Void> addToCart(@Valid @RequestBody CartAddDTO dto) {
    Long userId = dto.getUserId() != null ? dto.getUserId() : DEFAULT_USER_ID;
    cartService.addToCart(userId, dto.getProductId(), dto.getQuantity());
    return Result.success("添加成功", null);
}
    /**
     * 获取购物车详情（核心功能2）
     */
    @GetMapping("/detail")
    public Result<CartVO> getCartDetail() {
        return Result.success(cartService.getCartDetail(DEFAULT_USER_ID));
    }

    @PutMapping("/update")
    public Result<Void> updateQuantity(@RequestBody Map<String, Object> params) {
        Long cartId = Long.valueOf(params.get("cartId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        cartService.updateQuantity(cartId, quantity);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/remove/{cartId}")
    public Result<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return Result.success("清空成功", null);
    }

    @GetMapping("/detail/{userId}")
    public Result<Map<String, Object>> getCartDetailByUserId(@PathVariable Long userId) {
        CartVO cartVO = cartService.getCartDetail(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("items", cartVO.getItems());
        result.put("totalAmount", cartVO.getTotalAmount());
        result.put("totalCount", cartVO.getTotalCount());
        return Result.success(result);
    }

}