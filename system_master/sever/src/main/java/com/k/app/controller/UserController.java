package com.k.app.controller;

import com.k.app.entity.User;
import com.k.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired // 依赖注入
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User loginform){
        String message=userService.login(loginform);
        return message;
    }
    @PostMapping("/register")
    public String register(@RequestBody User reUser){
        String message=userService.register(reUser);
        return message;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> processData(@RequestBody Map<String, Object> requestBody) {
        String userName = (String) requestBody.get("username");
        String oldPassword = (String) requestBody.get("old");
        String newPassword = (String) requestBody.get("new");

        String message=userService.changePassword(userName,oldPassword, newPassword);

        return ResponseEntity.ok(message);
    }
}