package com.artineer.artineer_renewal.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Setter
//@Entity
@Data
@ToString
@Table(name = "calendar_event", schema = "artineer_renewal")
public class CalendarEventDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false)
    private Integer no;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ColumnDefault("0")
    @Column(name = "is_all_day")
    private Boolean isAllDay;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    private Optional<LocalDateTime> start;
    private Optional<LocalDateTime> end;

    private Optional<String> query;
    private Optional<String> queryValue;
    private Optional<String> sort;
    private Optional<String> order;
    private Optional<Integer> page;
    private Optional<Integer> pageSize;

    public CalendarEventDTO(String title, String description, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Boolean isAllDay, Instant createdAt, Instant updatedAt, LocalDateTime  end, LocalDateTime  start, String query, String queryValue, String sort, String order, Integer page, Integer pageSize) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.isAllDay = isAllDay;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.start = Optional.ofNullable(start);
        this.end = Optional.ofNullable(end);
        this.query = Optional.ofNullable(query);
        this.queryValue = Optional.ofNullable(queryValue);
        this.sort = Optional.ofNullable(sort);
        this.order = Optional.ofNullable(order);
        this.page = Optional.ofNullable(page);
        this.pageSize = Optional.ofNullable(pageSize);
    }
}
