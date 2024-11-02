package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.artineer.artineer_renewal.repository.UserRepository;

@Controller
public class MyPageController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mypage")
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "myPage/myPage";
    }

    @GetMapping("/mypage/activity1")
    public String activity1() { return "myPage/mypageAct1"; }

}

