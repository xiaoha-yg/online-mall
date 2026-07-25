package com.mall.product.controller;

import com.mall.common.config.RedisConfig;
import com.mall.common.dto.Result;
import com.mall.common.dto.StockDeductDTO;
import com.mall.common.entity.Product;
import com.mall.common.utils.RedisUtils;
import com.mall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> list() {
        return Result.success(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    /**
     * 实时查询库存（核心功能1）
     */
    @GetMapping("/stock/{id}")
    public Result<Integer> getStock(@PathVariable Long id) {
        return Result.success(productService.getRealTimeStock(id));
    }

    /**
     * 扣减库存（供订单服务调用）
     */
    @PostMapping("/stock/deduct")
    public Result<Boolean> deductStock(@RequestBody StockDeductDTO dto) {
        return Result.success(productService.deductStock(dto.getProductId(), dto.getQuantity()));
    }

    @Autowired
    private RedisUtils redisUtils;
    @GetMapping("/test-redis")
    public String testRedis() {
        // 写入缓存
        redisUtils.set("mall:product:test", "Hello Redis", 60, TimeUnit.SECONDS);
        // 读取缓存
        Object value = redisUtils.get("mall:product:test");
        return "Redis 返回值: " + value;
    }

    /**
     * 添加商品
     */
    @PostMapping("/add")
    public Result<Void> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.success("添加成功", null);
    }

    /**
     * 修改商品
     */
    @PutMapping("/update")
    public Result<Void> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.success("更新成功", null);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success("删除成功", null);
    }
}