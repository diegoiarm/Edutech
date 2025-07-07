package com.edutech.courses.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.edutech.common.dto.UserDTO;

// Como estoy usando Eureka no necesito especificar el puerto en la Feign Client, por lo que:
// Puedo usar: @FeignClient(name = "ms-users") 
// en vez de:  @FeignClient(name = "ms-users", url = "http://localhost:9001/api/users")
@FeignClient(name = "ms-users") 
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDTO findById(@PathVariable("id") Integer id);
}
