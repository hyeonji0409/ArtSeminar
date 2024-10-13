package com.artineer.artineer_renewal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    @Value("${file.dir}")
    private String fileDir;

    public String uploadMultipartFile(MultipartFile file) {
        if(file==null || file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
//        File directory = new File(fileDir);
//        if (!directory.exists()) {
//            directory.mkdirs(); // 디렉터리 생성
//        }

        try {
            Path path = Paths.get(fileDir + fileName);
            System.out.println("업로드된 파일의 절대경로는");
            System.out.println(path.toAbsolutePath());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return fileName;
    }
}
