package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByNo(Long no);
    List<Comment> findAllByBbsNo(Integer bbsno);
    List<Comment> findByBbsNoIn(List<Integer> bbsnos);
    List<Comment> findAllByBbsnameAndBbsNo(String bbsname, Integer bbsno);
    Page<Comment> findAllByUser_Username(String username, Pageable pageable);
}
