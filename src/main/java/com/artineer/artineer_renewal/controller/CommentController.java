package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;


    @PostMapping("/comments/add")
    public ResponseEntity<Resource> addComment(@RequestParam String bbsName,
                             @RequestParam Integer bbsNo,
                             @RequestParam String username,
                             @RequestParam(required = false) Integer replys,
                             @RequestParam String memo) {

        boolean result = commentService.createComment(bbsName, bbsNo, replys, memo);

        ResponseEntity<Resource> res = null;

        if (result) {
            res = ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return res;
    }

    @PostMapping("/comments/update")
    public ResponseEntity<Resource> updateComment(@RequestParam Integer no,
                                                  @RequestParam(required = false) Integer replys,
                                                  @RequestParam String memo) {

        boolean result = commentService.updateComment(no, memo);

        ResponseEntity<Resource> res = null;

        if (result) {
            res = ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return res;
    }

    @PostMapping("/comments/delete")
    public ResponseEntity<Resource> deleteComment(@RequestParam Integer no) {

        boolean result = commentService.deleteComment(no);

        ResponseEntity<Resource> res = null;
        if (result) {
            res = ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return res;
    }


}
