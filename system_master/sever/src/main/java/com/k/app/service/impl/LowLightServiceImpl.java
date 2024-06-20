package com.k.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k.app.entity.LowLight;
import com.k.app.mapper.LowLightMapper;
import com.k.app.service.LowLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class LowLightServiceImpl extends ServiceImpl<LowLightMapper, LowLight> implements LowLightService {
    @Autowired
    LowLightMapper lowLightMapper;
    @Override
    public ResponseEntity<List<LowLight>> getAllInfo() {
        List<LowLight> list = lowLightMapper.selectList(null);
        for(LowLight lowLight : list) {
            // 数据库只存储文件名 或者说 相对路径
            String imgPath = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\lowLightImgs\\" + lowLight.getImgpath();
            Path filePath = Paths.get(imgPath);
            try {
                byte[] imageBytes = Files.readAllBytes(filePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                String name = lowLight.getImgpath().split("\\.")[0]; //. 不能被识别
//                lowLight.setName(name); // 取名

                String url =  "data:image/" + imageFormat(lowLight.getImgpath()) + ";base64," + base64Image;
                lowLight.setImgpath(url); // 装换成 byte 64
            }
            catch (IOException e) {
                lowLight.setImgpath(""); // 装换成 byte 64
            }

        }
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename()); // name

        String storageDirectory = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\lowLightImgs\\"; // 迫不得已绝对路径
        try {
            // 重复图像不写入
            QueryWrapper<LowLight> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("imgpath", originalFilename);
            LowLight exist = lowLightMapper.selectOne(queryWrapper);

            if (exist != null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("repeat");  // 重复 不动
            }

            // KX10_GIU_20230312_E116.59_N40.01_202300092147_L4A_A_LH_891891839189381_E116.59_N40.01.jpg
            String imgName = originalFilename.substring(0, originalFilename.lastIndexOf(".")); // KX10_GIU_20230312_E116.59_N40.01_202300092147_L4A_A_LH_891891839189381_E116.59_N40.01

            String str = imgName.substring(0,imgName.lastIndexOf("_")); // KX10_GIU_20230312_E116.59_N40.01_202300092147_L4A_A_LH_891891839189381_E116.59
            int index = str.lastIndexOf("_"); // _E116.59_N40.01
            String source_mmsi = str.substring(0, index);
            int index_mmsi = source_mmsi.lastIndexOf("_");

            // source，mmsi，imgPath，pos
            String source = source_mmsi.substring(0, index_mmsi);
            String mmsi = source_mmsi.substring(index_mmsi + 1);
            String imgPath = originalFilename;
            String pos = imgName.substring(index + 1);

            // 如果不存在记录，则创建新的记录
            LowLight lowLight = new LowLight(mmsi,source,imgPath,pos);
            lowLightMapper.insert(lowLight); // 插入

            // 创建存储目录（如果不存在）
            File directory = new File(storageDirectory);
            if (!directory.exists()) directory.mkdirs();
            // 更新 或者 插入 都需要保存图片
            File destinationFile = new File(storageDirectory + originalFilename);
            file.transferTo(destinationFile); // 保存

            return ResponseEntity.ok("ok");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error"); // 错误
        }
    }

    @Override
    public ResponseEntity<List<LowLight>> getInfo(String mmsi) {
        QueryWrapper<LowLight> queryWrapper = Wrappers.query();
        queryWrapper.eq("mmsi", mmsi); // 模糊查找

        List<LowLight> list = lowLightMapper.selectList(queryWrapper); // 获取所有
        for(LowLight lowLight : list) {
            // 数据库只存储文件名 或者说 相对路径
            String imgPath = "D:\\Study\\4.ProjectFiles\\2.web\\system_master\\sever\\data\\lowLightImgs\\" + lowLight.getImgpath();
            Path filePath = Paths.get(imgPath);
            try {
                byte[] imageBytes = Files.readAllBytes(filePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                String name = lowLight.getImgpath().split("\\.")[0]; //. 不能被识别

                String url =  "data:image/" + imageFormat(lowLight.getImgpath()) + ";base64," + base64Image;
                lowLight.setImgpath(url); // 装换成 byte 64
            }
            catch (IOException e) {
                lowLight.setImgpath(""); // 装换成 byte 64
            }

        }
        return ResponseEntity.ok(list);
    }

    private String imageFormat(String imgpath) { // 数据库只存名称, 原因：mysql存 base 编码容量太大，并适合这种大量图像存储的情形
        int dotIndex = imgpath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < imgpath.length() - 1) {
            return imgpath.substring(dotIndex + 1);
        }
        return "";
    }
}
