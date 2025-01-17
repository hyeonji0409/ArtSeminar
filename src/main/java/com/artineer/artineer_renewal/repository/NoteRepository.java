package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Exam;
import com.artineer.artineer_renewal.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findAll(Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.name LIKE %:name%")
    Page<Note> findAllByNameContaining(@Param("name") String name, Pageable pageable);

    List<Note> findAllByFile(String fileName);
}
