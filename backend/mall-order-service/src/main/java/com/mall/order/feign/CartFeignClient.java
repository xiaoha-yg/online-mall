package com.mall.order.feign;

import com.mall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "mall-cart-service")
public interface CartFeignClient {

    @GetMapping("/cart/detail/{userId}")
    Result<Map<String, Object>> getCartDetail(@PathVariable("userId") Long userId);

    @DeleteMapping("/cart/clear/{userId}")
    Result<Void> clearCart(@PathVariable("userId") Long userId);
}