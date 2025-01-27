package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
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
            log.error("User not found!!! {}", loggedInUsername);
            throw new UsernameNotFoundException("User not found");
        }

        Comment originComment = commentRepository.findByNo(no.longValue());

        if (originComment == null) {
            log.error("comment not found!!! {}", no.longValue());
            return false;
        }

        // 인가 권한 확인
        if (user.getUsername().equals(originComment.getUser().getUsername()) || userService.isAdmin(loggedInUsername)) {

            // 자식이 있다면 소프트딜리트
            List<Comment> repliesSize = commentRepository.findAllByReplys(originComment.getNo());
            if (repliesSize.isEmpty()) {
                commentRepository.delete(originComment);
                // 삭제한 댓글의 부모가 있을 시, 그 부모가 삭제된 댓글이고 자식도 없을 때 부모도 하드딜리트
                Comment parentCmt = commentRepository.findByNo(Long.valueOf(originComment.getReplys()));
                if (parentCmt != null &&
                        parentCmt.getMemo().equals("삭제된 댓글입니다.") &&
                        commentRepository.findAllByReplys(parentCmt.getNo()).isEmpty()
                ) {
                    // 재귀적으로 조상를 확인 후 삭제.
                    deleteComment(Math.toIntExact(parentCmt.getNo()));
                }
            }
            else {
                originComment.setMemo("삭제된 댓글입니다.");
                commentRepository.save(originComment);
            }
        } else {
            log.error("co!!! {}", userService.isAdmin(loggedInUsername));
            return false;
        }

        return true;
    }


}
