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

import java.util.NoSuchElementException;

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





//        String sortProperty =  userSearchDTO.getSort().orElse("name");
//        Sort.Direction direction = userSearchDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

//        Page<User> users = userRepository.findByUsernameContaining(
//                userSearchDTO.getQueryValue().orElse(null),
//                PageRequest.of(
//                        (userSearchDTO.getPage() != null ? (int) userSearchDTO.getPage().get() : 0),
//                        userSearchDTO.getPageSize() != null ? (int) (userSearchDTO.getPageSize().get()) : 10,
//                        Sort.by(direction, sortProperty))
//        );

        Page<User> users;

//        try {
//            users = userRepository.findByUsernameContaining(
//                    userSearchDTO.getQueryValue().orElse(null),
//                    PageRequest.of(
//                            (int) userSearchDTO.getPage().get(),
//                            (int) userSearchDTO.getPageSize().get(),
//                            Sort.by(direction, sortProperty)
//                    )
//            );
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            users = userRepository.findAll(PageRequest.of(0, 10));
//        }



//        model.addAttribute("users", users);
        model.addAttribute("userSearchDTO", userSearchDTO);

        return "/admin/admin";
    }

//    @PostMapping("update-user")
//    public String updateUser(@ModelAttribute("userSearchDTO") UserSearchDTO userSearchDTO) {
//        userSearchDTO
//    }
}
