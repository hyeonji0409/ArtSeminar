package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username);
        return user.getRole().equals("ROLE_ADMIN");
    }


    public Page<User> paginationFindAllUsers(int pageNumber, int pageSize, Sort sort) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }


    public void updatePasswordEncoder(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public ResponseEntity<String> checkSignUpValue(String valueName,
                                                   Map<String, String> payload) {


//        Todo 이하 3종목이 유니크하지 않을 떄 오류 발생(회원가입되버림)
//        org.hibernate.NonUniqueResultException: Query did not return a unique result: 9 results were returned
        User foundUser = switch (valueName) {
            case "username" -> userRepository.findByUsername(payload.get("value"));
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


        if (foundUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("invalid");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("valid");
        }
    }



    public boolean updateUser(String logInUsername, UserDto userDto, String clientIp) {
        // IP 주소 가져오기
        System.out.println("Client IP: " + clientIp);
        System.out.println("회원정보수정");
        System.out.println(userDto.toString());

        if (!(
                logInUsername.equals( userDto.getUsername() )
                || isAdmin(logInUsername))
        ) return false;

        // 생년월일 포멧
        String formattedBirth = null;
        try {
            DateTimeFormatter inputDtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            formattedBirth = LocalDate.parse(userDto.getBirth(), inputDtf).format(dbDtf).toString();
        } catch (DateTimeParseException e) {
            return false;
        }

        System.out.println("수정요청 받음" +  userDto.toString());


        User oldUser = userRepository.findByUsername(userDto.getUsername());

        // todo 예외처리
        if (userDto.getPassword()!=null && !userDto.getPassword().isEmpty())oldUser.setPassword( passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getName()!=null && !userDto.getName().isEmpty()) oldUser.setName(userDto.getName());
        if (userDto.getSex()!=null && !userDto.getSex().isEmpty()) oldUser.setSex(userDto.getSex());
        if (userDto.getBirth()!=null && !userDto.getBirth().isEmpty()) oldUser.setBirth(formattedBirth);
        if (userDto.getTel()!=null && !userDto.getTel().isEmpty()) oldUser.setTel(userDto.getTel());
        if (userDto.getEmail()!=null && !userDto.getEmail().isEmpty()) oldUser.setEmail(userDto.getEmail());
        if (userDto.getZipcode()!=null && !userDto.getZipcode().isEmpty()) oldUser.setZipcode(userDto.getZipcode());
        if (userDto.getRoadAddress()!=null && !userDto.getRoadAddress().isEmpty()) oldUser.setRoadAddress(userDto.getRoadAddress());
        if (userDto.getDetailAddress()!=null && !userDto.getDetailAddress().isEmpty()) oldUser.setDetailAddress(userDto.getDetailAddress());
        if (userDto.getYear()!=null) oldUser.setYear(userDto.getYear());
        if (userDto.getRole()!=null && !userDto.getRole().isEmpty() && isAdmin(logInUsername)) oldUser.setRole(userDto.getRole());

        userRepository.save(oldUser);

        return true;
    }



    public ResponseEntity<String> PostWithdrawal(Map<String, Object> payload,
                                                 String username) {

        System.out.println("아이디가 삭제될 예정" + payload);

        if (username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            User requestedUser = userRepository.findByUsername(username);

            if (!isAdmin(requestedUser.getUsername())
                    && !requestedUser.getUsername().equals( (String) payload.get("username"))
            ) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

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
