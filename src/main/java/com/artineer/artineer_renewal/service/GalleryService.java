package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.GalleryDto;
import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GalleryService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    //글작성
    public ResponseEntity<String> createGallery(String title, String story, List<MultipartFile> files){
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();

        System.out.println("Client IP: "+clientIp);

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);

        // User 엔티티 조회
        User user = userRepository.findByUsername(loggedInUsername);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // .orElseThrow(() -> new UsernameNotFoundException("User not found username: " + loggedInUsername));

        List<String> fileNames = new ArrayList<>();

        // 파일 처리
        for( MultipartFile file : files) {
            if(!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                try {
                    Path path = Paths.get(fileDir + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("파일 업로드 실패");
                }

            }
        }

        String fileNameString = String.join(",", fileNames);

        Gallery gallery = new Gallery();

        gallery.setUser(user);
        gallery.setName(user.getName());
        gallery.setYear(user.getYear());
        gallery.setTitle(title);
        gallery.setStory(story);
        gallery.setFile(fileNameString); // 파일명 리스트
        gallery.setRegdate(formattedDate);
        gallery.setIp(clientIp);

        galleryRepository.save(gallery);

        return null;
    }


    // 글 상세 조회
    public GalleryDto getGalleryByNo(Long no){
        Gallery gallery = galleryRepository.findById(no).orElseThrow(()->new RuntimeException("Gallery not found"));
        String fileNameString = gallery.getFile();
        List<String> fileName;

        if(fileNameString != null && !fileNameString.isEmpty()){
            fileName = Arrays.asList(fileNameString.split(","));
        }
        else{
            fileName = new ArrayList<>();
        }
        return new GalleryDto(
                gallery.getNo(),
                gallery.getTitle(),
                gallery.getStory(),
                gallery.getHit(),
                fileName);
    }

    // 글 수정
    public void updateGallery(Long no, String title, String story){
        Gallery gallery = galleryRepository.findById(no).orElseThrow(()->new RuntimeException("Notice not found"));
        gallery.setTitle(title);
        gallery.setStory(story);

        galleryRepository.save(gallery);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteGallery(Long no) {
        Gallery gallery =galleryRepository.findById(no).orElseThrow(()->new RuntimeException("Notice not found"));

        if(gallery.getFile() != null) {
            deleteFiles(gallery.getFile());
        }
        galleryRepository.deleteById(no);
    }

    // 파일 삭제 로직
    private void deleteFiles(String fileNames) {
        String[] fileNameArray = fileNames.split(",");

        for(String fileName : fileNameArray) {
            Path filePath = Paths.get(fileDir + fileName);
            File file = filePath.toFile();

            if(file.exists()) {
                if(file.delete()) {
                    System.out.println("파일 삭제 완료" + fileName);
                } else {
                    System.out.println("파일 삭제 실패" + fileName);
                }
            } else {
                System.out.println("파일을 찾을 수 없습니다." + fileName);
            }


        }
    }

    // 조회수 증가
    public void increaseHitCount(Long no){
        Gallery gallery = galleryRepository.findById(no).orElseThrow(()->new RuntimeException("Notice not found"));
        gallery.setHit(gallery.getHit()+1);
        galleryRepository.save(gallery);
    }

}
