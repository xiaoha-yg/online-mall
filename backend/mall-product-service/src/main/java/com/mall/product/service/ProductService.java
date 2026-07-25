package com.mall.product.service;

import com.mall.common.entity.Product;
import com.mall.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final String PRODUCT_KEY_PREFIX = "product:";

    public List<Product> getAllProducts() {
        return productMapper.selectList(null);
    }

    public Product getProductById(Long id) {
        String key = PRODUCT_KEY_PREFIX + id;
        Product product = (Product) redisTemplate.opsForValue().get(key);
        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
            }
        }
        return product;
    }

    /**
     * 实时查询库存（核心功能1）
     */
    public Integer getRealTimeStock(Long productId) {
        String stockKey = STOCK_KEY_PREFIX + productId;
        Integer stock = (Integer) redisTemplate.opsForValue().get(stockKey);
        if (stock == null) {
            Product product = productMapper.selectById(productId);
            stock = (product != null) ? product.getStock() : 0;
            redisTemplate.opsForValue().set(stockKey, stock, 10, TimeUnit.SECONDS);
        }
        return stock;
    }

    /**
     * 扣减库存（数据库行锁防止超卖）
     */
    @Transactional
    public boolean deductStock(Long productId, Integer quantity) {
        int rows = productMapper.deductStock(productId, quantity);
        if (rows > 0) {
            String stockKey = STOCK_KEY_PREFIX + productId;
            Integer currentStock = (Integer) redisTemplate.opsForValue().get(stockKey);
            if (currentStock != null) {
                redisTemplate.opsForValue().set(stockKey, currentStock - quantity, 10, TimeUnit.SECONDS);
            }
            return true;
        }
        return false;
    }

    /**
     * 添加商品
     */
    @Transactional
    public void addProduct(Product product) {
        // 如果前端传递了 ID，使用前端的 ID；否则自动生成
        if (product.getId() != null && product.getId() > 0) {
            // 手动指定 ID
            productMapper.insert(product);
        } else {
            // 自动生成 ID
            productMapper.insert(product);
        }
        redisTemplate.delete(PRODUCT_KEY_PREFIX + product.getId());
    }

    /**
     * 修改商品
     */
    @Transactional
    public void updateProduct(Product product) {
        productMapper.updateById(product);
        // 清除相关缓存
        redisTemplate.delete(PRODUCT_KEY_PREFIX + product.getId());
        redisTemplate.delete(STOCK_KEY_PREFIX + product.getId());
    }

    /**
     * 删除商品
     */
    @Transactional
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
        // 清除该商品的缓存
        redisTemplate.delete(PRODUCT_KEY_PREFIX + id);
        redisTemplate.delete(STOCK_KEY_PREFIX + id);
    }
}