package com.k.app.controller;

import com.k.app.entity.LowLight;
import com.k.app.service.LowLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lowlight")
public class LowLightController {
    @Autowired
    LowLightService lowLightService;

    @GetMapping("/getAllInfo") // 获取全部lowlight信息
    public ResponseEntity<List<LowLight>> getAllInfo() {
        return lowLightService.getAllInfo();
    }

    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImages(@RequestParam MultipartFile file) {
        System.out.println(file);
        return lowLightService.uploadImages(file); // 单个文件
    }

    @PostMapping("/getInfo")
    public ResponseEntity<List<LowLight>> getInfo(@RequestParam("mmsi") String mmsi) {
        return lowLightService.getInfo(mmsi); //
    }
}
