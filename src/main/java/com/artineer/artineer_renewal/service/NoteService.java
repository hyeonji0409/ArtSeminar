package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.NoteDto;
import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.Note;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.NoteRepository;
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

import javax.naming.Name;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NoteService
{
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;

    // 글 작성
    public ResponseEntity<String> createNote(String name, String pw,String title, String story, List<MultipartFile> files) {
        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();

        System.out.println("Client IP: " + clientIp);

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);


        // 파일명 저장할 리스트
        List<String> fileNames = new ArrayList<>();

        // 파일 처리
        for( MultipartFile file : files) {
            String fileName = fileService.uploadMultipartFile(file);
            if (fileName!=null) fileNames.add(fileName);
        }

        String fileNameString = String.join(",", fileNames);

        // System.out.println(fileNames);


        Note note = new Note();
        note.setName(name);
        note.setTitle(title);
        note.setStory(story);
        note.setPw(pw);
        note.setFile(fileNameString); // 파일명 리스트
        note.setRegdate(formattedDate);
        note.setIp(clientIp);

        noteRepository.save(note);

        return null;
    }

    // 글 상세 조회
    public NoteDto getNoteByNo(Long no) {
        Note note = noteRepository.findById(no).orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다."));

        // db 저장된 파일명 ,로 분리하여 리스트화
        String fileNameString = note.getFile();
        List<String> fileNames;
        // 파일 없을 경우도 확인
        if(fileNameString != null && !fileNameString.isEmpty()) {
            fileNames = Arrays.asList(fileNameString.split(","));
        } else {
            fileNames = new ArrayList<>();
        }

        return new NoteDto(
                note.getNo(),
                note.getTitle(),
                note.getStory(),
                note.getName(),
                note.getPw(),
                note.getHit(),
                note.getRegdate(),
                fileNames,
                note.getComments());
    }

    // 글 수정
    public void updateNote(Long no, String title, String story) {

        Note note = noteRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));
        note.setTitle(title);
        note.setStory(story);

        noteRepository.save(note);
    }

    // 글 삭제 -> 삭제 시 data 폴더의 사진도 함꼐 지워짐
    public void deleteNote(Long no) {
        // 글 정보 가져오기
        Note note = noteRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));

        // 파일 삭제
        if(note.getFile() != null) {
            deleteFiles(note.getFile());
            //에디터 사진 삭제 기능 추가
            // fileService.deleteFile()
        }

        noteRepository.deleteById(no);
    }

    // 파일 삭제 로직
    public void deleteFiles(String fileNames) {
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

//            파일 없는 게시판 삭제 오류 때문에 주석처리(파일이 있을 때는 삭제 오류 없음)
            //반환값이 여러개라 생긴 오류
//            해당 코드를 통해서 파일 삭제 시 데이터베이스에 있던 파일 값을 없애는 코드임
//            Notice notice = noticeRepository.findByFile(fileName);
//            if (notice != null) {
//                notice.setFile("");  // 파일 경로 삭제
//                noticeRepository.save(notice);  // 변경사항 저장
//                System.out.println("데이터베이스에서 file 필드 삭제 완료: " + fileName);
//            } else {
//                System.out.println("데이터베이스에서 해당 file을 찾을 수 없습니다: " + fileName);
//            }
        }

    }



    // 조회수 증가
    public void increaseHitCount(Long no) {
        Note note = noteRepository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found"));

        note.setHit(note.getHit() + 1);

        noteRepository.save(note);
    }
}
