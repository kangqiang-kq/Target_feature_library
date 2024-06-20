package com.k.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ThermalInfrared {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String mmsi;
    private String source; // 来源 ---> 大图像标识
    private String imgpath; // 存储地址
    private String position; // 经纬度信息

    public ThermalInfrared() {
    }

    public ThermalInfrared(String mmsi, String source, String imgpath, String position) {
        this.mmsi = mmsi;
        this.source = source;
        this.imgpath = imgpath;
        this.position = position;
    }
}
