package com.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.mall.order", "com.mall.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.mall.order.mapper")
public class MallOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallOrderServiceApplication.class, args);
		System.out.println("========================================");
		System.out.println("订单服务启动成功！端口: 9003");
		System.out.println("========================================");
	}

}
