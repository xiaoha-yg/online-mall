package com.mall.order.feign;

import com.mall.common.dto.Result;
import com.mall.common.dto.StockDeductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mall-product-service")
public interface StockFeignClient {

    @PostMapping("/product/stock/deduct")
    Result<Boolean> deductStock(@RequestBody StockDeductDTO dto);

    @GetMapping("/product/stock/{id}")
    Result<Integer> getStock(@PathVariable("id") Long id);
}