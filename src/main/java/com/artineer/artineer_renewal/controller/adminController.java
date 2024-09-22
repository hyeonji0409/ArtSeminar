package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class adminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @RequestMapping("/admin")
    public String adminPage(Model model,
//                            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
//                            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
                            @ModelAttribute UserSearchDTO userSearchDTO ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals("anonymousUser")) {
            model.addAttribute("user", username);
        } else {
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }


        System.out.println(userSearchDTO.toString());





        String sortProperty =  userSearchDTO.getSort().orElse("name");
        Sort.Direction direction = userSearchDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<User> users = null;

        if (userSearchDTO.getQuery().isEmpty() ||userSearchDTO.getQuery().get().isEmpty()) {
            users = userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(
                    userSearchDTO.getQueryValue().orElse(""),
                    userSearchDTO.getSex().orElse(""),
                    userSearchDTO.getRole().orElse(""),
                    PageRequest.of(
                            userSearchDTO.getPage() == null ? 0 : (int) userSearchDTO.getPage().get() - 1,
                            userSearchDTO.getPageSize() == null ? 10 : (int) (userSearchDTO.getPageSize().get()),
                            Sort.by(direction, sortProperty))
            );
        } else if (userSearchDTO.getQuery().get().equals("name")) {
            users = userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(
                    userSearchDTO.getQueryValue().orElse(""),
                    userSearchDTO.getSex().orElse(""),
                    userSearchDTO.getRole().orElse(""),
                    PageRequest.of(
                            userSearchDTO.getPage() == null ? 0 : (int) userSearchDTO.getPage().get() - 1,
                            userSearchDTO.getPageSize() == null ? 10 : (int) (userSearchDTO.getPageSize().get()),
                            Sort.by(direction, sortProperty))
            );
        } else if (userSearchDTO.getQuery().get().equals("username")) {
            users = userRepository.findByUsernameContainingAndSexStartingWithAndRoleContaining(
                    userSearchDTO.getQueryValue().orElse(""),
                    userSearchDTO.getSex().orElse(""),
                    userSearchDTO.getRole().orElse(""),
                    PageRequest.of(
                            userSearchDTO.getPage() == null ? 0 : (int) userSearchDTO.getPage().get() - 1,
                            userSearchDTO.getPageSize() == null ? 10 : (int) (userSearchDTO.getPageSize().get()),
                            Sort.by(direction, sortProperty))
            );
        } else if (userSearchDTO.getQuery().get().equals("email")) {
            users = userRepository.findByEmailContainingAndSexStartingWithAndRoleContaining(
                    userSearchDTO.getQueryValue().orElse(""),
                    userSearchDTO.getSex().orElse(""),
                    userSearchDTO.getRole().orElse(""),
                    PageRequest.of(
                            userSearchDTO.getPage() == null ? 0 : (int) userSearchDTO.getPage().get() - 1,
                            userSearchDTO.getPageSize() == null ? 10 : (int) (userSearchDTO.getPageSize().get()),
                            Sort.by(direction, sortProperty))
            );
        } else if (userSearchDTO.getQuery().get().equals("tel")) {
            users = userRepository.findByTelContainingAndSexStartingWithAndRoleContaining(
                    userSearchDTO.getQueryValue().orElse(""),
                    userSearchDTO.getSex().orElse(""),
                    userSearchDTO.getRole().orElse(""),
                    PageRequest.of(
                            userSearchDTO.getPage() == null ? 0 : (int) userSearchDTO.getPage().get() - 1,
                            userSearchDTO.getPageSize() == null ? 10 : (int) (userSearchDTO.getPageSize().get()),
                            Sort.by(direction, sortProperty))
            );
        }

        if (userSearchDTO.getPage()==null) {
            userSearchDTO.setPage(Optional.of(1));
        }

        if (userSearchDTO.getPageSize()==null) {
            userSearchDTO.setPageSize(Optional.of(10));
        }


        model.addAttribute("users", users);
        model.addAttribute("userSearchDTO", userSearchDTO);

        return "/admin/admin";
    }

//    @PostMapping("update-user")
//    public String updateUser(@ModelAttribute("userSearchDTO") UserSearchDTO userSearchDTO) {
//        userSearchDTO
//    }
}
