package com.k.app.service;

import com.k.app.entity.LowLight;
import com.k.app.entity.ThermalInfrared;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThermalInfraredService {
    ResponseEntity<List<ThermalInfrared>> getAllInfo();

    ResponseEntity<String> uploadImages(MultipartFile file);

    ResponseEntity<List<ThermalInfrared>> getInfo(String mmsi);
}
