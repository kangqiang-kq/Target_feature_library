package com.k.app.controller;


import com.k.app.entity.Ais;
import com.k.app.service.AisService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController // 控制器声明
@RequestMapping("/ais")

public class AisController {
    @Autowired // 注入
    AisService aisService;

    @GetMapping("/getAllAis")
    public ResponseEntity<List<Ais>> getAllAis(){
        // 获取所有的ais信息
        return aisService.getAllAis();
    }

    @PostMapping("/getAis")
    public ResponseEntity<List<Ais>> getAis(@RequestParam("mmsi") String mmsi){
        // 根据 mmsi，@RequestParam
        // 前端 formData
        return aisService.getAis(mmsi);
    }

    @PostMapping("/uploadAis")
    public ResponseEntity<String> uploadAis(@RequestBody List<Ais> aisList){
        // @RequestBody 或者
        return aisService.uploadAis(aisList);
    }
}
