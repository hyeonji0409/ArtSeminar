package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    // 댓글 작성
    public boolean createComment(String bbsname, Integer bbsNo, Integer replys, String memo) {
        // user 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);

        // User 엔티티 조회
        User user = userRepository.findByUsername(loggedInUsername);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setBbsname(bbsname);
        comment.setBbsNo(bbsNo);
        comment.setName(user.getName());
        comment.setYear(user.getYear());
        comment.setReplys(replys);
        comment.setMemo(memo);
        comment.setRegdate(formattedDate);

        commentRepository.save(comment);

        return true;
    }


    public boolean updateComment(Integer no, String memo) {
        // user 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");
        String formattedDate = now.format(formatter);

        // User 엔티티 조회
        User user = userRepository.findByUsername(loggedInUsername);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Comment originComment = commentRepository.findByNo(no.longValue());

        if (originComment == null) {
            return false;
        }
        else if (user.getUsername().equals(originComment.getUser().getUsername())) {
            originComment.setMemo(memo);
            commentRepository.save(originComment);
        } else {
            return false;
        }

        return true;
    }

    public boolean deleteComment(Integer no) {
        // user 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // User 엔티티 조회
        User user = userRepository.findByUsername(loggedInUsername);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Comment originComment = commentRepository.findByNo(no.longValue());

        if (originComment == null) {
            return false;
        }
        else if (user.getUsername().equals(originComment.getUser().getUsername()) || userService.isAdmin(loggedInUsername)) {
            commentRepository.delete(originComment);
        } else {
            return false;
        }

        return true;
    }


}
