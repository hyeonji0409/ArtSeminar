package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.Greeting;
import com.artineer.artineer_renewal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    List<Greeting> findAllByUser(User user);

    Page<Greeting> findAll(Pageable pageable);

    List<Greeting> findAllByFile(String fileName);
}
