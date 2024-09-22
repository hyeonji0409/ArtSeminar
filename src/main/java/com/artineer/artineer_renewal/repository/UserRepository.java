package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
    public User findByPassword(String password);
    public User findByEmail(String email);
    public User findByTel(String telephoneNumber);

    Page<User> findAll(Pageable pageable);

//    Page<User> findAllByNameContainingAndSexContainingAndRoleContaining(String name, String sex, String role, Pageable pageable);
    Page<User> findByNameContainingAndSexStartingWith(String name, String sex, Pageable pageable);
    Page<User> findByUsernameContainingAndSexStartingWith(String username, String sex, Pageable pageable);
    Page<User> findByEmailContainingAndSexStartingWith(String email, String sex, Pageable pageable);
    Page<User> findByTelContainingAndSexStartingWith(String tel, String sex, Pageable pageable);
}
