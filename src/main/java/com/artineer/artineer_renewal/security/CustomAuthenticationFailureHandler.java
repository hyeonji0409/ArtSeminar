package com.artineer.artineer_renewal.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            // pw 틀림
//            아이디와 비번 중 틀린 것을 알리지 않음 유저에게
//            response.sendRedirect("/user/sign-in?error=invalid");
            response.sendRedirect("/user/sign-in?error=");
        } else if (exception instanceof UsernameNotFoundException) {
            // 계정 X
//            response.sendRedirect("/user/sign-in?error=account");
            response.sendRedirect("/user/sign-in?error=");
        } else if (exception instanceof LockedException) {
//            response.sendRedirect("/user/sign-in?error=lock");
            response.sendRedirect("/user/sign-in?error=");
        } else {
//            response.sendRedirect("/user/sign-in?error=invalid");
            response.sendRedirect("/user/sign-in?error=");
        }
    }
}
