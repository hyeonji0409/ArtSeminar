package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("select m from Notice m order by m.no desc")
    List<Notice> findAllNotice();

}
