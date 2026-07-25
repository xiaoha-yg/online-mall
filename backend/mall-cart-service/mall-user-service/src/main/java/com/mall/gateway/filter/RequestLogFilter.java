package com.mall.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局过滤器 - 记录每次请求的路径和耗时
 */
@Component
@Slf4j
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long costTime = System.currentTimeMillis() - startTime;
            log.info("请求方式: {}, 请求路径: {}, 耗时: {}ms", method, path, costTime);
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}