package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    @Query("select m from Gallery m order by m.no desc")
    List<Gallery> findAllGallery();
    List<Gallery> findAllByUser(User user);
}
