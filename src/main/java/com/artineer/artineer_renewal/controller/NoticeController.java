package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.NoticeRepository;
import com.artineer.artineer_renewal.service.NoticeService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Arrays;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Controller
public class NoticeController {
    @Value("${file.dir}")
    private String uploadDir;

    @Autowired
    private final NoticeRepository noticeRepository;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping("/notice")
    public String notices(Model model) {
        List<Notice> noticeList = noticeRepository.findAllNotice();
        model.addAttribute("notices", noticeList);
        return "board/notice";
    }

    /* 글 작성 */
    @PostMapping("/notice/new")
    public String createNotice(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        noticeService.createNotice(title, story, file);
        return "redirect:/notice";
    }

    /* 새 글 작성 */
    @GetMapping("/notice/new")
    public String showNewNotice() {
        return "board/noticeNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/notice/{no}")
    public String showNoticeDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 조회수 증가
        noticeService.increaseHitCount(no);

        NoticeDto notice = noticeService.getNoticeByNo(no);

        if (notice == null) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }

        /* Todo 2차 댓글까지만 지원함. */

        List<Comment> comments = commentRepository.findByBbsname("notice");

        model.addAttribute("comments", comments);
        model.addAttribute("bbsname", "notice");
        model.addAttribute("username", username);
        model.addAttribute("notice", notice);

        return "board/noticeDetail";
    }

    /* 글 수정 */
    @GetMapping("/notice/edit/{no}")
    public String showEditNoticeForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        Notice notice = noticeRepository.findById(no).orElse(null);

        if(isAuthorizedUser(notice, loggedInUsername)) {
            model.addAttribute("notice", notice);
            return "board/noticeEdit";
        } else {
            return "redirect:/access-denied";
        }

    }

    @PostMapping("/notice/edit")
    public String updateNotice(@RequestParam Long no, @RequestParam String title, @RequestParam String story) {
        noticeService.updateNotice(no, title, story);
        return "redirect:/notice/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/notice/delete/{no}")
    public String deleteNotice(@PathVariable("no") Long no) {
        Notice notice = noticeRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(notice, loggedInUsername)) {
            noticeService.deleteNotice(no);
            return "redirect:/notice";
        } else {
            return "redirect:/access-denied";
        }

    }

    private boolean isAuthorizedUser(Notice notice, String loggedInUsername) {
        // 작성자나 관리자면 true
        return notice.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }


}
