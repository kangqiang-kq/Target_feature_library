package com.k.app.controller;

import com.k.app.entity.LowLight;
import com.k.app.entity.ThermalInfrared;
import com.k.app.service.LowLightService;
import com.k.app.service.ThermalInfraredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/thermalInfrared")
public class ThermalInfraredController {
    @Autowired
    ThermalInfraredService service;

    @GetMapping("/getAllInfo") // 获取全部lowlight信息
    public ResponseEntity<List<ThermalInfrared>> getAllInfo() {
        return service.getAllInfo();
    }

    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImages(@RequestParam MultipartFile file) {
        System.out.println(file);
        return service.uploadImages(file); // 单个文件
    }

    @PostMapping("/getInfo")
    public ResponseEntity<List<ThermalInfrared>> getInfo(@RequestParam("mmsi") String mmsi) {
        return service.getInfo(mmsi); //
    }
}