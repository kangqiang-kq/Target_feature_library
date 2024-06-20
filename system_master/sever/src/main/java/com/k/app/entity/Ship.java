package com.k.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Ship {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String mmsi;

    private String imgpath; // 展示图像地址

    private String type; // 类型

    public Ship(String mmsi) {
        this.mmsi = mmsi;
    }

    public Ship() {
    }

    public Ship(String mmsi,String type) {
        this.mmsi = mmsi;

        this.type = type;
    }
}
