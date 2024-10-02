package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Component
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    // 회원정보 값 유효성 검사
    public ResponseEntity<String> checkUserValue(String valueName,
                                                 Map<String, String> payload) {

//        Todo 이하 2종목이 유니크하지 않을 떄 오류 발생(오류발생)
//        org.hibernate.NonUniqueResultException: Query did not return a unique result: 9 results were returned
        User foundUser = switch (valueName) {
//            아이디는 고정값임.
//            case "username" -> userRepository.findByUsername(payload.get("value"));
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

        // 검사를 요청한 정보로 찾은 foundUser 가 요청을 보낸 유저와 같을 경우, 중복허용
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


    public Page<User> paginationByQuery(UserSearchDTO userSearchDTO, Integer page, Integer pageSize) {
        String queryValue = userSearchDTO.getQueryValue().orElse("");
        String sex = userSearchDTO.getSex().orElse("");
        String role = userSearchDTO.getRole().orElse("");
        String sortProperty =  userSearchDTO.getSort().orElse("name");
        Sort.Direction direction = userSearchDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(
                userSearchDTO.getPage().orElse(page) - 1,
                userSearchDTO.getPageSize().orElse(pageSize),
                Sort.by(direction, sortProperty));

        Page<User> users =
                switch (userSearchDTO.getQuery().orElse("")) {
                    case "name" -> userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "username" -> userRepository.findByUsernameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "email" -> userRepository.findByEmailContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "tel" -> userRepository.findByTelContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "address" -> userRepository.findByRoadAddressContainingAndSexStartingWithAndRoleContainingOrDetailAddressContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, queryValue, sex, role, pageable);
                    default -> userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                };

        return users;
    }


}
