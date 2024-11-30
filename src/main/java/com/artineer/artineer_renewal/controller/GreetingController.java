package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.GreetingDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.GreetingRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.GreetingService;
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
public class GreetingController {
    @Autowired
    private GreetingRepository greetingRepository;
    @Autowired
    private GreetingService greetingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;


    @GetMapping("/greeting")
    public String greetings(Model model,
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

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Greeting.class, queryType, query, pageable);
        //        Page<Object> pagination = greetingRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/greeting/greeting";
    }

    /* 글 작성 */
    @PostMapping("/greeting/new")
    public String createGreeting(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        greetingService.createGreeting(title, story, file);
        return "redirect:/greeting";
    }

    /* 새 글 작성 */
    @GetMapping("/greeting/new")
    public String showNewGreeting(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/greeting/greetingNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/greeting/{no}")
    public String showGreetingDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        GreetingDto greeting = greetingService.getGreetingByNo(no);
        if (greeting == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
//        (new List<Integer>).
        //  System.out.println("WLWL" + greeting.getComments().size());

        // 조회수 증가
        greetingService.increaseHitCount(no);

        model.addAttribute("bbsName", "greeting");
        model.addAttribute("bbsNo", no);
        model.addAttribute("greeting", greeting);

        return "board/greeting/greetingDetail";
    }

    /* 글 수정 */
    @GetMapping("/greeting/edit/{no}")
    public String showEditGreetingForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Greeting greeting = greetingRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(greeting, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("greeting", greeting);
        return "board/greeting/greetingEdit";
    }

    @PostMapping("/greeting/edit")
    public String updateGreeting(@RequestParam Long no, @RequestParam String title, @RequestParam String story) {
        greetingService.updateGreeting(no, title, story);
        return "redirect:/greeting/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/greeting/delete/{no}")
    public String deleteGreeting(@PathVariable("no") Long no) {
        Greeting greeting = greetingRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(greeting, loggedInUsername)) {
            greetingService.deleteGreeting(no);
            return "redirect:/greeting";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/gr/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request) {

        greetingService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        // referer가 null이 아니면 해당 URL로 리다이렉트
        // referer가 null인 경우 기본 페이지로 리다이렉트 (예: greeting 목록)
        if (referer != null) return "redirect:" + referer;
        return "redirect:/greeting";
    }

    private boolean isAuthorizedUser(Greeting greeting, String loggedInUsername) {
        // 작성자나 관리자면 true
        return greeting.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
