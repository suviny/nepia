package com.suvin.nepia.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author PARK SU BIN
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URL_MATCHERS = {
            "/",
            "/sign-up",
            "/find-password",
            "/api/v1/auth/**",
            "/api/v1/users",
            "/api/v1/users/existence/**",
            "/h2-console/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // HTTP 기본 인증 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);
        // 폼 로그인 방식 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        // CSRF 공격 방어 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        /* H2 콘솔 화면 깨짐 방지를 위한 X-Frame-Options 헤더 설정 : 동일 도메인 내 접근 허용 */
        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        /* 세션 관리 정책 설정 */
        http.sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/"));

        /* HTTP 요청 인가 정책 설정 */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URL_MATCHERS).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }
}