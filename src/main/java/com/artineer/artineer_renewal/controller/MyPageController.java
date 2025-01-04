package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.artineer.artineer_renewal.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyPageController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/mypage")
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "myPage/myPage";
    }

    @GetMapping("/mypage/activity1")
    public String activity1(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<Class<?>> targetArticle = new ArrayList<>();
        targetArticle.add(Notice.class);
        targetArticle.add(Gallery.class);
        targetArticle.add(Exam.class);
        targetArticle.add(Reference.class);
        targetArticle.add(Greeting.class);

        Pageable pageable = PageRequest.of(
                0,
                9,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(targetArticle, "username", user.getUsername(), pageable);

        model.addAttribute("pagination", pagination);

        return "myPage/mypageAct1";
    }

    @GetMapping("/mypage/activity2")
    public String activity2(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<Class<?>> targetArticle = new ArrayList<>();
        targetArticle.add(Notice.class);
        targetArticle.add(Gallery.class);
        targetArticle.add(Exam.class);
        targetArticle.add(Reference.class);
        targetArticle.add(Greeting.class);

        Pageable pageable = PageRequest.of(
                0,
                9,
                Sort.by(Sort.Direction.DESC, "regdate"));

//        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(targetArticle, "username", user.getUsername(), pageable);

        Page<Comment> pagination = commentRepository.findAllByUser_Username(user.getUsername(), pageable);

        model.addAttribute("pagination", pagination);

        return "myPage/mypageAct2";
    }
      @GetMapping("mypage/remypage")
        public String remypage(Model model) {
        return "myPage/mypage_re";
      }

}

