package com.artineer.artineer_renewal.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
            model.addAttribute("errorCode", 400);
            model.addAttribute("errorMessage", ex.getMessage());
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
