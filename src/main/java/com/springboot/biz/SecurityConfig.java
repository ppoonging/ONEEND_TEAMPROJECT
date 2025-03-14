package com.springboot.biz;

import com.springboot.biz.OAuth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // OAuth2 서비스 주입 (생성자 자동 주입)
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * 시큐리티 필터 체인
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 (API는 제외)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // API는 로그인 필요
                        .requestMatchers("/users/mypage").authenticated() // 마이페이지 로그인 필요
                        .anyRequest().permitAll() // 나머지 경로 모두 허용
                )
                // 기본 Form 로그인 설정
                .formLogin(form -> form
                        .loginPage("/users/login") // 로그인 페이지
                        .defaultSuccessUrl("/")    // 성공 시 메인으로
                        .permitAll()
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout")) // 로그아웃 URL
                        .logoutSuccessUrl("/") // 로그아웃 후 이동
                        .invalidateHttpSession(true) // 세션 무효화
                );

        return http.build();
    }


    /**
     * AuthenticationManager 빈 등록
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
