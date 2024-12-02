package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.NoticeRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.NoticeService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class NoticeController {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;


    @GetMapping("/notice")
    public String notices(Model model,
                          @RequestParam(name = "qt", required = false, defaultValue = "subject") String queryType,
                          @RequestParam(name = "q", required = false, defaultValue = "") String query,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println("받아온 값" + queryType +"~" + query);

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Notice.class, queryType, query, pageable);
        //        Page<Object> pagination = noticeRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/notice/notice";
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
        return "board/notice/noticeNew";
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
      //  System.out.println("WLWL" + notice.getComments().size());

        // 조회수 증가
        noticeService.increaseHitCount(no);

        model.addAttribute("bbsName", "notice");
        model.addAttribute("bbsNo", no);
        model.addAttribute("notice", notice);

        return "board/notice/noticeDetail";
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
        return "board/notice/noticeEdit";
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

    // 게시글 수정 시 파일을 따로 삭제 하고자 할 때 사용(현재 오류로 인해 서비스에서 주석 처리해놨음)
    @GetMapping("/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request) {

        noticeService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        // referer가 null이 아니면 해당 URL로 리다이렉트
        // referer가 null인 경우 기본 페이지로 리다이렉트 (예: notice 목록)
        if (referer != null) return "redirect:" + referer;
        return "redirect:/notice";
    }

    private boolean isAuthorizedUser(Notice notice, String loggedInUsername) {
        // 작성자나 관리자면 true
        return notice.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
