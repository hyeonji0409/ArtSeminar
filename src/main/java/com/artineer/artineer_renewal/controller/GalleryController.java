package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.GalleryDto;
import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.service.GalleryService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
//@AllArgsConstructor
public class GalleryController {
    @Value("${file.dir}")
    private String uploadDir;

    @Autowired
    private final GalleryRepository galleryRepository;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private UserService userService;

    public GalleryController(GalleryRepository galleryRepository){this.galleryRepository = galleryRepository;}

    @GetMapping("/gallery")
    public String gallerys(Model model) {
        List<Gallery> galleryList = galleryRepository.findAllGallery();
        model.addAttribute("gallerys", galleryList);
        return "board/gallery";
    }

    /* 글 작성 */
    @PostMapping("/gallery/new")
    public String createGallery(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file)
    {
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }
        galleryService.createGallery(title, story, file);
        return "redirect:/gallery";
    }

    /* 새 글 작성 */
    @GetMapping("/gallery/new")
    public String showNewGallery() {
        return "board/galleryNew_";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/gallery/{no}")
    public String showGalleryDetail(@PathVariable("no") Long no, Model model){
        galleryService.increaseHitCount(no);

        GalleryDto gallery = galleryService.getGalleryByNo(no);

        if (gallery == null) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }
        model.addAttribute("gallery",gallery);
        return "board/galleryDetail";
    }

    /* 글 수정 */
    //현재 오류 떠서 안들어가짐
   @GetMapping("/gallery/edit/{no}")
   public String showEditGalleryFrom(@PathVariable Long no, Model model){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String loggedInUsername = authentication.getName();

       Gallery gallery = galleryRepository.findById(no).orElse(null);

       if(isAuthorizedUser(gallery, loggedInUsername)){
           model.addAttribute("gallery",gallery);
           return  "board/galleryEdit";
       }
       else {
           return "redirect:/access-denied";
       }
   }

    @PostMapping("/gallery/edit")
    public String updateGallery(@RequestParam Long no, @RequestParam String title,@RequestParam String story){
       galleryService.updateGallery(no,title, story);
       return "redirect:/gallery/"+no;
    }

    /* 글 삭제 */
    //오류 마찬가지
    @GetMapping("/gallery/delete/{no}")
    public String deleteGallery(@PathVariable("no") Long no) {
        Gallery gallery = galleryRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(gallery, loggedInUsername)) {
            galleryService.deleteGallery(no);
            return "redirect:/gallery";
        } else {
            return "redirect:/access-denied";
        }

    }

    private boolean isAuthorizedUser(Gallery gallery, String loggedInUsername) {
        // 작성자나 관리자면 true
        return gallery.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
