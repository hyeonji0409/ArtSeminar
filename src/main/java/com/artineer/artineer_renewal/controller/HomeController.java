package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.PopupDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    public String home(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals("anonymousUser")) {
            model.addAttribute("user", username);
        } else{
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }

//        todo db 등에 영속저장해야 할 듯
        PopupDTO popup = new PopupDTO(121414L, "", "공지사항", "2024 하반기 부원모집 안내","/data/IMG_5885.png");
        model.addAttribute("popup", popup);

        return "index";
    }


    @RequestMapping("/denied")
    public String denied() {
        return "/user/access-denied";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals("anonymousUser")) {
            model.addAttribute("user", username);

        } else{
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }

        return "about/about";
    }
}
