package com.edutech.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.edutech.identity.client")
@SpringBootApplication
public class ComEdutechIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComEdutechIdentityApplication.class, args);
	}

}
