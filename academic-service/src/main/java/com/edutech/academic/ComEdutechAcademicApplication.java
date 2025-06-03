package com.edutech.academic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.edutech.academic.client")
@SpringBootApplication
public class ComEdutechAcademicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComEdutechAcademicApplication.class, args);
	}

}
