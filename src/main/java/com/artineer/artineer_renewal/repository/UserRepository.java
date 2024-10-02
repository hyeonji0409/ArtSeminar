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

//      todo 네이티브? 쿼리가 나을지도, 또는 스펙이나?
    Page<User> findByNameContainingAndSexStartingWithAndRoleContaining(String name, String sex, String role, Pageable pageable);
    Page<User> findByUsernameContainingAndSexStartingWithAndRoleContaining(String username, String sex, String role, Pageable pageable);
    Page<User> findByEmailContainingAndSexStartingWithAndRoleContaining(String email, String sex, String role, Pageable pageable);
    Page<User> findByTelContainingAndSexStartingWithAndRoleContaining(String tel, String sex, String role, Pageable pageable);
    Page<User> findByRoadAddressContainingAndSexStartingWithAndRoleContainingOrDetailAddressContainingAndSexStartingWithAndRoleContaining(String addr, String sex, String role, String addr2, String sex2, String role2, Pageable pageable);
}
