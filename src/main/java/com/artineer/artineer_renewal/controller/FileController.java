package com.artineer.artineer_renewal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Component
public class FileController {
    @Value("${file.dir}")
    private String fileDir;

    // 파일 다운로드
    @GetMapping("/downloads/{file}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String file) {
        try {
            Path filePath = Paths.get(fileDir).resolve(file).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            // uuid 제거한 원래 파일명으로 저장하기
            String encodedFileName = URLEncoder.encode(file, StandardCharsets.UTF_8.toString());
            int underscoreIndex = encodedFileName.indexOf("_");
            String fileName = encodedFileName.substring(underscoreIndex + 1);

            // 다운로드 창 띄워서 저장할 수 있게 하기
            String contentDisposition = "attachment; filename=\"" + fileName + "\"";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
