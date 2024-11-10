package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.GalleryDto;
import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CommentRepository;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.GalleryService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
//@AllArgsConstructor
public class GalleryController {
    @Value("${file.dir}")
    private String uploadDir;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/gallery")
    public String gallerys(Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(name = "size", required = false, defaultValue = "6") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<Gallery> pagination = galleryRepository.findAll(pageable);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
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
    public String showNewGallery(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/galleryNew_";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/gallery/{no}")
    public String showGalleryDetail(@PathVariable("no") Long no, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        GalleryDto gallery = galleryService.getGalleryByNo(no);

        if (gallery == null)
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");

        System.out.println("WLWL" + gallery.getComments().size());

        galleryService.increaseHitCount(no);

        model.addAttribute("bbsName", "gallery");
        model.addAttribute("bbsNo", no);
        model.addAttribute("gallery",gallery);
        return "board/galleryDetail";
    }

    /* 글 수정 */
   @GetMapping("/gallery/edit/{no}")
   public String showEditGalleryFrom(@PathVariable Long no, Model model){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
       User user = userRepository.findByUsername(username);

       Gallery gallery = galleryRepository.findById(no).orElse(null);
       if (!isAuthorizedUser(gallery, username)) throw new AccessDeniedException("수정권한이 없습니다.");

       model.addAttribute("user", user);
       model.addAttribute("gallery",gallery);
       return  "board/galleryEdit";
   }

    @PostMapping("/gallery/edit")
    public String updateGallery(@RequestParam Long no, @RequestParam String title,@RequestParam String story, Model model){

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
