package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.MinutesDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.MinutesRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.MinutesService;
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
public class MinutesController {
    @Autowired
    private MinutesRepository minutesRepository;
    @Autowired
    private MinutesService minutesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;


    @GetMapping("/minutes")
    public String minutes(Model model,
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

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Minutes.class, queryType, query, pageable);
        //        Page<Object> pagination = minutesRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/minutes/minutes";
    }

    /* 글 작성 */
    @PostMapping("/minutes/new")
    public String createMinutes(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        minutesService.createMinutes(title, story, file);
        return "redirect:/minutes";
    }

    /* 새 글 작성 */
    @GetMapping("/minutes/new")
    public String showNewMinutes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/minutes/minutesNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/minutes/{no}")
    public String showMinutesDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        MinutesDto minutes = minutesService.getMinutesByNo(no);
        if (minutes == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
//        (new List<Integer>).
        //  System.out.println("WLWL" + minutes.getComments().size());

        // 조회수 증가
        minutesService.increaseHitCount(no);

        model.addAttribute("bbsName", "minutes");
        model.addAttribute("bbsNo", no);
        model.addAttribute("minutes", minutes);

        return "board/minutes/minutesDetail";
    }

    /* 글 수정 */
    @GetMapping("/minutes/edit/{no}")
    public String showEditMinutesForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Minutes minutes = minutesRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(minutes, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("minutes", minutes);
        return "board/minutes/minutesEdit";
    }

    @PostMapping("/minutes/edit")
    public String updateMinutes(@RequestParam Long no, @RequestParam String title, @RequestParam String story,@RequestParam("file") List<MultipartFile> file) {
        minutesService.updateMinutes(no, title, story,file);
        return "redirect:/minutes/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/minutes/delete/{no}")
    public String deleteMinutes(@PathVariable("no") Long no) {
        Minutes minutes = minutesRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(minutes, loggedInUsername)) {
            minutesService.deleteMinutes(no);
            return "redirect:/minutes";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/m/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request) {

        minutesService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        // referer가 null이 아니면 해당 URL로 리다이렉트
        // referer가 null인 경우 기본 페이지로 리다이렉트 (예: minutes 목록)
        if (referer != null) return "redirect:" + referer;
        return "redirect:/minutes";
    }

    private boolean isAuthorizedUser(Minutes minutes, String loggedInUsername) {
        // 작성자나 관리자면 true
        return minutes.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
