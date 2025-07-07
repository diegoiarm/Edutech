package com.edutech.grades.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.edutech.common.dto.UserDTO;

@FeignClient(name = "ms-users")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDTO findById(@PathVariable("id") Integer id);
} 