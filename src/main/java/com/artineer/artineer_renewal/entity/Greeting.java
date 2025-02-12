package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name = "hello")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Greeting {
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
    @Where(clause = "bbsname = 'greeting'")
//    @JoinColumn(name = "bbs_no", referencedColumnName = "no")
    private List<Comment> comments;

    private String regdate;
    private String ip;

    // 아이디
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private int year;
}
