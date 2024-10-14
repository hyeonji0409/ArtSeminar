package com.artineer.artineer_renewal.service;

import com.artineer.artineer_renewal.dto.CalendarEventDTO;
import com.artineer.artineer_renewal.dto.UserDto;
import com.artineer.artineer_renewal.dto.UserSearchDTO;
import com.artineer.artineer_renewal.entity.CalendarEvent;
import com.artineer.artineer_renewal.entity.Popup;
import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.CalendarEventRepository;
import com.artineer.artineer_renewal.repository.PopupRepository;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@Component
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CalendarEventRepository eventRepository;
    @Autowired
    private PopupRepository popupRepository;
    @Autowired
    private FileService fileService;


    // 회원정보 값 유효성 검사
    public ResponseEntity<String> checkUserValue(String valueName,
                                                 Map<String, String> payload) {

//        Todo 이하 2종목이 유니크하지 않을 떄 오류 발생(오류발생)
//        org.hibernate.NonUniqueResultException: Query did not return a unique result: 9 results were returned
        User foundUser = switch (valueName) {
//            아이디는 고정값임.
//            case "username" -> userRepository.findByUsername(payload.get("value"));
            case "email" -> userRepository.findByEmail(payload.get("value"));
            case "tel" -> userRepository.findByTel(payload.get("value"));
            default -> null;
        };

        System.out.println("sign-up check: "+ valueName);
        System.out.println(payload);
        System.out.println(
                (foundUser ==
                        null ?
                        "It is possible to register a new user...\n null" :
                        "submitted value is already exist in database...\n" + foundUser.toString()
                ) + "\n-------------------------------------------------\n"
        );

        // 검사를 요청한 정보로 찾은 foundUser 가 요청을 보낸 유저와 같을 경우, 중복허용
        if (foundUser != null && foundUser.equals(userRepository.findByUsername(payload.get("username")))) {
            foundUser = null;
        }


        if (foundUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("invalid");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("valid");
        }

    }


    public Page<User> paginationByQuery(UserSearchDTO userSearchDTO, Integer page, Integer pageSize) {
        String queryValue = userSearchDTO.getQueryValue().orElse("");
        String sex = userSearchDTO.getSex().orElse("");
        String role = userSearchDTO.getRole().orElse("");
        String sortProperty =  userSearchDTO.getSort().orElse("name");
        Sort.Direction direction = userSearchDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(
                userSearchDTO.getPage().orElse(page) - 1,
                userSearchDTO.getPageSize().orElse(pageSize),
                Sort.by(direction, sortProperty));

        Page<User> users =
                switch (userSearchDTO.getQuery().orElse("")) {
                    case "name" -> userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "username" -> userRepository.findByUsernameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "email" -> userRepository.findByEmailContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "tel" -> userRepository.findByTelContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                    case "address" -> userRepository.findByRoadAddressContainingAndSexStartingWithAndRoleContainingOrDetailAddressContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, queryValue, sex, role, pageable);
                    default -> userRepository.findByNameContainingAndSexStartingWithAndRoleContaining(queryValue, sex, role, pageable);
                };

        return users;
    }



    public Page<CalendarEvent> calenderPaginationByQuery(CalendarEventDTO calendarEventDTO, Integer page, Integer pageSize) {
        LocalDate startDate = null;
        if (calendarEventDTO.getStart().isEmpty()) {
            startDate = LocalDate.of(1000, 1, 1);  // MySQL의 최소 허용 날짜로 설정
        } else {
            startDate = calendarEventDTO.getStart()
                    .map(instant -> instant.atZone(ZoneId.systemDefault()).toLocalDate())
                    .orElse(LocalDate.of(1000, 1, 1)); // 만약 값이 없으면 기본 날짜로 설정
        }

        LocalDate endDate = null;
        if (calendarEventDTO.getStart().isEmpty()) {
            endDate = LocalDate.of(9999, 12, 31);  // MySQL의 최소 허용 날짜로 설정
        } else {
            endDate = calendarEventDTO.getEnd()
                    .map(instant -> instant.atZone(ZoneId.systemDefault()).toLocalDate())
                    .orElse(LocalDate.of(9999, 1, 1)); ;
        }

        String sortProperty =  calendarEventDTO.getSort().orElse("endDate");
        Sort.Direction direction = calendarEventDTO.getOrder().orElse("ASC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                calendarEventDTO.getPage().orElse(page) - 1,
                calendarEventDTO.getPageSize().orElse(pageSize),
                Sort.by(direction, sortProperty));

        String queryValue = calendarEventDTO.getQueryValue().orElse("");

        Page<CalendarEvent> calenderEventList =
                switch (calendarEventDTO.getQuery().orElse("")) {
                    case "title" -> eventRepository.findAllByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(queryValue, startDate, endDate, pageable);
                    case "description" -> eventRepository.findAllByDescriptionContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(queryValue, startDate, endDate, pageable);
                    default -> eventRepository.findAllByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualOrDescriptionContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(queryValue, startDate, endDate, queryValue, startDate, endDate, pageable);
                };


        return calenderEventList;
    }



    public boolean updateCalendarEvent(String logInUsername, CalendarEventDTO calendarEventDTO, String clientIp) {
        // IP 주소 가져오기
        System.out.println("Client IP: " + clientIp);

        if (!userService.isAdmin(logInUsername)) return false;


        System.out.println("수정요청 받음" +  calendarEventDTO.toString());


        CalendarEvent calendarEvent = eventRepository.findByNo(calendarEventDTO.getNo());
        System.out.println(calendarEventDTO.getNo()+ "가죠요요요" + calendarEvent);
        if (calendarEvent != null) {
            System.out.println("디비서 가져온 " + calendarEvent);

            // todo 예외처리
            if (calendarEventDTO.getTitle()!=null && !calendarEvent.getTitle().isEmpty()) calendarEvent.setTitle(calendarEventDTO.getTitle());
            if (calendarEventDTO.getDescription()!=null && !calendarEvent.getDescription().isEmpty()) calendarEvent.setDescription(calendarEventDTO.getDescription());
            if (calendarEventDTO.getStartDate()!=null ) calendarEvent.setStartDate(calendarEventDTO.getStartDate());
            if (calendarEventDTO.getStartTime()!=null ) calendarEvent.setStartTime(calendarEventDTO.getStartTime());
            if (calendarEventDTO.getEndDate()!=null) calendarEvent.setEndDate(calendarEventDTO.getEndDate());
            if (calendarEventDTO.getEndTime()!=null ) calendarEvent.setEndTime(calendarEventDTO.getEndTime());
            if (calendarEventDTO.getIsAllDay()!=null ) calendarEvent.setIsAllDay(calendarEventDTO.getIsAllDay());
            // todo utc 기준
            calendarEvent.setUpdatedAt(Instant.now());
        } else {
            calendarEvent = new CalendarEvent();
            calendarEvent.setTitle(calendarEventDTO.getTitle());
            calendarEvent.setDescription(calendarEventDTO.getDescription());
            calendarEvent.setStartDate(calendarEventDTO.getStartDate());
            calendarEvent.setStartTime(calendarEventDTO.getStartTime());
            calendarEvent.setEndDate(calendarEventDTO.getEndDate());
            calendarEvent.setEndTime(calendarEventDTO.getEndTime());
            calendarEvent.setIsAllDay(calendarEventDTO.getIsAllDay());
        }

        eventRepository.save(calendarEvent);

        return true;
    }


    public boolean updatePopup(String logInUsername, Popup popup, String clientIp) {
        // IP 주소 가져오기
//        System.out.println("Client IP: " + clientIp);

        if (!userService.isAdmin(logInUsername)) return false;


        System.out.println("수정요청 받음" +  popup.toString());


        Popup foundPopup = popupRepository.findByNo(popup.getNo());

        if (foundPopup != null) {
            System.out.println("디비서 가져온 " + foundPopup);

            // todo 예외처리
            if (popup.getTitle()!=null && !foundPopup.getTitle().isEmpty()) foundPopup.setTitle(popup.getTitle());
            if (popup.getDescription()!=null && !foundPopup.getDescription().isEmpty()) foundPopup.setDescription(popup.getDescription());
            if (popup.getStartDate()!=null ) foundPopup.setStartDate(popup.getStartDate());
            if (popup.getEndDate()!=null) foundPopup.setEndDate(popup.getEndDate());
            if (popup.getIsVisible()!=null ) foundPopup.setIsVisible(popup.getIsVisible());
            if (popup.getLink()!=null) {
                String oldLink = foundPopup.getLink();
                if (oldLink != null) fileService.deleteFile(oldLink);
                foundPopup.setLink(popup.getLink());
            }
        } else {
            foundPopup = new Popup();
            foundPopup.setTitle(popup.getTitle());
            foundPopup.setDescription(popup.getDescription());
            foundPopup.setStartDate(popup.getStartDate());
            foundPopup.setEndDate(popup.getEndDate());
            foundPopup.setIsVisible(popup.getIsVisible());
            foundPopup.setLink(popup.getLink());
            // todo 서버는 utc이고 클라 인풋폼은 localtime이라서 문제발생
            foundPopup.setCreatedAt(Instant.now());
        }

        popupRepository.save(foundPopup);

        return true;
    }






}
