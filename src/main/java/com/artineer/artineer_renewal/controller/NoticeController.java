package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.IntegratedArticleRepository;
import com.artineer.artineer_renewal.repository.NoticeRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.NoticeService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class NoticeController {
    @Value("${file.dir}")
    private String uploadDir;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;
    @Autowired
    private IntegratedArticleRepository integratedArticleRepository;


    @GetMapping("/notice")
    public String notices(Model model,
                          @RequestParam(name = "q", required = false, defaultValue = "") String query,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));


        List<Class<?>> obj = new ArrayList<>();
//        obj.add(Notice.class);
//        obj.add(Gallery.class);
        obj.add(Project.class);
//        obj.add(Greeting.class);
//        obj.add(Minutes.class);

        Page<IntegratedArticle> pagination = integratedArticleService.getNoticesByQuery(obj, query, pageable);
        //        Page<Object> pagination = noticeRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("query", query);
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
    public String showNewNotice(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/noticeNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/notice/{no}")
    public String showNoticeDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        NoticeDto notice = noticeService.getNoticeByNo(no);
        if (notice == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
//        (new List<Integer>).
        System.out.println("WLWL" + notice.getComments().size());

        // 조회수 증가
        noticeService.increaseHitCount(no);

        model.addAttribute("bbsName", "notice");
        model.addAttribute("bbsNo", no);
        model.addAttribute("notice", notice);

        return "board/noticeDetail";
    }

    /* 글 수정 */
    @GetMapping("/notice/edit/{no}")
    public String showEditNoticeForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Notice notice = noticeRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(notice, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("notice", notice);
        return "board/noticeEdit";
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
