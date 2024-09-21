package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class GalleryController {

    private final GalleryRepository galleryRepository;


    @GetMapping("/gallery")
    public String gallery(Model model) {
        List<Gallery> galleryList = galleryRepository.findAllGallery();
        model.addAttribute("gallerys", galleryList);
        return "board/gallery";
    }

    @GetMapping("/gallery/new")
    public String showNewGallery() {
        return "board/galleryNew";
    }
}
