package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


// 각 게사판 테이블의 슈퍼 타입이다.
// 네이티브 쿼리를 직접 요청해서 연관매핑이 안된다.?
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class IntegratedArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "subject")
    private String title;
    private String story;
    private int hit;
    private String file;
    private int comment; // 댓글 개수

    @OneToMany
    @JoinColumn(name = "bbs_no", referencedColumnName = "no")
    private List<Comment> comments; // 댓글 개수

    private String regdate;
    private String ip;

    // 아이디
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private int year;



}
