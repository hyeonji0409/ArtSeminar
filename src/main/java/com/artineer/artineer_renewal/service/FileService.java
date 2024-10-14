package com.artineer.artineer_renewal.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {
    @Value("${file.dir}")
    private String fileDir;

//    FileService() {

//    }

    // @Value는 인스턴스 생성 단계에서 주입되지 않음.
//    @PostConstruct
//    public void init() {
//        File directory = new File(fileDir);
//        if (!directory.exists()) {
//            directory.mkdirs(); // 디렉토리 생성
//        }
//    }


    public String uploadMultipartFile(MultipartFile file) {
        if(file==null || file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

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


    // todo 게시글, 팝업 등 파일 업로드 기능이 구현된 곳에서 이 로직을 추가할 필요성
    /**
     * 파일 경로를 받아와 삭제하는 메서드,
     * 전역 프로퍼티의 경로가 기본 경로이다.
     * @param fileName 기본 설정 경로 하위에 있는 파일명
     *                 확장자까지 포함한다.
     * @return 삭제 성공 여부
     */
    public boolean deleteFile(String fileName) {
        boolean flag = false;

        try {
            Path path = Paths.get(fileDir + fileName);
            Files.delete(path);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }

        return flag;
    }
}
