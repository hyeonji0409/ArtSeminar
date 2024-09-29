package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


// 단순 페이지 확인용 컨트롤러
@Controller
public class TempController {
    private final UserRepository userRepository;

    public TempController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/base")
    public String basePage(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals("anonymousUser")) {
            model.addAttribute("user", username);

        } else{
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }

        return "/basePage";
    }


}
