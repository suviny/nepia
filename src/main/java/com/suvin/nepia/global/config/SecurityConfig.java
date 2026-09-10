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
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URL_PATTERNS = {
            "/",
            "/api/v1/auth/**",
            "/sign-up",
            "/api/v1/users",
            "/api/v1/users/exists-email",
            "/api/v1/users/exists-nickname",
            "/h2-console/**"
    };

    /**
     * 비밀번호 암호화를 위한 {@link BCryptPasswordEncoder} 스프링 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 스프링 시큐리티의 필터 체인에 정적 리소스가 적용되지 않도록 설정한다.
     */
    @Bean
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 스프링 시큐리티의 보안 작업을 수행하기 위한 필터 체인을 구성한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* HTTP 기본 인증 방식 비활성화 */
        http.httpBasic(AbstractHttpConfigurer::disable);
        /* 폼 로그인 방식 비활성화 */
        http.formLogin(AbstractHttpConfigurer::disable);
        /* CSRF 공격 방어 임시 비활성화 */
        http.csrf(AbstractHttpConfigurer::disable);

        /* X-Frame-Options 헤더 설정 : H2 콘솔의 정상적인 동작을 위한 동일한 도메인 내 iframe 로드 허용  */
        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        /* 세션 관리 정책 설정 */
        http.sessionManagement(session -> session
                // 사용자당 허용될 최대 세션 수
                .maximumSessions(1)
                // true: 새로운 로그인 차단, false: 기존 세션 만료(기본값)
                .maxSessionsPreventsLogin(false)
                // 세션 만료시 이동할 URL
                .expiredUrl("/"));

        /* HTTP 요청 인가 정책 설정 */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URL_PATTERNS).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    /**
     * 세션 이벤트(생성, 변경, 삭제 등)을 스프링 애플리케이션 컨텍스트가 감지할 수 있도록 {@link HttpSessionEventPublisher}를 스프링 빈 등록한다.
     *
     * @return  서블릿 컨테이너에서 발생하는 세션 이벤트를 변환해서 스프링 시큐리티로 전달해주는 {@link HttpSessionEventPublisher} 반환
     */
    @Bean
    public HttpSessionEventPublisher sessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}