package com.edutech.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ComEdutechPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComEdutechPaymentApplication.class, args);
    }
} 