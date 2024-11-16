package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.TurnstileService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.ProtocolException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TurnstileService turnstileService;


    @RequestMapping("/user/sign-in")
    public String signIn(Model model,
                        @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("error", error);

        return "/user/sign-in";
    }


    @PostMapping("/user/sign-up")
    public String saveUser(UserDto userDto,
                           @RequestParam(value = "cf-turnstile-response", required = true) String turnstileKey) throws ProtocolException {

        boolean isBot = !turnstileService.getVerification(turnstileKey, request);
        if (isBot) throw new AccessDeniedException("의심적인 활동입니다. 나중에 다시 시도해주세요.");


        System.out.println("Client IP: " + request.getRemoteAddr());

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);


        // Todo js에서도 포멧하면 좋은가
        String formattedBirth = null;
        try {
            DateTimeFormatter inputDtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            formattedBirth = LocalDate.parse(userDto.getBirth(), inputDtf).format(dbDtf).toString();
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("생년월일이 잘못 입력되었습니다.", userDto.getBirth(), 0);
        }

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
                request.getRemoteAddr()
        );

        userRepository.save(user);
        System.out.println(user);

        return  "redirect:/user/sign-in";
    }

    @GetMapping("/user/sign-up")
    public String signUp() {
        return "/user/sign-up";
    }


    // todo @RequestMapping("/denied") 랑 동일함.
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }


//     회원가입 폼 유효성 검사하기
    @PostMapping("/sign-up/check-{valueName}")
    @ResponseBody
    public ResponseEntity<String> checkSignUpValue(@PathVariable(name = "valueName") String valueName,
                                                   @RequestBody Map<String, String> payload) {

        return userService.checkSignUpValue(valueName, payload);
    }




    // todo 이런 특정 페이지를 요구하지 않는 작업은 클라이언트에서 js로 fetch 해서 통신하는 것이 나을 듯
    @PostMapping("/user/update")
    public String updateUser(Model model,
                             UserDto userDto) {

        System.out.println("/user/update");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();
        boolean isSuccess = userService.updateUser(username, userDto, clientIp);

        String redirectAddress =
                request.getHeader("Referer") != null ?
                        request.getHeader("Referer") :
                        (String) request.getSession().getAttribute("redirectUrl");

        System.out.println("리다이렉 주소는 " + redirectAddress);
        return "redirect:" + redirectAddress;
    }


    @PostMapping("/user/update-info")
    public ResponseEntity<String> updateInfoUser(Model model,
                             UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();
        boolean isSuccess = userService.updateUser(username, userDto, clientIp);

        if (isSuccess) return ResponseEntity.status(HttpStatus.OK).body("success");
        else  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
    }

    @GetMapping("/user/update-info")
    public String updateUserInfo(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);

        model.addAttribute("username", username);

        return "/user/update-info";
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
    public String withdrawal(Model model,
                             @AuthenticationPrincipal User user) {
        if (user==null) throw new AccessDeniedException("로그인 후 이용가능합니다.");

        model.addAttribute("user", user);
        return "/user/sign-withdrawal";
    }

    @GetMapping("/user/sign-withdrawalConfirm")
    public String withdrawalConfirm(Model model,
                                    @AuthenticationPrincipal User user) {
        if (user==null) throw new AccessDeniedException("로그인 후 이용가능합니다.");

        model.addAttribute("user", user);
        return "/user/sign-withdrawalConfirm";
    }



    @PostMapping("/user/withdrawal")
    public ResponseEntity<String> PostWithdrawal(@AuthenticationPrincipal User user,
                                                 @RequestParam Map<String, Object> payload) {

        return userService.PostWithdrawal(payload, user.getUsername());
    }



}
