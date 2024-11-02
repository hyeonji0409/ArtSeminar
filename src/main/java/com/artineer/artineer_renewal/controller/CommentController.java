package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;


    @PostMapping("/comments/add")
    public String addComment(@RequestParam String bbsName,
                             @RequestParam Integer bbsNo,
                             @RequestParam String username,
                             @RequestParam(required = false) Integer replys,
                             @RequestParam String memo) {

        commentService.createComment(bbsName, bbsNo, replys, memo);

        return "redirect:/" + bbsName + "/" + bbsNo;
    }


}
