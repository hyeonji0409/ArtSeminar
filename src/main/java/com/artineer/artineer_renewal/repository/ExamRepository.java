package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findAll(Pageable pageable);

    List<Exam> findAllByFile(String fileName);
}
