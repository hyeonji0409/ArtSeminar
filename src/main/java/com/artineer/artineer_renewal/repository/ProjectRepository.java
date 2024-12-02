package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.Project;
import com.artineer.artineer_renewal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByUser(User user);

    Page<Project> findAll(Pageable pageable);

    List<Project> findAllByFile(String fileName);
}
