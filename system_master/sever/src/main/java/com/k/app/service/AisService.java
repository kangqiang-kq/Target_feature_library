package com.k.app.service;

import com.k.app.entity.Ais;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AisService {
    ResponseEntity<List<Ais>> getAllAis();

    ResponseEntity<List<Ais>> getAis(String mmsi);

    ResponseEntity<String> uploadAis(List<Ais> aisList);
}
