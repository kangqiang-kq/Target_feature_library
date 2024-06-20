package com.k.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
// 同名自动映射
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id; // 自动递增编号, 对应数据库是bigint类型

    private String username; // 账号名
    private String password; // 密码
}
