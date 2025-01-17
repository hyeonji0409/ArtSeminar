package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.ExamDto;
import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.ExamRepository;
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
public class ExamService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;

    // 글 작성
    public ResponseEntity<String> createExam(String title, String story, List<MultipartFile> files) {
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


        Exam exam = new Exam();
        // User 자체 설정
        exam.setUser(user);
        exam.setName(user.getName());
        exam.setYear(user.getYear());
        exam.setTitle(title);
        exam.setStory(story);
        exam.setFile(fileNameString); // 파일명 리스트
        exam.setRegdate(formattedDate);
        exam.setIp(clientIp);

        examRepository.save(exam);

        return null;
    }

    // 글 상세 조회
    public ExamDto getExamByNo(Long no) {
        Exam exam = examRepository.findById(no).orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다."));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = exam.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new ExamDto(
                exam.getNo(),
                exam.getTitle(),
                exam.getStory(),
                exam.getHit(),
                exam.getUser().getUsername(),
                exam.getName(),
                exam.getYear(),
                exam.getRegdate(),
                fileNames,
                exam.getComments());
    }

    // 글 수정
    public void updateExam(Long no, String title, String story, List<MultipartFile> files) {

        Exam exam = examRepository.findById(no).orElseThrow(() -> new RuntimeException("Exam not found"));
        exam.setTitle(title);
        exam.setStory(story);

        List<String> fileNames = new ArrayList<>();

        String existingFiles = exam.getFile();
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

        exam.setFile(fileNameString);

        examRepository.save(exam);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteExam(Long no) {
        // 글 정보 가져오기
        Exam exam = examRepository.findById(no).orElseThrow(() -> new RuntimeException("Exam not found"));

        // 파일 삭제
        if(exam.getFile() != null) {
            deleteFiles(exam.getFile());
            //에디터 사진 삭제 기능 추가
            // fileService.deleteFile()
        }
        examRepository.deleteById(no);
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

            List<Exam> exams = examRepository.findAllByFile(fileorigin);

            if (exams.isEmpty()) {
                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
            } else {
                for (Exam exam : exams) {
                    exam.setFile("");  // 파일 경로 초기화
                    examRepository.save(exam);  // 변경사항 저장
                    System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
                }
            }
        }

    }
    // 조회수 증가
    public void increaseHitCount(Long no) {
        Exam exam = examRepository.findById(no).orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setHit(exam.getHit() + 1);

        examRepository.save(exam);
    }
}
