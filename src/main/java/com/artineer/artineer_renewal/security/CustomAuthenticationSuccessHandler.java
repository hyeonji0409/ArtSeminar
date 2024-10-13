package com.artineer.artineer_renewal.security;

import com.artineer.artineer_renewal.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

//        // 회원정보 최신화 요구 (db 콘셉에 의한)
//        User user = (User) authentication.getPrincipal();
//        if ( user.getRoadAddress().isBlank() || user.getDetailAddress().isBlank() ) {
//
//        }


        // 세션에서 저장된 SavedRequest 가져오기
        SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        // 만약 SavedRequest가 있으면, 원래 요청한 URL로 리다이렉트
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();  // 원래 요청한 URL
            redirectStrategy.sendRedirect(request, response, targetUrl);  // 해당 URL로 리다이렉트
        } else {
            // 만약 이전 URL 정보가 없다면 기본 페이지로 리다이렉트
            redirectStrategy.sendRedirect(request, response, "/");
        }

//        response.sendRedirect( prevPage!=null ? prevPage : "/");
    }
}
