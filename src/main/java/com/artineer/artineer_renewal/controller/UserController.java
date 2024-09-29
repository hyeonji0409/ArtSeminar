package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;


    @RequestMapping("/user/sign-in")
    public String signIn(Model model,
                        @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("error", error);

        return "/user/sign-in";
    }

    @PostMapping("/user/sign-up")
    public String saveUser(UserDto userDto) {
        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();

        System.out.println("Client IP: " + clientIp);

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);


        // Todo js에서도 포멧하면 좋은가
        String formattedBirth = userDto.getBirth().substring(0,4) + '/' + userDto.getBirth().substring(4,6) + '/' + userDto.getBirth().substring(6,8);

        User user = new User(
                null,
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getName(),
                userDto.getSex(),
                formattedBirth,
                userDto.getTel(),
                userDto.getEmail(),
                userDto.getZipcode(),
                userDto.getRoadAddress(),
                userDto.getDetailAddress(),
                userDto.getYear(),
                "ROLE_GUEST",
                formattedDate,
                clientIp
        );

        userRepository.save(user);
        System.out.println(user);

        return  "redirect:/user/sign-in";
    }

    @GetMapping("/user/sign-up")
    public String signUp() {
        return "/user/sign-up";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "user/access-denied";
    }


//     회원가입 폼 유효성 검사하기
    @PostMapping("/sign-up/check-{valueName}")
    @ResponseBody
    public ResponseEntity<String> checkSignUpValue(@PathVariable(name = "valueName") String valueName,
                                                   @RequestBody Map<String, String> payload) {

        return userService.checkSignUpValue(valueName, payload);
    }


    /* 아이디/비밀번호 찾기 */
    @GetMapping("/user/sign-find/{what}")
    public String signFind(@PathVariable String what,
                           Model model) {

        if (what.equals("id")) {
            model.addAttribute("what", "id");
        } else if (what.equals("pw")) {
            model.addAttribute("what", "pw");
        }

        return "/user/sign-find";
    }


    @GetMapping("/user/sign-withdrawal")
    public String withdrawal() {
        return "/user/sign-withdrawal";
    }
    @PostMapping("/user/sign-withdrawal")
    public String PostWithdrawal() {
        return "redirect:/";
    }

    @GetMapping("/user/sign-withdrawalConfirm")
    public String withdrawalConfirm() {
        return "/user/sign-withdrawalConfirm";
    }



//    @PostMapping("/sign-withdrawalConfirm")
//    public String PostWithdrawalConfirm() {
//        // Todo 회원정보 삭제화
//        return "redirect:/";
//    }

    @PostMapping("/user/withdrawal")
    public ResponseEntity<String> PostWithdrawal(@RequestBody Map<String, Object> payload) {

        System.out.println("다음 아이디가 삭제될 예정:\n" + payload.get("username"));

        User user = null;
        try {
            user = userRepository.findByUsername((String) payload.get("username"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("notFound");
        }

        userRepository.deleteById(user.getNo());
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }


}
