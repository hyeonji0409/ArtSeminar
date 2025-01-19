package com.artineer.artineer_renewal.config;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.artineer.artineer_renewal.service.InformationFileDataService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InformationFileDataService informationFileDataService;

    @ModelAttribute
    public void addUserInfo(Model model,
                            @AuthenticationPrincipal User user) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
    }

    @ModelAttribute
    public void addFooterInfo(Model model) {
        model.addAttribute("headOfficerName", informationFileDataService.get("headOfficerName"));
        model.addAttribute("contactNumber", informationFileDataService.get("contactNumber"));
        model.addAttribute("email", informationFileDataService.get("email"));
    }
}
