package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
//    @Query("select m from Gallery m order by m.no desc")
//    List<Gallery> findAllGallery();
    List<Gallery> findAllByUser(User user);

    Page<Gallery> findAll(Pageable pageable);
    List<Gallery> findAllByTitleContaining(String title);
}
