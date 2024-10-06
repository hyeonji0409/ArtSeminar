package com.artineer.artineer_renewal.controller;

import com.artineer.artineer_renewal.dto.CalendarEventDTO;
import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.CalendarEvent;
import com.artineer.artineer_renewal.entity.Popup;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CalendarEventRepository;
import com.artineer.artineer_renewal.repository.PopupRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import com.artineer.artineer_renewal.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class adminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CalendarEventRepository calendarEventRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PopupRepository popupRepository;


    // 관리자의 사용자 정보 쿼리 화면
    @RequestMapping("/admin")
    public String adminPage(Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
                            @ModelAttribute UserSearchDTO userSearchDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println(userSearchDTO.toString());

        Page<User> users = adminService.paginationByQuery(userSearchDTO, page, pageSize);
        if (userSearchDTO.getPage().isEmpty()) { userSearchDTO.setPage(Optional.of(page==null?1:page)); }
        if (userSearchDTO.getPageSize().isEmpty()) { userSearchDTO.setPageSize(Optional.of(pageSize==null?10:pageSize)); }

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("userSearchDTO", userSearchDTO);

        return "/admin/admin";
    }



    // 회원정보 갱신
    @PostMapping("/user/update/check-{valueName}")
    @ResponseBody
    public ResponseEntity<String> checkSignUpValue(@PathVariable(name = "valueName") String valueName,
                                                   @RequestBody Map<String, String> payload) {

        return adminService.checkUserValue(valueName, payload);
    }





    // 관리자의 사용자 정보 쿼리 화면
    @RequestMapping("/admin/calendar")
    public String calendarManagePage(Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
                            @ModelAttribute CalendarEventDTO calendarEventDTO) {

        System.out.println(calendarEventDTO.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        System.out.println(calendarEventDTO.toString());

        Page<CalendarEvent> calenderEventList = adminService.calenderPaginationByQuery(calendarEventDTO, page, pageSize);
        System.out.println("일정을 출력" + calenderEventList.getTotalElements());

        Page<CalendarEvent> users = adminService.calenderPaginationByQuery(calendarEventDTO, page, pageSize);
        if (calendarEventDTO.getPage().isEmpty()) { calendarEventDTO.setPage(Optional.of(page==null?1:page)); }
        if (calendarEventDTO.getPageSize().isEmpty()) { calendarEventDTO.setPageSize(Optional.of(pageSize==null?10:pageSize)); }

        model.addAttribute("user", user);
        model.addAttribute("calenderEventList", calenderEventList);
        model.addAttribute("calendarEventDTO", calendarEventDTO);

        return "/admin/CalendarEventManage";
    }

    @PostMapping("/calendar/update")
    public String updateCalendar(Model model,
                             CalendarEventDTO calendarEventDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();
        boolean isSuccess = adminService.updateCalendarEvent(username, calendarEventDTO, clientIp);
        if (!isSuccess) {
            model.addAttribute("errorCode", 400);
            return "/user/errorPage";
        }

        String redirectAddress =  request.getHeader("Referer");
        System.out.println(redirectAddress);
        return "redirect:" + redirectAddress;
    }

    @PostMapping("admin/calendar/delete")
    public ResponseEntity<String> deleteCalendar(Model model,
                                                 @RequestBody Map<String, Object> payload) {

        System.out.println("일정을 삭제" + payload.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // IP 주소 가져오기
        String clientIp = request.getRemoteAddr();
        Integer no = Integer.parseInt((String) payload.get("no"));

        CalendarEvent calendarEvent = calendarEventRepository.findByNo(no);
        calendarEventRepository.delete(calendarEvent);

//        boolean isSuccess = adminService.updateCalendarEvent(username, calendarEventDTO, clientIp);
//        if (!isSuccess) {
//            model.addAttribute("errorCode", 400);
//            return "/user/errorPage";
//        }

        String redirectAddress =  request.getHeader("Referer");
        System.out.println(redirectAddress);
        return ResponseEntity.status(HttpStatus.OK).body("good");
    }


    @RequestMapping("/admin/popup")
    public String adPopupManage(Model model,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                     @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
                                     @ModelAttribute Popup popup) {

        System.out.println(popup.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Pageable pageable = PageRequest.of(0,99999999);

        Page<Popup> popupPageable = popupRepository.findAll(pageable);
        System.out.println("일정을 출력" + popupPageable.getTotalElements());


        model.addAttribute("user", user);
        model.addAttribute("popupPageable", popupPageable);

        return "/admin/popupManage";
    }
}
