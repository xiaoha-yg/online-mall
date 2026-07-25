package com.mall.common.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 创建订单请求 DTO
 */
@Data
public class OrderCreateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "购物车ID列表不能为空")
    @Size(min = 1, message = "至少选择一个商品")
    private List<Long> cartIds;
}