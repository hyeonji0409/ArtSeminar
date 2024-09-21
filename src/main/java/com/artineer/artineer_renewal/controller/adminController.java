package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class adminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String adminPage(Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals("anonymousUser")) {
            model.addAttribute("user", username);

        } else {
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }

        Page<User> users = userService.paginationFindUsers(page, pageSize, Sort.by(Sort.Direction.ASC, "name"));
        model.addAttribute("users", users);

        return "/admin/admin";
    }
}
