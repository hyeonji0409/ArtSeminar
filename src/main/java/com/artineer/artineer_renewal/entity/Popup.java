package com.artineer.artineer_renewal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "popup", schema = "artineer_renewal")
public class Popup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false)
    private Integer no;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

//    @Column(name = "start_date", nullable = false)
//    private Instant startDate;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

//    @Column(name = "end_date", nullable = false)
//    private Instant endDate;

    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @ColumnDefault("1")
    @Column(name = "is_visible")
    private Boolean isVisible;

    @ColumnDefault("0")
    @Column(name = "priority")
    private Integer priority;

    @Column(name = "link", length = 500)
    private String link;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}