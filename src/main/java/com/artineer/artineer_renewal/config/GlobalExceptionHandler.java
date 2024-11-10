package com.artineer.artineer_renewal.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorCode", 400);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public String handleNotFoundException(NotFoundException ex, Model model, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String decodedURI = URLDecoder.decode(requestURI, StandardCharsets.UTF_8);

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "\"" +decodedURI+ "\" 에 대한 결과를 찾을 수 없습니다.\n" + ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleNoResourceFoundException(Exception  ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "\n작업을 수행하는 도중에 문제가 발생했습니다.\n" + ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
            model.addAttribute("errorCode", 500);
            model.addAttribute("errorMessage", "알 수 없는 문제가 발생했습니다." + ex.getClass());
            return "error/errorPage";
    }

//    public AccessDeniedHandler customAccessDeniedHandler() {
//        return (request, response, accessDeniedException) -> {
//            request.setAttribute("errorCode", 403);
//            request.setAttribute("errorMessage", "접근 권한이 없습니다.");
//            request.getRequestDispatcher("/error").forward(request, response);
//        };
//    }

}