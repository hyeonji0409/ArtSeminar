package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExamController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;

    @GetMapping("/exam")
    public String exam(){
        return null;
    }

    /* 글 작성 */
    @PostMapping("/exam/new")
    public String createExam(){
        return null;
    }

    /* 새 글 작성 */
    @GetMapping("/exam/new")
    public String showNewExam(){
        return null;
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/exam/{no}")
    public String showExamDetail(){
        return null;
    }

    /* 글 수정 */
    @GetMapping("/exam/edit/{no}")
    public String showEditExamForm(){
        return null;
    }

    @PostMapping("/exam/edit")
    public String updateExam(){
        return null;
    }

    /* 글 삭제 */
    @GetMapping("/exam/delete/{no}")
    public String deleteExam(){
        return null;
    }

    @GetMapping("/exam/delete/{file}")
    public String deleteFile(){
        return null;
    }

    private boolean isAuthorizedUser(Exam exma, String loggedInUsername) {
        // 작성자나 관리자면 true
        return exma.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
