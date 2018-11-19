package cn.mauth.safe.shop.SafeShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class SafeShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeShopApplication.class, args);
	}
}
