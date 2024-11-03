package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.CalendarEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    CalendarEvent findByNo(Integer No);
    List<CalendarEvent> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate);
    Page<CalendarEvent> findAllByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<CalendarEvent> findAllByDescriptionContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String description, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<CalendarEvent> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<CalendarEvent> findAllByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualOrDescriptionContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title, LocalDate startDate, LocalDate endDate, String description, LocalDate startDate2, LocalDate endDate2, Pageable pageable);
    Page<CalendarEvent> findAllByTitle(String title, Pageable pageable);
}
