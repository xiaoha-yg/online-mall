package com.mall.common.constant;

/**
 * 项目常量类
 */
public class MallConstants {

    /** 默认用户ID */
    public static final Long DEFAULT_USER_ID = 1L;

    /** Redis 库存缓存 TTL（秒） */
    public static final int STOCK_CACHE_TTL = 10;

    /** Redis 缓存 Key 前缀 */
    public static final String STOCK_KEY_PREFIX = "stock:";

    /** 商品缓存 Key 前缀 */
    public static final String PRODUCT_KEY_PREFIX = "product:";

    /** 订单状态：待支付 */
    public static final int ORDER_STATUS_PENDING = 0;

    /** 订单状态：已支付 */
    public static final int ORDER_STATUS_PAID = 1;

    /** 订单状态：已取消 */
    public static final int ORDER_STATUS_CANCEL = 2;
}