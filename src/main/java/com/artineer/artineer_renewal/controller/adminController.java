package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.AdminService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
public class adminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;


    // 관리자의 사용자 정보 쿼리 화면
    @RequestMapping("/admin")
    public String adminPage(Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
                            @ModelAttribute UserSearchDTO userSearchDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println(userSearchDTO.toString());

        Page<User> users = adminService.paginationByQuery(userSearchDTO, page, pageSize);
        if (userSearchDTO.getPage().isEmpty()) { userSearchDTO.setPage(Optional.of(page==null?1:page)); }
        if (userSearchDTO.getPageSize().isEmpty()) { userSearchDTO.setPageSize(Optional.of(pageSize==null?10:pageSize)); }

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("userSearchDTO", userSearchDTO);

        return "/admin/admin";
    }



    // 회원정보 갱신
    @PostMapping("/user/update/check-{valueName}")
    @ResponseBody
    public ResponseEntity<String> checkSignUpValue(@PathVariable(name = "valueName") String valueName,
                                                   @RequestBody Map<String, String> payload) {

        return adminService.checkUserValue(valueName, payload);
    }
}
