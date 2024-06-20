package com.k.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k.app.entity.Ship;
import com.k.app.mapper.ShipMapper;
import com.k.app.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class ShipServiceImpl extends ServiceImpl<ShipMapper, Ship> implements ShipService {
    @Autowired
    ShipMapper shipMapper;
    @Override
    public List<Ship> getAllShips() {
//        QueryWrapper queryWrapper = new QueryWrapper();
        List<Ship> shipList = shipMapper.selectList(null); // 不设定条件， 查询所有的记录


        for(Ship ship : shipList) {
            // 数据库只存储文件名 或者说 相对路径
            String imgPath = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\shipImgs\\" + ship.getImgpath();
            Path filePath = Paths.get(imgPath);
            try {
                byte[] imageBytes = Files.readAllBytes(filePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                String url =  "data:image/" + imageFormat(ship.getImgpath()) + ";base64," + base64Image;
                ship.setImgpath(url); // 装换成 byte 64
            }
            catch (IOException e) {
                ship.setImgpath(""); // 装换成 byte 64
            }

        }
        return shipList;
    }

    private String imageFormat(String imgpath) { // 数据库只存名称, 原因：mysql存 base 编码容量太大，并适合这种大量图像存储的情形
        int dotIndex = imgpath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < imgpath.length() - 1) {
            return imgpath.substring(dotIndex + 1);
        }
        return "";
    }

    @Override
    public ResponseEntity<String> updateShipInfo(String mmsi, MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.ok("No file!");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String storageDirectory = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\shipImgs\\"; // 迫不得已绝对路径

        try {
            QueryWrapper<Ship> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mmsi", mmsi);
            Ship existingShip = shipMapper.selectOne(queryWrapper);

            if (existingShip != null) {
                // 如果存在记录，则更新图片，单条更新，多条用 updateWrapper
                existingShip.setImgpath(originalFilename); // 名称
                shipMapper.update(existingShip, queryWrapper);
            } else {
                return ResponseEntity.ok("error");
            }

            // 创建存储目录（如果不存在）
            File directory = new File(storageDirectory);
            if (!directory.exists()) directory.mkdirs();
            // 更新 或者 插入 都需要保存图片
            File destinationFile = new File(storageDirectory + originalFilename);
            file.transferTo(destinationFile); // 保存

//            if(originalFilename != null) {
//                UpdateWrapper<Ship> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("mmsi",mmsi); // 根据 mmsi 进行更新， 更新规则
//
//                Ship ship = new Ship(); // lombok
//                ship.setImgpath(originalFilename);
//
//                shipMapper.update(ship, updateWrapper);
//            }
            return ResponseEntity.ok("ok");
        } catch (IOException e) {
            return ResponseEntity.ok("error");
        }
    }

    @Override
    public ResponseEntity<String> deleteShipInfo(String id) {
        try {
//            QueryWrapper<Ship> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("mmsi", mmsi);
//            int rows = shipMapper.delete(queryWrapper); // 定义 queryWrapper 进行 更新 / 删除
              int rows = shipMapper.deleteById(id);
            if (rows > 0) {
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @Override
    public ResponseEntity<String> addShipInfo(String mmsi, MultipartFile file) {
        // 非空信息，前端已经判断
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String storageDirectory = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\shipImgs\\"; // 迫不得已绝对路径
        try {
            QueryWrapper<Ship> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mmsi", mmsi);
            Ship existingShip = shipMapper.selectOne(queryWrapper);

            if (existingShip != null) {
                return ResponseEntity.ok("repeat");  // 重复
            }

            // 如果不存在记录，则创建新的记录
            Ship newShip = new Ship();
            newShip.setMmsi(mmsi);
            newShip.setImgpath(originalFilename);
            shipMapper.insert(newShip); // 插入

            // 创建存储目录（如果不存在）
            File directory = new File(storageDirectory);
            if (!directory.exists()) directory.mkdirs();
            // 更新 或者 插入 都需要保存图片
            File destinationFile = new File(storageDirectory + originalFilename);
            file.transferTo(destinationFile); // 保存

            return ResponseEntity.ok("ok");
        } catch (IOException e) {
            return ResponseEntity.ok("error");
        }
    }
}