package com.artineer.artineer_renewal.controller;


import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.EmailService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmailController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @PostMapping("/email/send")
    public ResponseEntity<Map<String, Object>> sendEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email) {

        System.out.println("Email sent");

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
            e.printStackTrace();
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

        System.out.println("Email verified");

        String code = emailService.getVerificationCode(email);
        User user = userRepository.findByEmail(email);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("code", 200);

        if (what.equals("username")) {
            responseData.put("username", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }


        if (code==null || !code.equals(injeung) || !user.getName().equals(name)) {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); }

        userService.changePassword(user, "a1234");
        responseData.put("password", "a1234");

        return ResponseEntity.ok().body(responseData);
    }


    @GetMapping(value = "/email", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getEmail(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String header = "로그인된 관리자 계정 : " + username + "  [ " + LocalDateTime.now() + "]\n";

        if (username.equals("anonymousUser")) throw new AccessDeniedException("관리자만 접근가능합니다.");
        if (!userService.isAdmin(username)) throw new AccessDeniedException("관리자만 접근가능합니다.");

        return header + emailService.getVerificationMapToList().toString();
    }

}
