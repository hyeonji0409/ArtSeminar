package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.ProjectDto;
import com.artineer.artineer_renewal.entity.*;
import com.artineer.artineer_renewal.repository.ProjectRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.IntegratedArticleService;
import com.artineer.artineer_renewal.service.ProjectService;
import com.artineer.artineer_renewal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IntegratedArticleService integratedArticleService;


    @GetMapping("/project")
    public String project(Model model,
                          @RequestParam(name = "qt", required = false, defaultValue = "subject") String queryType,
                          @RequestParam(name = "q", required = false, defaultValue = "") String query,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "6") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println("받아온 값" + queryType +"~" + query);

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "regdate"));

        Page<IntegratedArticle> pagination = integratedArticleService.findAllArticlesByQuery(Project.class, queryType, query, pageable);
        //        Page<Object> pagination = projectRepository.findAll(pageable);

        model.addAttribute("user", user);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pagination", pagination);
        model.addAttribute("queryType", queryType);
        model.addAttribute("query", query);
        return "board/project/project";
    }

    /* 글 작성 */
    @PostMapping("/project/new")
    public String createProject(@RequestParam String title, @RequestParam String story, @RequestParam("file") List<MultipartFile> file) {

        // 파일이 없으면 비어있는 리스트 처리
        if(file == null || file.isEmpty()) {
            file = new ArrayList<>();
        }

        projectService.createProject(title, story, file);
        return "redirect:/project";
    }

    /* 새 글 작성 */
    @GetMapping("/project/new")
    public String showNewProject(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("user", user);
        return "board/project/projectNew";
    }

    /* 글 세부 내용 조회 */
    @GetMapping("/project/{no}")
    public String showProjectDetail(@PathVariable("no") Long no, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        ProjectDto project = projectService.getProjectByNo(no);
        if (project == null)  throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
//        (new List<Integer>).
        //  System.out.println("WLWL" + project.getComments().size());

        // 조회수 증가
        projectService.increaseHitCount(no);

        model.addAttribute("bbsName", "project");
        model.addAttribute("bbsNo", no);
        model.addAttribute("project", project);

        return "board/project/projectDetail";
    }

    /* 글 수정 */
    @GetMapping("/project/edit/{no}")
    public String showEditProjectForm(@PathVariable Long no, Model model) {
        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);


        Project project = projectRepository.findById(no).orElse(null);
        if(!isAuthorizedUser(project, username)) throw new AccessDeniedException("수정권한이 없습니다.");

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        return "board/project/projectEdit";
    }

    @PostMapping("/project/edit")
    public String updateProject(@RequestParam Long no, @RequestParam String title, @RequestParam String story,@RequestParam("file") List<MultipartFile> file) {
        projectService.updateProject(no, title, story,file);
        return "redirect:/project/" + no;
    }

    /* 글 삭제 */
    @GetMapping("/project/delete/{no}")
    public String deleteProject(@PathVariable("no") Long no) {
        Project project = projectRepository.findById(no).orElse(null);

        // User 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(isAuthorizedUser(project, loggedInUsername)) {
            projectService.deleteProject(no);
            return "redirect:/project";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/pro/delete/{file}")
    public String deleteFile(@PathVariable String file, HttpServletRequest request) {

        projectService.deleteFiles(file);
        String referer = request.getHeader("Referer");

        // referer가 null이 아니면 해당 URL로 리다이렉트
        // referer가 null인 경우 기본 페이지로 리다이렉트 (예: project 목록)
        if (referer != null) return "redirect:" + referer;
        return "redirect:/project";
    }

    private boolean isAuthorizedUser(Project project, String loggedInUsername) {
        // 작성자나 관리자면 true
        return project.getUser().getUsername().equals(loggedInUsername) || userService.isAdmin(loggedInUsername);
    }
}
