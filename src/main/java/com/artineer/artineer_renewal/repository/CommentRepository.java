package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
