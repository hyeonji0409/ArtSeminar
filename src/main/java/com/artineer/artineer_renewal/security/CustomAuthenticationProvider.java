package com.artineer.artineer_renewal.security;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@ComponentScan
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = (User) customUserDetailService.loadUserByUsername(username);

        try {
            // check pw
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Password is invalid");
            }

            // 계정 만료 확인
            if(!user.isAccountNonExpired()) {
                throw new AccountExpiredException("Account expired");
            } else if(!user.isAccountNonLocked()) {
                // 계정 잠금 여부
                throw new AccountLockedException("Account is locked");
            } else if (!user.isEnabled()) {
                // 계정 사용 여부
                throw new LockedException("Can't use account");
            } else if(!user.isCredentialsNonExpired()) {
                // 계정 비밀번호 만료 여부
                throw new CredentialsExpiredException("Credentials is expired");
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }catch (AccountLockedException e) {
            throw new RuntimeException(e);
        }

//         비밀번호 인코딩 알고리즘 최신화
        if (passwordEncoder.upgradeEncoding(user.getPassword())) {
            userService.updatePasswordEncoder(user, password);
        }

        System.out.println(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        
        // 인증 완료 후 객체 리턴
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
