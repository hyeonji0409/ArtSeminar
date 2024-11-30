package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.NoteDto;
import com.artineer.artineer_renewal.dto.NoticeDto;
import com.artineer.artineer_renewal.entity.IntegratedArticle;
import com.artineer.artineer_renewal.entity.Note;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.NoteRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.NoteService;
import com.artineer.artineer_renewal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteService noteService;
    @Autowired
    private IntegratedArticleService integratedArticleService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/note")
    public String notis(Model model,
                          @RequestParam(name = "qt", required = false, defaultValue = "subject") String queryType,
                          @RequestParam(name = "q", required = false, defaultValue = "") String query,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Note.class, queryType, query, pageable);
        //        Page<Object> pagination = noticeRepository.findAll(pageable);


        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/note/note";
    }

    /* 글 작성 */
    @PostMapping("/note/new")
    public String createNote(@RequestParam String pw,@RequestParam String name, @RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        noteService.createNote(pw, name,title, story, file);
        return "redirect:/note";
    }

    /* 새 글 작성 */
    @GetMapping("/note/new")
    public String showNewNote() {

        return "board/note/noteNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/note/{no}")
    public String showNoteDetail(@PathVariable("no") Long no, Model model) {


        NoteDto note = noteService.getNoteByNo(no);
        if (note == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");

        // 조회수 증가
        noteService.increaseHitCount(no);

        model.addAttribute("bbsName", "note");
        model.addAttribute("bbsNo", no);
        model.addAttribute("notice", note);

        return "board/note/noteDetail";
    }

    /* 글 수정 */
    @GetMapping("/note/edit/{no}")
    public String showEditNoteForm(@PathVariable Long no, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Note note = noteRepository.findById(no).orElse(null);

//        pw를 통해 게시판 수정 가능하도록
        if(!isAuthorizedUser(username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("note", note);
        return "board/note/noteEdit";
    }

    @PostMapping("/note/edit")
    public String updateNote(@RequestParam Long no, @RequestParam String title, @RequestParam String story) {
        noteService.updateNote(no, title, story);
        return "redirect:/note/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/note/delete/{no}")
    public String deleteNote(@PathVariable("no") Long no) {
        Note note = noteRepository.findById(no).orElse(null);
// User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        //이것도 pw에 맞게 작성하면 삭제 가능
        if(isAuthorizedUser(loggedInUsername)) {
            noteService.deleteNote(no);
            return "redirect:/note";
        } else {
            return "redirect:/access-denied";
        }
    }

    private boolean isAuthorizedUser(String loggedInUsername) {
        // 관리자면 true
        return userService.isAdmin(loggedInUsername);
    }

}
