package com.edutech.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.edutech.payment.client")
@SpringBootApplication
public class ClassroomPaymentsModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomPaymentsModuleApplication.class, args);
	}

}
