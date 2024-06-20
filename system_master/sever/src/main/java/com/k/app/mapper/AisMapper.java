package com.k.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.k.app.entity.Ais;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
// 会自动生成mapper的代理对象，并将该对象交给IOC管理
//
public interface AisMapper extends BaseMapper<Ais> {
    Ais selectByUniqueFields(
        String aisname,
        String callsign,
        String an,
        String color,
        double course,
        String destination,
        String dn,
        String dwt,
        String eta,
        String fn,
        double heading,
        String hide,
        int imonumber,
        double lat,
        double length,
        double lon,
        String minotypecode,
        String minotype,
        String minudiff,
        String mmsi,
        String name,
        int offseta,
        int offsetb,
        int offsetc,
        int offsetd,
        int rs,
        double speed,
        String status,
        String statuscode,
        String trawlsonar,
        String turnrate,
        String type,
        String typecode,
        String updatetimeinfo,
        Date updatetime,
        String updatetimestamp,
        Object width,
        Date acqtime
     );
}
