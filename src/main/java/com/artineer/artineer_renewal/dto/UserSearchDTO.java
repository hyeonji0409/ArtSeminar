package com.artineer.artineer_renewal.dto;

import jakarta.persistence.ElementCollection;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@ToString
public class UserSearchDTO {
    private Optional<String> query;
    private Optional<String> queryValue;
    private Optional<String> sex;
    private Optional<String> minBirth;
    private Optional<String> maxBirth;
    private Optional<String> role;
    private Optional<String> sort;
    private Optional<String> order;
    private Optional<Integer> page;
    private Optional<Integer> pageSize;
    private Optional<Timestamp> deletedAt;


    @Builder
    public UserSearchDTO(String query, String queryValue, String sex, String minBirth, String maxBirth, String role, String sort, String order, Integer page, Integer pageSize, Timestamp deletedAt) {
        this.query = Optional.ofNullable(query);
        this.queryValue = Optional.ofNullable(queryValue);
        this.sex = Optional.ofNullable(sex);
        this.minBirth = Optional.ofNullable(minBirth);
        this.maxBirth = Optional.ofNullable(maxBirth);
        this.role = Optional.ofNullable(role);
        this.sort = Optional.ofNullable(sort);
        this.order = Optional.ofNullable(order);
        this.page = Optional.ofNullable(page);
        this.pageSize = Optional.ofNullable(pageSize);
        this.deletedAt = Optional.ofNullable(deletedAt);
    }
}
