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

    // 댓글 작성
    public String createComment(String bbsname, int bbsNo, int replys, String memo) {
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

        return null;
    }

}
