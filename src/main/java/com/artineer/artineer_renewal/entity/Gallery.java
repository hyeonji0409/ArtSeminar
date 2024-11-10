package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.ErrorResponse;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "gallery")
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "subject")
    private String title;
    private String story;
    private int hit;
    private String file;
    private int comment;

    @OneToMany
    @JoinColumn(name = "bbs_no", referencedColumnName = "no")
    private List<Comment> comments; // 댓글 개수

    private String regdate;
    private String ip;

    // User 정보
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private int year;


}
