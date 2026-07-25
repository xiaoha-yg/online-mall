package com.mall.common.dto;

import lombok.Data;

@Data
public class StockDeductDTO {
    private Long productId;
    private Integer quantity;
}