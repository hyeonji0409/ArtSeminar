package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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


    public Page<User> paginationFindUsers(int pageNumber, int pageSize, Sort sort) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }


    public void updatePasswordEncoder(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
