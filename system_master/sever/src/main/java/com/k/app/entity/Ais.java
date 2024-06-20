package com.k.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Ais {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String aisname;
    private String callsign;
    private String an;
    private String color;
    private double course;
    private String destination;
    private String dn;
    private String dwt;
    private String eta;
    private String fn;
    private double heading;
    private String hide;
    private int imonumber;
    private double lat;
    private double length;
    private double lon;
    private String minotypecode;
    private String minotype;
    private String minudiff;
    private String mmsi;
    private String name;
    private int offseta;
    private int offsetb;
    private int offsetc;
    private int offsetd;
    private int rs;
    private double speed;
    private String status;
    private String statuscode;
    private String trawlsonar;
    private String turnrate;
    private String type;
    private String typecode;
    private String updatetimeinfo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC") // 时间格式
    private Date updatetime;

    private String updatetimestamp; // 序列
    private double width;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC") // 时间格式
    private Date acqtime;

}