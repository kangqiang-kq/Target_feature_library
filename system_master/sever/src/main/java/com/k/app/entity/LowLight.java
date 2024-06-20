package com.k.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.beans.Transient;

@Data
public class LowLight { // 裁剪后的小图像
    @TableId(type = IdType.AUTO)
    private Long id;
    private String mmsi;

    private String source; // 来源 ---> 大图像标识 ID

    private String imgpath; // 存储地址

    private String position; // 经纬度信息
    public LowLight() {
    }

    public LowLight(String mmsi, String source, String imgpath, String position) {
        this.mmsi = mmsi;
        this.source = source;
        this.imgpath = imgpath;
        this.position = position;
    }
}
