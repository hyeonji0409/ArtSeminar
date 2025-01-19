package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.ReferenceDto;
import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.entity.Reference;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.ReferenceRepository;
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
public class ReferenceService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest request;



    // 글 작성
    public ResponseEntity<String> createReference(String title, String story, List<MultipartFile> files) {
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


        Reference reference = new Reference();
        // User 자체 설정
        reference.setUser(user);
        reference.setName(user.getName());
        reference.setYear(user.getYear());
        reference.setTitle(title);
        reference.setStory(story);
        reference.setFile(fileNameString); // 파일명 리스트
        reference.setRegdate(formattedDate);
        reference.setIp(clientIp);

        referenceRepository.save(reference);

        return null;
    }

    // 글 상세 조회
    public ReferenceDto getReferenceByNo(Long no) {
        Reference reference = referenceRepository.findById(no).orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다."));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = reference.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new ReferenceDto(
                reference.getNo(),
                reference.getTitle(),
                reference.getStory(),
                reference.getHit(),
                reference.getUser().getUsername(),
                reference.getName(),
                reference.getYear(),
                reference.getRegdate(),
                fileNames,
                reference.getComments());
    }

    // 글 수정
    public void updateReference(Long no, String title, String story, List<MultipartFile> files) {

        Reference reference = referenceRepository.findById(no).orElseThrow(() -> new RuntimeException("Reference not found"));
        reference.setTitle(title);
        reference.setStory(story);

        List<String> fileNames = new ArrayList<>();

        String existingFiles = reference.getFile();
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

        reference.setFile(fileNameString);

        referenceRepository.save(reference);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteReference(Long no) {
        // 글 정보 가져오기
        Reference reference = referenceRepository.findById(no).orElseThrow(() -> new RuntimeException("Reference not found"));

        // 파일 삭제
        if(reference.getFile() != null) {
            deleteFiles(reference.getFile());
            //에디터 사진 삭제 기능 추가
            // fileService.deleteFile()
        }

        referenceRepository.deleteById(no);
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

            List<Reference> references = referenceRepository.findAllByFile(fileorigin);

            if (references.isEmpty()) {
                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
            } else {
                for (Reference reference : references) {
                    reference.setFile("");  // 파일 경로 초기화
                    referenceRepository.save(reference);  // 변경사항 저장
                    System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
                }
            }
        }

    }



    // 조회수 증가
    public void increaseHitCount(Long no) {
        Reference reference = referenceRepository.findById(no).orElseThrow(() -> new RuntimeException("Reference not found"));

        reference.setHit(reference.getHit() + 1);

        referenceRepository.save(reference);
    }
}
