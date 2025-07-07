package com.edutech.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.edutech.courses.client")
@SpringBootApplication
public class ClassroomCoursesModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomCoursesModuleApplication.class, args);
	}

}
