package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.MinutesDto;
import com.artineer.artineer_renewal.entity.Minutes;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.MinutesRepository;
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
public class MinutesService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private MinutesRepository minutesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;

    // 글 작성
    public ResponseEntity<String> createMinutes(String title, String story, List<MultipartFile> files) {
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

        String fileNameString = String.join(",", fileNames);

        // System.out.println(fileNames);


        Minutes minutes = new Minutes();
        // User 자체 설정
        minutes.setUser(user);
        minutes.setName(user.getName());
        minutes.setYear(user.getYear());
        minutes.setTitle(title);
        minutes.setStory(story);
        minutes.setFile(fileNameString); // 파일명 리스트
        minutes.setRegdate(formattedDate);
        minutes.setIp(clientIp);

        minutesRepository.save(minutes);

        return null;
    }

    // 글 상세 조회
    public MinutesDto getMinutesByNo(Long no) {
        Minutes minutes = minutesRepository.findById(no).orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다."));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = minutes.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new MinutesDto(
                minutes.getNo(),
                minutes.getTitle(),
                minutes.getStory(),
                minutes.getHit(),
                minutes.getUser().getUsername(),
                minutes.getName(),
                minutes.getYear(),
                minutes.getRegdate(),
                fileNames,
                minutes.getComments());
    }

    // 글 수정
    public void updateMinutes(Long no, String title, String story,List<MultipartFile> files) {

        Minutes minutes = minutesRepository.findById(no).orElseThrow(() -> new RuntimeException("Minutes not found"));
        minutes.setTitle(title);
        minutes.setStory(story);

        List<String> fileNames = new ArrayList<>();

        String existingFiles = minutes.getFile();
        if (existingFiles != null && !existingFiles.isEmpty()) {
            // 기존 파일 목록을 , 기준으로 분리하여 리스트로 변환
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

        minutes.setFile(fileNameString);

        minutesRepository.save(minutes);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteMinutes(Long no) {
        // 글 정보 가져오기
        Minutes minutes = minutesRepository.findById(no).orElseThrow(() -> new RuntimeException("Minutes not found"));

        // 파일 삭제
        if(minutes.getFile() != null) {
            deleteFiles(minutes.getFile());
            //에디터 사진 삭제 기능 추가
            // fileService.deleteFile()
        }
        minutesRepository.deleteById(no);
    }

    // 파일 삭제 로직
    public void deleteFiles(String fileNames) {
        String fileorigin = fileNames;
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

            List<Minutes> minutes = minutesRepository.findAllByFile(fileorigin);

            if (minutes.isEmpty()) {
                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
            } else {
                for (Minutes minute : minutes) {
                    minute.setFile("");  // 파일 경로 초기화
                    minutesRepository.save(minute);  // 변경사항 저장
                    System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
                }
            }
        }

    }
    // 조회수 증가
    public void increaseHitCount(Long no) {
        Minutes minutes = minutesRepository.findById(no).orElseThrow(() -> new RuntimeException("Minutes not found"));

        minutes.setHit(minutes.getHit() + 1);

        minutesRepository.save(minutes);
    }
}
