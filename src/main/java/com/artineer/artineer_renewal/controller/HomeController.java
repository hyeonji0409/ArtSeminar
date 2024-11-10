package com.artineer.artineer_renewal.controller;


import com.artineer.artineer_renewal.entity.Popup;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.PopupRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final PopupRepository popupRepository;

    public HomeController(UserRepository userRepository, PopupRepository popupRepository) {
        this.userRepository = userRepository;
        this.popupRepository = popupRepository;
    }


    @GetMapping("/")
    public String home(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

//        todo db 등에
        List<Popup>  popups = popupRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsVisible(LocalDateTime.now(), LocalDateTime.now(), true);
        model.addAttribute("popups", popups);
//        PopupDTO tempPopup = new PopupDTO(121414L, "", "공지사항", "2024 하반기 부원모집 안내","/data/IMG_5885.png");
//        model.addAttribute("popup", tempPopup);

        return "index";
    }


    @RequestMapping("/denied")
    public String denied() {
        return "/error/access-denied";
    }


    @RequestMapping("/about")
    public String about(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "about/about";
    }


    @RequestMapping("/base")
    public String base(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "basePage";
    }
}
