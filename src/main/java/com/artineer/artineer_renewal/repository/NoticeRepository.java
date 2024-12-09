package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NoticeRepository extends JpaRepository<Notice, Long> {
//    @Query("select m from Notice m order by m.no desc")
//    List<Notice> findAllNotice();

    Page<Notice> findAll(Pageable pageable);
    List<Notice> findAllByTitleContaining(String title);

    List<Notice> findAllByFile(String fileName);

}
