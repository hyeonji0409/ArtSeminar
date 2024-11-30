package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBbsNo(Integer bbsno);
    List<Comment> findAllByBbsnameAndBbsNo(String bbsname, Integer bbsno);
}
