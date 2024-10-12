package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.Popup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {

    Page<Popup> findAll(Pageable pageable);
}
