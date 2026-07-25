package com.mall.common.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private Long userId;
    private List<Long> cartIds;
    private String remark;
}