package com.artineer.artineer_renewal.controller;


import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.EmailService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class EmailController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void logRequestDetails(HttpServletRequest request,
                                  @AuthenticationPrincipal User user) {

        log.info("email accessed by {}: [{}] {} at {}",
                user.getUsername(),
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );
    }

    @PostMapping("/email/send")
    public ResponseEntity<Map<String, Object>> sendEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email) {

        User user = userRepository.findByEmail(email);

        if (user == null || !user.getName().equals(name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String code = emailService.createVerificationCode(email);

        try {
            emailService.sendEmail(
                    email,
                    "[아티니어] 계정찾기 인증번호",
                    "인증번호 : " + code
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @PostMapping("/email/verification")
    public ResponseEntity<Map<String, Object>> verifyEmail(
            @RequestParam(value = "what", required = false) String what,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "injeung", required = false) String injeung) {

        Map<String, Object> responseData = new HashMap<>();
        User user = userRepository.findByEmail(email);

        if (user==null || !user.getName().equals(name))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);;

        if (what.equals("username")) {
            responseData.put("username", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }

        String code = emailService.getVerificationCode(email);
        if (code==null || !code.equals(injeung) || !user.getName().equals(name))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);

        emailService.removeVerificationCode(email);

        responseData.put("code", 200);
        String tempPassword = emailService.getRandomString(5, false);
        userService.changePassword(user, tempPassword);
        responseData.put("password", tempPassword);

        return ResponseEntity.ok().body(responseData);
    }


    @GetMapping(value = "/email", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getEmail(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!userService.isAdmin(username)) throw new AccessDeniedException("관리자만 접근가능합니다.");

        String title = "로그인된 관리자 계정 : " + username + "  [ " + LocalDateTime.now() + "] &nbsp&nbsp&nbsp&nbsp&nbsp" + "<button type=\"button\" onclick=\"window.location.href='/email/reset'\">인증정보 초기화</button></br>";

        return title + emailService.getVerificationMapToList().toString();
    }

    @GetMapping(value = "/email/reset", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String resetEmail(@AuthenticationPrincipal User user) {
        if (!userService.isAdmin(user.getUsername())) throw new AccessDeniedException("관리자만 접속가능합니다.");

        String title = "로그인된 관리자 계정 : " + user.getUsername() + "  [ " + LocalDateTime.now() + "] &nbsp&nbsp&nbsp&nbsp&nbsp" + "<button type=\"button\" onclick=\"window.location.href='/email/reset'\">인증정보 초기화</button></br>";

        emailService.resetVerification();

        return title + emailService.getVerificationMapToList().toString();
    }

}
