package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.ReferenceDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.ReferenceRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.ReferenceService;
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
public class ReferenceController {
    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;


    @GetMapping("/reference")
    public String references(Model model,
                          @RequestParam(name = "qt", required = false, defaultValue = "subject") String queryType,
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

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Reference.class, queryType, query, pageable);
        //        Page<Object> pagination = referenceRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/reference/reference";
    }

    /* 글 작성 */
    @PostMapping("/reference/new")
    public String createReference(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        referenceService.createReference(title, story, file);
        return "redirect:/reference";
    }

    /* 새 글 작성 */
    @GetMapping("/reference/new")
    public String showNewReference(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/reference/referenceNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/reference/{no}")
    public String showReferenceDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        ReferenceDto reference = referenceService.getReferenceByNo(no);
        if (reference == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
//        (new List<Integer>).
        //  System.out.println("WLWL" + reference.getComments().size());

        // 조회수 증가
        referenceService.increaseHitCount(no);

        model.addAttribute("bbsName", "reference");
        model.addAttribute("bbsNo", no);
        model.addAttribute("reference", reference);

        return "board/reference/referenceDetail";
    }

    /* 글 수정 */
    @GetMapping("/reference/edit/{no}")
    public String showEditReferenceForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Reference reference = referenceRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(reference, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("reference", reference);
        return "board/reference/referenceEdit";
    }

    @PostMapping("/reference/edit")
    public String updateReference(@RequestParam Long no, @RequestParam String title, @RequestParam String story,@RequestParam("file") List<MultipartFile> file) {
        referenceService.updateReference(no, title, story,file);
        return "redirect:/reference/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/reference/delete/{no}")
    public String deleteReference(@PathVariable("no") Long no) {
        Reference reference = referenceRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(reference, loggedInUsername)) {
            referenceService.deleteReference(no);
            return "redirect:/reference";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/r/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request) {

        referenceService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        // referer가 null이 아니면 해당 URL로 리다이렉트
        // referer가 null인 경우 기본 페이지로 리다이렉트 (예: reference 목록)
        if (referer != null) return "redirect:" + referer;
        return "redirect:/reference";
    }

    private boolean isAuthorizedUser(Reference reference, String loggedInUsername) {
        // 작성자나 관리자면 true
        return reference.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
