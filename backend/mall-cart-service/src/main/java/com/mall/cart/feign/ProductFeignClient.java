package com.mall.cart.feign;

import com.mall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 //FeignClient服务名，Feign自动从Nacos获取该服务地址，并完成HTTP请求 的发送
//通过调用Spring Cloub 的OpenFeign让微服务之间通信
@FeignClient(name = "mall-product-service")
public interface ProductFeignClient {

    @GetMapping("/product/stock/{id}")
    Result<Integer> getStock(@PathVariable("id") Long id);
}