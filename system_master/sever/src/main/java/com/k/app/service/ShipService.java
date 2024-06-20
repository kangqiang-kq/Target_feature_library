package com.k.app.service;

import com.k.app.entity.Ship;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ShipService {
    List<Ship> getAllShips() throws IOException;
    ResponseEntity<String> updateShipInfo(String mmsi, MultipartFile file); // 更新

    ResponseEntity<String> deleteShipInfo(String id);

    ResponseEntity<String> addShipInfo(String mmsi, MultipartFile file);  // add info
}
