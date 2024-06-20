package com.k.app.controller;

import com.k.app.entity.Ship;
import com.k.app.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/*
功能列举：
渔船数据上传！
微光数据上传！
热红外数据上传！

微光 / 热红外数据是和渔船数据进行绑定的
*/

@RestController
@RequestMapping("/img") // 父路劲
public class ShipController {

    @Autowired
    ShipService shipService;

    @GetMapping("/getAllShips")  // 获取 ship 列表
    public List<Ship> getAllShips() throws IOException {
        return shipService.getAllShips(); // 获取所有的渔船对象，接口已经测试 ok！
    }


    @PostMapping("/updateShipInfo")
    public ResponseEntity<String> updateShipInfo(@RequestParam("mmsi") String mmsi, @RequestParam("file") MultipartFile file){
//        System.out.println(file);
        // formData.append(key,value);
        return shipService.updateShipInfo(mmsi, file); // 具体实现看 impl
    }

    @PostMapping("/deleteShipInfo")
    public ResponseEntity<String> deleteShipInfo(@RequestParam("id") String id) {
//        System.out.println(id);
        return shipService.deleteShipInfo(id);
    }
    @PostMapping("/addShipInfo")
    public ResponseEntity<String> addShipInfo(@RequestParam("mmsi") String mmsi, @RequestParam("file") MultipartFile file) {
        return shipService.addShipInfo(mmsi, file); // add info
    }
}
