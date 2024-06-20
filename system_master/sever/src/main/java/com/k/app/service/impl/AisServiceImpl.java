package com.k.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k.app.entity.Ais;
import com.k.app.entity.Ship;
import com.k.app.mapper.AisMapper;
import com.k.app.mapper.ShipMapper;
import com.k.app.service.AisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 声明
public class AisServiceImpl extends ServiceImpl<AisMapper, Ais> implements AisService{
    @Autowired
    AisMapper aisMapper;
    @Autowired
    ShipMapper shipMapper;

    @Override
    public ResponseEntity<List<Ais>> getAllAis() { // 获取所有的ais
        List<Ais> aisList = aisMapper.selectList(null); // 获取所有
        return ResponseEntity.ok(aisList); // 返回
    }

    @Override
    public ResponseEntity<List<Ais>> getAis(String mmsi) { // 未使用
        QueryWrapper<Ais> queryWrapper = Wrappers.query();
        queryWrapper.like("mmsi", mmsi); // 模糊查找

        List<Ais> aisList = aisMapper.selectList(queryWrapper); // 获取所有
        return ResponseEntity.ok(aisList);
    }

    @Override
    public ResponseEntity<String> uploadAis(List<Ais> aisList) {
        for(Ais ais : aisList) {
//            QueryWrapper<Ais> exsitWapper = Wrappers.query();
//            exsitWapper.eq("mmsi", ais.getMmsi())
//                    .eq("updatetimestamp", ais.getUpdatetimestamp()); // 这两项可以标识唯一
//            Ais existAis = aisMapper.selectOne(exsitWapper); // 查找

            if(true) {
                try {
                    aisMapper.insert(ais);
                    Ship shipInsert = new Ship(ais.getMmsi(),ais.getType()); // type
                    shipMapper.insert(shipInsert);
                }
                catch (Exception e) {
                    // 插入异常就跳过，速度会提升，添加索引是目前速度最快的方式
                }
//                aisMapper.insert(ais);
//
//                // 去重 速度太慢********************************************
//                QueryWrapper<Ship> queryWrapper = Wrappers.query();
//                queryWrapper.eq("mmsi",ais.getMmsi()); // 精确
//
//                Ship ship = shipMapper.selectOne(queryWrapper);
//                if(ship == null) {
//                    // 没找到，插入
//                    Ship shipInsert = new Ship(ais.getMmsi(),ais.getType()); // type
//                    shipMapper.insert(shipInsert);
//                }
//                // *******************************************
//
//                Ship shipInsert = new Ship(ais.getMmsi(),ais.getType()); // type
//                shipMapper.insert(shipInsert);
            }
        }
        return ResponseEntity.ok("ok");
    }
}
