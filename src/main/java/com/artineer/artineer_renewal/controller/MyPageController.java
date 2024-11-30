package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.artineer.artineer_renewal.repository.UserRepository;

import java.util.List;

@Controller
public class MyPageController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GalleryRepository galleryRepository;
//    @Autowired
//    private CommentRepository commentRepository;

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


        List<Gallery> galleryList = galleryRepository.findAllByUser(user);
        for(Gallery gallery : galleryList) {
            System.out.println(gallery);
        }

        model.addAttribute("articles", galleryList);

        return "myPage/mypageAct1"; }

//    @GetMapping("/mypage/activity2")
//    public String activity2(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//        model.addAttribute("user", user);
//
//        List<Comment> commentList = commentRepository.findAllByUser_id(user.getUsername());
//        model.addAttribute("comments", commentList);
//
//        return "myPage/mypageAct2";
//    }
      @GetMapping("mypage/remypage")
        public String remypage(Model model) {
        return "myPage/mypage_re";
      }

}

