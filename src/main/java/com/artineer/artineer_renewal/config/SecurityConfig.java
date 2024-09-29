package com.artineer.artineer_renewal.config;


import com.artineer.artineer_renewal.security.CustomAuthenticationFailureHandler;
import com.artineer.artineer_renewal.security.CustomAuthenticationSuccessHandler;
import com.artineer.artineer_renewal.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashMap;
import java.util.Map;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    // MD5 방식으로 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(null, new MessageDigestPasswordEncoder("MD5"));
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        /* grades, signUp, gallery에 관해서는 user의 권한을 주지 않고 접속이 가능하게 한다. 그 외의 모든 요청은 인증을 필요로 함 */

                        auth.requestMatchers("/",
                                        "/css/**", "/static/assets/**", "/js/**", "/images/**", "/webjars/**", "/static/**").permitAll()
                                .requestMatchers("/notice/delete/**", "/notice/edit/**").authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()

                )
                .formLogin(form -> form
                        .loginPage("/user/sign-in")
                        .loginProcessingUrl("/authenticate")

                        .successHandler(customAuthenticationSuccessHandler) // login success
                        .failureHandler(customAuthenticationFailureHandler) // login failure
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 세션 무효화
                        .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/denied"));
        return http.build();
    }
}
