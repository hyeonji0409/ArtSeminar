package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.GalleryDto;
import com.artineer.artineer_renewal.entity.Exam;
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
    @Autowired
    private FileService fileService;

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
        // 파일 처리 부분을 story의 값에서 src부분만 추출 해서 저장 가능한지 실험 중
        for( MultipartFile file : files) {
            String fileName = fileService.uploadMultipartFile(file);
            if (fileName!=null) fileNames.add(fileName);
        }

        // 파일이 하나도 없다면 기본 파일 "no_image.jpg" 추가
        if (fileNames.isEmpty() || fileNames.stream().noneMatch(fileName -> fileName.matches(".*\\.(jpg|jpeg|png)$"))) {
            fileNames.add("no_image.jpg");
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
                gallery.getUser().getUsername(),
                gallery.getName(),
                gallery.getYear(),
                gallery.getRegdate(),
                fileName,
                gallery.getComments());

    }

    // 글 수정
    public void updateGallery(Long no, String title, String story, List<MultipartFile> files){
        Gallery gallery = galleryRepository.findById(no).orElseThrow(()->new RuntimeException("Notice not found"));
        gallery.setTitle(title);
        gallery.setStory(story);

        List<String> fileNames = new ArrayList<>();

        String existingFiles = gallery.getFile();
        if (existingFiles != null && !existingFiles.isEmpty()) {
            String[] existingFileArray = existingFiles.split(",");
            fileNames.addAll(Arrays.asList(existingFileArray));
        }

        for (MultipartFile file : files) {
            String fileName = fileService.uploadMultipartFile(file);
            if (fileName != null) {
                fileNames.add(fileName);
            }
        }

        String fileNameString = String.join(",", fileNames);

        gallery.setFile(fileNameString);

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
    public void deleteFiles(String fileNames) {
        String fileorigin = fileNames;
        String[] fileNameArray = fileNames.split(",");

        for(String fileName : fileNameArray) {
            Path filePath = Paths.get(fileDir + fileName);
            File file = filePath.toFile();

            if (!fileName.equals("no_image.jpg")) {
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("파일 삭제 완료: " + fileName);
                    } else {
                        System.out.println("파일 삭제 실패: " + fileName);
                    }
                } else {
                    System.out.println("파일을 찾을 수 없습니다: " + fileName);
                }
            } else {
                System.out.println("no_image.jpg는 삭제할 수 없습니다.");
            }

            List<Gallery> galleries = galleryRepository.findAllByFile(fileorigin);

            if (galleries.isEmpty()) {
                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
            } else {
                for (Gallery gallery : galleries) {
                    gallery.setFile("");  // 파일 경로 초기화
                    galleryRepository.save(gallery);  // 변경사항 저장
                    System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
                }
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
