package com.artineer.artineer_renewal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentDto {

    private Long no;
    private String bbsname;
    private int bbsNo;
    private int replys;
    private String memo;
    private String regdate;

    private String username;
    private String name;
    private int year;
}
