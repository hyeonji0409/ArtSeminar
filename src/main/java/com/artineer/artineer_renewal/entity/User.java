package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false, name = "user_id")
    private String username;
    @Column(nullable = false, name = "pw")
    private String password;

    private String name;
    private String sex;
    private String birth;
    @Column(unique = true)
    private String tel;
    @Column(unique = true)
    private String email;
    private String addr;

    // 신규회원 주소 입력
    private String zipcode;
    @Column(name = "road_address")
    private String roadAddress;
    @Column(name = "detail_address")
    private String detailAddress;


    private int year;
    @Column(name = "level")
    private String role;
    private String regdate;
    private String ip;



    @Builder
    public User(Long no, String username, String password, String name, String sex, String birth,
                      String tel, String email, String zipcode, String roadAddress, String detailAddress, int year, String role, String regdate, String ip) {
        this.no = no;
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.tel = tel;
        this.email = email;

        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;

        this.year = year;
        this.role = role;
        this.regdate = regdate;
        this.ip = ip;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> { return "ROLE_USER"; });
        authorities.add(this.getRole() == null ? new SimpleGrantedAuthority("ROLE_GUEST") : this::getRole);
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
