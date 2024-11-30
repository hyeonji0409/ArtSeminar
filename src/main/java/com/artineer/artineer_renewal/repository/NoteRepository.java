package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findAll(Pageable pageable);
}
