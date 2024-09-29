package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.AdminService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
public class adminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PasswordEncoder passwordEncoder;


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

        return adminService.checkSignUpValue(valueName, payload);
    }


    @PostMapping("/user/update")
    public String updateUser(UserDto userDto,
                             RedirectAttributes redirectAttributes ) {
        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();
        System.out.println("Client IP: " + clientIp);

        // 생년월일 포멧
        String formattedBirth = userDto.getBirth().substring(0,4) + '/' + userDto.getBirth().substring(4,6) + '/' + userDto.getBirth().substring(6,8);

        System.out.println("수정요청 받음");

        User oldUser = userRepository.findByUsername(userDto.getUsername());

        if (!userDto.getPassword().isEmpty()) oldUser.setPassword( passwordEncoder.encode(userDto.getPassword()));
        oldUser.setName(userDto.getName());
        oldUser.setSex(userDto.getSex());
        oldUser.setBirth(formattedBirth);
        oldUser.setTel(userDto.getTel());
        oldUser.setEmail(userDto.getEmail());
        oldUser.setZipcode(userDto.getZipcode());
        oldUser.setRoadAddress(userDto.getRoadAddress());
        oldUser.setDetailAddress(userDto.getDetailAddress());
        oldUser.setYear(userDto.getYear());
        oldUser.setRole(userDto.getRole());

        userRepository.save(oldUser);

        return "redirect:/admin";
    }
}
