package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/admin")
    public String adminPage(Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
                            @ModelAttribute UserSearchDTO userSearchDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        System.out.println(userSearchDTO.toString());

        String sortProperty =  userSearchDTO.getSort().orElse("name");
        Sort.Direction direction = userSearchDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<User> users = null;
        String queryValue = userSearchDTO.getQueryValue().orElse("");
        String sex = userSearchDTO.getSex().orElse("");
        String role = userSearchDTO.getRole().orElse("");
        Pageable pageable = PageRequest.of(
                userSearchDTO.getPage().orElse(page) - 1,
                userSearchDTO.getPageSize().orElse(pageSize),
                Sort.by(direction, sortProperty));

        if (userSearchDTO.getQuery().isEmpty() || userSearchDTO.getQuery().get().isEmpty()) {
            users = userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
        } else if (userSearchDTO.getQuery().get().equals("name")) {
            users = userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
        } else if (userSearchDTO.getQuery().get().equals("username")) {
            users = userRepository.findByUsernameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
        } else if (userSearchDTO.getQuery().get().equals("email")) {
            users = userRepository.findByEmailContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
        } else if (userSearchDTO.getQuery().get().equals("tel")) {
            users = userRepository.findByTelContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
        } else if (userSearchDTO.getQuery().get().equals("address")) {
            System.out.println("address" + sex);
            users = userRepository.findByRoadAddressContainingAndSexStartingWithAndRoleContainingOrDetailAddressContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, queryValue, sex, role, pageable);
        }

        if (userSearchDTO.getPage().isEmpty()) { userSearchDTO.setPage(Optional.of(page==null?1:page)); }
        if (userSearchDTO.getPageSize().isEmpty()) { userSearchDTO.setPageSize(Optional.of(pageSize==null?10:pageSize)); }


        model.addAttribute("users", users);
        model.addAttribute("userSearchDTO", userSearchDTO);

        return "/admin/admin";
    }



    // 회원정보 갱신
    @PostMapping("/user/update/check-{valueName}")
    @ResponseBody
    public ResponseEntity<String> checkSignUpValue(@PathVariable(name = "valueName") String valueName,
                                                   @RequestBody Map<String, String> payload) {

//        Todo 이하 2종목이 유니크하지 않을 떄 오류 발생(회원가입되버림)
//        org.hibernate.NonUniqueResultException: Query did not return a unique result: 9 results were returned
        User foundUser = switch (valueName) {
            case "email" -> userRepository.findByEmail(payload.get("value"));
            case "tel" -> userRepository.findByTel(payload.get("value"));
            default -> null;
        };

        System.out.println("sign-up check: "+ valueName);
        System.out.println(payload);
        System.out.println(
                (foundUser ==
                        null ?
                        "It is possible to register a new user...\n null" :
                        "submitted value is already exist in database...\n" + foundUser.toString()
                ) + "\n-------------------------------------------------\n"
        );

        if (foundUser != null && foundUser.equals(userRepository.findByUsername(payload.get("username")))) {
            foundUser = null;
        }


        if (foundUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("invalid");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("valid");
        }

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
