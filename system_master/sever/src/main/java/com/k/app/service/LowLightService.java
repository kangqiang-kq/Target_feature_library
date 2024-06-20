package com.k.app.service;

import com.k.app.entity.LowLight;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LowLightService {
    ResponseEntity<List<LowLight>> getAllInfo();

    ResponseEntity<String> uploadImages(MultipartFile file);


    ResponseEntity<List<LowLight>> getInfo(String mmsi);
}
