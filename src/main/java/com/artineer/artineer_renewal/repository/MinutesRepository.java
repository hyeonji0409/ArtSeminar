package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Minutes;
import com.artineer.artineer_renewal.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MinutesRepository extends JpaRepository<Minutes, Long> {

    Page<Minutes> findAll(Pageable pageable);

    List<Minutes> findAllByFile(String fileName);
}
