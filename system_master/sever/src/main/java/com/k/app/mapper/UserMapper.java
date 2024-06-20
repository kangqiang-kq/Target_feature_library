package com.k.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.k.app.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
    // 映射xml文件的
    // mybitsPlus, 大部分操作不需要写 sql
}
