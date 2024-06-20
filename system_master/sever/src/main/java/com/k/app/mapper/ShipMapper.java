package com.k.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k.app.entity.Ship;
import com.k.app.entity.User;
import org.springframework.stereotype.Repository;

@Repository // 数据访问层，自动装配
public interface ShipMapper extends BaseMapper<Ship> { // mybatis-plus
}
