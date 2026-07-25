package com.mall.common.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVO {
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;
    private Integer totalCount;

    @Data
    public static class CartItemDTO {
        private Long cartId;
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal itemTotal;
        private Integer stock;
        private String imageUrl;
    }
}