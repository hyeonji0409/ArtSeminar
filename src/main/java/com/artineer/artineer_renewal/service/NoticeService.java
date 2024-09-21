package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.NoticeRepository;
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
public class NoticeService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    // 글 작성
    public ResponseEntity<String> createNotice(String title, String story, List<MultipartFile> files) {
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();

        System.out.println("Client IP: " + clientIp);

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

        // 파일명 저장할 리스트
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


        Notice notice = new Notice();
        // User 자체 설정
        notice.setUser(user);
        notice.setName(user.getName());
        notice.setYear(user.getYear());
        notice.setTitle(title);
        notice.setStory(story);
        notice.setFile(fileNameString); // 파일명 리스트
        notice.setRegdate(formattedDate);
        notice.setIp(clientIp);

        noticeRepository.save(notice);

        return null;
    }

    // 글 상세 조회
    public NoticeDto getNoticeByNo(Long no) {
        Notice notice = noticeRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = notice.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new NoticeDto(
                notice.getNo(),
                notice.getTitle(),
                notice.getStory(),
                notice.getHit(),
                fileNames);
    }

    // 글 수정
    public void updateNotice(Long no, String title, String story) {

        Notice notice = noticeRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setTitle(title);
        notice.setStory(story);

        noticeRepository.save(notice);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteNotice(Long no) {
        // 글 정보 가졍괴
        Notice notice = noticeRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));

        // 파일 삭제
        if(notice.getFile() != null) {
            deleteFiles(notice.getFile());
        }

        noticeRepository.deleteById(no);
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
    public void increaseHitCount(Long no) {
        Notice notice = noticeRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));

        notice.setHit(notice.getHit() + 1);

        noticeRepository.save(notice);
    }
}
