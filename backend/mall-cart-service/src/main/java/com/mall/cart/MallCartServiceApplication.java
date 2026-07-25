package com.mall.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.mall.cart", "com.mall.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.mall.cart.mapper")

public class MallCartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallCartServiceApplication.class, args);
		System.out.println("========================================");
		System.out.println("购物车服务启动成功！端口: 9002");
		System.out.println("========================================");
	}

}
