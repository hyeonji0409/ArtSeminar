package com.artineer.artineer_renewal.dto;

import com.artineer.artineer_renewal.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class UserDto {

    private Long no;

    private String username;
    private String password;

    private String name;
    private String sex;
    private String birth;

    private String tel;
    private String email;
    private String addr;

    private String zipcode;
    private String roadAddress;
    private String detailAddress;


    private int year;
    private String role;
    private String regdate;
    private String ip;

    public User toEntity() {
        return User.builder()
                .no(no)
                .username(username)
                .password(password)
                .name(name)
                .sex(sex)
                .birth(birth)
                .tel(tel)
                .email(email)
                .zipcode(zipcode)
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .year(year)
                .role(role)
                .regdate(regdate)
                .ip(ip)
                .build();
    }

}
