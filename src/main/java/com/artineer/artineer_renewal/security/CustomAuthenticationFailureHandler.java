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
            response.sendRedirect("/user/sign-in?error=invalid");
        } else if (exception instanceof UsernameNotFoundException) {
            // 계정 X
            response.sendRedirect("/user/sign-in?error=account");
        } else if (exception instanceof LockedException) {
            response.sendRedirect("/user/sign-in?error=lock");
        } else {
            response.sendRedirect("/user/sign-in?error=invalid");
        }
    }
}
