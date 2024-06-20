package com.k.app.controller;

import ch.qos.logback.core.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k.app.entity.User;
import com.k.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    UserService userService;

    @PostMapping("/test") // 接口映射
    String test(@RequestBody User user) throws IOException {

        String msg = userService.register(user); // 注册
        return msg;
    }

    @Value("${upload-dir}")
    private String uploadDir;

    @GetMapping("/testResources") // 接口映射
    String testResources() throws IOException {
        return uploadDir;
    }
}
