package com.edutech.payment.client;

import com.edutech.common.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", path = "/api/users")
public interface UserClient {
    @GetMapping("/{id}")
    UserDTO findById(@PathVariable Integer id);
} 