package com.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@SpringBootApplication(scanBasePackages = {"com.mall.product", "com.mall.common"})
@EnableDiscoveryClient
@MapperScan("com.mall.product.mapper")
@SpringBootApplication(scanBasePackages = "com.mall")
public class MallProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductServiceApplication.class, args);
        System.out.println("========================================");
        System.out.println("商品服务启动成功！端口: 9001");
        System.out.println("========================================");
    }
}