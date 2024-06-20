package com.k.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k.app.entity.User;


public interface UserService extends IService<User> {
    public String login(User user);
    public String register(User user);

    String changePassword(String userName, String oldPassword, String newPassword);
}
