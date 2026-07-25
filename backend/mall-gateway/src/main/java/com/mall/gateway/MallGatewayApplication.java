package com.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MallGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallGatewayApplication.class, args);
		System.out.println("========================================");
		System.out.println("网关服务启动成功！端口: 9000");
		System.out.println("========================================");
	}
}