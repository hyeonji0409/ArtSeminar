package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.GreetingDto;
import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.Greeting;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.GreetingRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GreetingService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private GreetingRepository greetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest request;



    // 글 작성
    public ResponseEntity<String> createGreeting(String title, String story, List<MultipartFile> files) {
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();

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
            String fileName = fileService.uploadMultipartFile(file);
            if (fileName!=null) fileNames.add(fileName);
        }

        // 파일이 하나도 없다면 기본 파일 "no_image.jpg" 추가
        if (fileNames.isEmpty() || fileNames.stream().noneMatch(fileName -> fileName.matches(".*\\.(jpg|jpeg|png)$"))) {
            fileNames.add("no_image.jpg");
        }

        String fileNameString = String.join(",", fileNames);

        // System.out.println(fileNames);


        Greeting greeting = new Greeting();
        // User 자체 설정
        greeting.setUser(user);
        greeting.setName(user.getName());
        greeting.setYear(user.getYear());
        greeting.setTitle(title);
        greeting.setStory(story);
        greeting.setFile(fileNameString); // 파일명 리스트
        greeting.setRegdate(formattedDate);
        greeting.setIp(clientIp);

        greetingRepository.save(greeting);

        return null;
    }

    // 글 상세 조회
    public GreetingDto getGreetingByNo(Long no) {
        Greeting greeting = greetingRepository.findById(no).orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다."));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = greeting.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new GreetingDto(
                greeting.getNo(),
                greeting.getTitle(),
                greeting.getStory(),
                greeting.getHit(),
                greeting.getUser().getUsername(),
                greeting.getName(),
                greeting.getYear(),
                greeting.getRegdate(),
                fileNames,
                greeting.getComments());
    }

    // 글 수정
    public void updateGreeting(Long no, String title, String story, List<MultipartFile> files) {

        Greeting greeting = greetingRepository.findById(no).orElseThrow(() -> new RuntimeException("Greeting not found"));
        greeting.setTitle(title);
        greeting.setStory(story);

        List<String> fileNames = new ArrayList<>();

        String existingFiles = greeting.getFile();
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

        greeting.setFile(fileNameString);

        greetingRepository.save(greeting);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteGreeting(Long no) {
        // 글 정보 가져오기
        Greeting greeting = greetingRepository.findById(no).orElseThrow(() -> new RuntimeException("Greeting not found"));

        // 파일 삭제
        if(greeting.getFile() != null) {
            deleteFiles(greeting.getFile());
            //에디터 사진 삭제 기능 추가
            // fileService.deleteFile()
        }

        greetingRepository.deleteById(no);
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

            List<Greeting> galleries = greetingRepository.findAllByFile(fileorigin);

            if (galleries.isEmpty()) {
                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
            } else {
                for (Greeting greeting : galleries) {
                    greeting.setFile("");  // 파일 경로 초기화
                    greetingRepository.save(greeting);  // 변경사항 저장
                    System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
                }
            }
        }

    }



    // 조회수 증가
    public void increaseHitCount(Long no) {
        Greeting greeting = greetingRepository.findById(no).orElseThrow(() -> new RuntimeException("Greeting not found"));

        greeting.setHit(greeting.getHit() + 1);

        greetingRepository.save(greeting);
    }
}
