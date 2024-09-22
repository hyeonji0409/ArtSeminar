package com.artineer.artineer_renewal.dto;

import jakarta.persistence.ElementCollection;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@ToString
public class UserSearchDTO {
    private Optional<String> query;
    private  Optional<String> queryValue;
    private  Optional<String> sex;
    private  Optional<String> minBirth;
    private  Optional<String> maxBirth;
    private  Optional<String> role;
    private  Optional<String> sort;
    private  Optional<String> order;
    private  Optional<Integer> page;
    private  Optional<Integer> pageSize;

    @Builder
    public UserSearchDTO(Optional<String> query, Optional<String> queryValue, Optional<String> sex, Optional<String> minBirth, Optional<String> maxBirth, Optional<String> role, Optional<String> sort, Optional<String> order) {
        this.query = query;
        this.queryValue = queryValue;
        this.sex = sex;
        this.minBirth = minBirth;
        this.maxBirth = maxBirth;
        this.role = role;
        this.sort = sort;
        this.order = order;
    }
}
