package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.ExamDto;
import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.entity.IntegratedArticle;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.ExamRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.ExamService;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ExamController {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;

    @GetMapping("/exam")
    public String exam(Model model,
                       @RequestParam(name = "qt", required = false, defaultValue = "subject") String queryType,
                       @RequestParam(name = "q", required = false, defaultValue = "") String query,
                       @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println("받아온 값" + queryType +"~" + query);

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Exam.class, queryType, query, pageable);
        //        Page<Object> pagination = examRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);

        return "board/exam/main";
    }

    /* 글 작성 */
    @PostMapping("/exam/new")
    public String createExam(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file){
        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        examService.createExam(title, story, file);

        return "redirect:/exam";
    }

    /* 새 글 작성 */
    @GetMapping("/exam/new")
    public String showNewExam(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/exam/new";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/exam/{no}")
    public String showExamDetail(@PathVariable("no") Long no, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        ExamDto exam = examService.getExamByNo(no);
        if (exam == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");


        // 조회수 증가
        examService.increaseHitCount(no);

        model.addAttribute("bbsName", "exam");
        model.addAttribute("bbsNo", no);
        model.addAttribute("exam", exam);

        return "board/exam/detail";
    }

    /* 글 수정 */
    @GetMapping("/exam/edit/{no}")
    public String showEditExamForm(@PathVariable Long no, Model model){
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Exam exam = examRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(exam, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("exam", exam);
        return "board/exam/edit";
    }

    @PostMapping("/exam/edit")
    public String updateExam(@RequestParam Long no, @RequestParam String title, @RequestParam String story){
        examService.updateExam(no, title, story);
        return "redirect:/exam/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/exam/delete/{no}")
    public String deleteExam(@PathVariable("no") Long no){
        Exam exam = examRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(exam, loggedInUsername)) {
            examService.deleteExam(no);
            return "redirect:/exam";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/e/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request){
        examService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        if (referer != null) return "redirect:" + referer;
        return "redirect:/exam";
    }

    private boolean isAuthorizedUser(Exam exam, String loggedInUsername) {
        // 작성자나 관리자면 true
        return exam.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
