package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.repository.GreetingRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private GreetingRepository greetingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;
}
