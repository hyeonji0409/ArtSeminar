package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Auditable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notice")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "subject")
    private String title;
    private String story;
    private int hit;
    private String file;
    private int comment; // 댓글 개수

    @OneToMany(mappedBy = "bbsNo", cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = Comment.class)
//    @JoinColumn(name = "bbs_no", referencedColumnName = "no")
    private List<Comment> comments = new ArrayList<>();

    private String regdate;
    private String ip;

    // 아이디
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private int year;


}
