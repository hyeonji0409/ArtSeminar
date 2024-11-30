package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    Page<Reference> findAll(Pageable pageable);

}
