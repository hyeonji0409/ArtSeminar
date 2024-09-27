package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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


    // 파일 다운로드
    @GetMapping("/data/{file}")
    public ResponseEntity<Resource> viewFile(@PathVariable String file) {
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



    @PostMapping("/data/upload")
    @ResponseBody
//    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    public Map<String, Object> uploadData(MultipartHttpServletRequest files) throws IOException {
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // IP 주소 가져오기
//        String clientIp = request.getRemoteAddr();

//        System.out.println("Client IP: " + clientIp);

        // 현재 시간 가져오기
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
//        String formattedDate = now.format(formatter);
//
//        // User 엔티티 조회
//        User user = userRepository.findByUsername(loggedInUsername);
//        if(user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
        // .orElseThrow(() -> new UsernameNotFoundException("User not found username: " + loggedInUsername));

//         파일명 저장할 리스트
        List<MultipartFile> fileList = files.getFiles("upload");
        List<String> fileNames = new ArrayList<>();

        // 파일 처리
        for( MultipartFile file : fileList) {
            if(!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                System.out.println(originalFilename);
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                try {
                    Path path = Paths.get(fileDir + fileName);
                    System.out.println(path.toAbsolutePath());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body("파일 업로드 실패");
                    Map<String, Object> response = new HashMap<>();
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", "The image upload failed because the image was too big (max 200MB).");
                    response.put("error", "ee");

                    return response;
                }

            }
        }

        Map<String, Object> url = new HashMap<>();
        url.put("url", "http://localhost:8080/data/"+ fileNames.get(0));
        return url;
    }

}
