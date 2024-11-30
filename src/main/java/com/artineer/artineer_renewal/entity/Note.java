package com.artineer.artineer_renewal.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "note")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "subject")
    private String title;
    private String story;
    private int hit;
    private String file;
    private int comment; // 댓글 개수
    private String name;
    private String pw;

    @OneToMany(mappedBy = "bbsNo", cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = Comment.class)
//    @JoinColumn(name = "bbs_no", referencedColumnName = "no")
    private List<Comment> comments;

    private String regdate;
    private String ip;
}
