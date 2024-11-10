package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Popup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {

    Page<Popup> findAll(Pageable pageable);
    List<Popup> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsVisible(LocalDateTime startDate, LocalDateTime endDate, boolean isVisible);
    Popup findByNo(Integer no);
}
