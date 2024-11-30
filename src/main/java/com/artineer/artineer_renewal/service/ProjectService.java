package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.repository.ProjectRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;
}
