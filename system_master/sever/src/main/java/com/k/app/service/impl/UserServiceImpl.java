package com.k.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.k.app.entity.User;
import com.k.app.mapper.UserMapper;
import com.k.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper mapper;
    @Override
    public String login(User user) {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        userQueryWrapper.like("username", user.getUsername());
        List<User> list = mapper.selectList(userQueryWrapper);
        if (list.size()!=0){
            String password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes()); // 转换md5进行验证
            if (list.get(0).getPassword().equals(password)){
                return "success";
            }else return "error";
        }else return "error";
    }

    @Override
    public String register(User user) {
        if (user!=null){
            boolean flag=true;
            for (User list:mapper.selectList(null)){
                if (list.getUsername().equals(user.getUsername()))
                    flag=false;
            }
            if (flag){
                String pw=DigestUtils.md5DigestAsHex(user.getPassword().getBytes()); // 转换为 md5 码
                user.setPassword(pw);
                int index=mapper.insert(user);
                if (index==1){return "success";}else return "error";
            }else return "repeat"; // 用户已存在，name不可以重复，因此也是唯一的
        }else return "error";
    }

    @Override
    public String changePassword(String userName, String oldPassword, String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName); // 以name进行查询
        User user = mapper.selectOne(queryWrapper);

        String oldMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        String newMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // md5 码匹配
        if (user != null && user.getPassword().equals(oldMd5)) {
            user.setPassword(newMd5); // 设置为新的MD5
            int rowsAffected = mapper.updateById(user); // 更新用户
            if (rowsAffected > 0) {
                return "Password changed successfully";
            }
        }
        return "Failed to change password";
    }
}
