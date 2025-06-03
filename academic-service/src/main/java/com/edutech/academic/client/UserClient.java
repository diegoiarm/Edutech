package com.edutech.academic.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.edutech.common.dto.UserDTO;

@FeignClient(name = "ms-user", url = "http://localhost:9001/api/users")
public interface UserClient {
    @GetMapping("/{id}")
    UserDTO findById(@PathVariable("id") Integer id);
}
