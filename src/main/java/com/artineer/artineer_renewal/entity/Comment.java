package com.artineer.artineer_renewal.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.List;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String bbsname; // 게시판 이름
    @Column(name = "bbs_no")
    private int bbsNo; // 게시글 번호

    @Nullable
    private Integer replys; // 대댓글 수..?
    private String memo;
    private String regdate;

    // 아이디, 이름, 기수
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private int year;

    // todo 대댓글도 낙수적으로 처리가 되야 함 캐스케이드 옵션 필요
    @OneToMany
    @JoinColumn(name = "replys", referencedColumnName = "no")
    private List<Comment> comments;

}
