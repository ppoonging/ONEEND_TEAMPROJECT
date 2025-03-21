package com.springboot.biz;

import com.springboot.biz.OAuth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/static/**",
                        "/index.css",
                        "/bootstrap.min.css",
                        "/bootstrap.bundle.js",
                        "/delete.js"
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/ws-chat/**") // ✅ WebSocket 경로에 대해 CSRF 비활성화
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()  // API는 로그인 필요
                        .requestMatchers("/users/mypage").authenticated()  // 마이페이지 로그인 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 접근 가능
                        .requestMatchers("/user/**").hasRole("USER")  // 일반 사용자만 접근 가능
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/ws-chat/**").permitAll() // ✅ WebSocket 경로 허용
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // ✅ 동일 출처에서는 iframe 허용
                )

                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login")  // 로그인 폼의 action과 동일하게 설정
                        .defaultSuccessUrl("/", true)  // 로그인 성공 시 이동할 페이지
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/users/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)  // 커스텀 서비스 연결
                        )
                        .defaultSuccessUrl("/")  // 로그인 후 기본 페이지
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                        .logoutSuccessUrl("/")  // 로그아웃 후 기본 페이지
                        .invalidateHttpSession(true)  // 세션 무효화
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // 필요할 때만 세션 생성
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized Access\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {

            response.sendRedirect("/");
        };
    }
}
