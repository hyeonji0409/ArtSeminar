package com.artineer.artineer_renewal.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.HttpStatus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        loggingWarn(request, ex);

        model.addAttribute("errorCode", 400);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model, HttpServletRequest request) {
        loggingWarn(request, ex);

        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    //NotFoundException.class,
    // todo 핸들러에 전달은 되었는데 로깅만 되고 페이지 반환은 안 되는
    // NoResourceFoundException와 Exception은 다른 루트 클래스를 상속한다?
    // ResponseStatus를 안하면 리스폰스 바디에 html 코드가 안 보이는 이유?

    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception ex, Model model, HttpServletRequest request) {
//        loggingWarn(request, ex);

        String requestURI = request.getRequestURI();
        String decodedURI = URLDecoder.decode(requestURI, StandardCharsets.UTF_8);

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "\"" +decodedURI+ "\" 에 대한 결과를 찾을 수 없습니다.\n" + ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public String handleInvalidDataAccessApiUsageException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "\n" + ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(Exception  ex, Model model, HttpServletRequest request) {
        loggingError(request, ex);

        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "\n작업을 수행하는 도중에 문제가 발생했습니다. : \n" + ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, Model model) {
        loggingError(request, ex);

        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "\n작업을 수행하는 도중에 문제가 발생했습니다. : \n" + request.getRequestURL());
        return "error/errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, HttpServletRequest request) {
        loggingError(request, ex);

        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "알 수 없는 문제가 발생했습니다. : " + ex.getClass());
        return "error/errorPage";
    }







    public void loggingError(HttpServletRequest request, Exception ex) {
        // 요청 URL, HTTP 메서드, IP 주소 등 주요 정보를 로깅
        log.error("Request URL: {}", request.getRequestURL());
        log.error("HTTP Method: {}", request.getMethod());
        log.error("Remote Address: {}", request.getRemoteAddr());
        log.error("User Agent: {}", request.getHeader("User-Agent"));

        // 모든 요청 파라미터를 로깅
        request.getParameterMap().forEach((key, values) -> {
            log.error("Parameter: {} = {}", key, String.join(", ", values));
        });

        log.error("Exception Stack Trace: ", ex);

        // 디버깅용
        ex.printStackTrace();
    }

    public void loggingWarn(HttpServletRequest request, Exception ex) {
        // 요청 URL, HTTP 메서드, IP 주소 등 주요 정보를 로깅
        log.warn("Request URL: {}", request.getRequestURL());
        log.warn("HTTP Method: {}", request.getMethod());
        log.warn("Remote Address: {}", request.getRemoteAddr());
        log.warn("User Agent: {}", request.getHeader("User-Agent"));

        // 모든 요청 파라미터를 로깅
        request.getParameterMap().forEach((key, values) -> {
            log.warn("Parameter: {} = {}", key, String.join(", ", values));
        });

        log.warn("Exception Stack Trace: ", ex);

        // 디버깅용
        ex.printStackTrace();
    }

//    public AccessDeniedHandler customAccessDeniedHandler() {
//        return (request, response, accessDeniedException) -> {
//            request.setAttribute("errorCode", 403);
//            request.setAttribute("errorMessage", "접근 권한이 없습니다.");
//            request.getRequestDispatcher("/error").forward(request, response);
//        };
//    }

}
