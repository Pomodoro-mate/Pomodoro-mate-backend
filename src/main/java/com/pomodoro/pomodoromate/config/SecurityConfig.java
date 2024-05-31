package com.pomodoro.pomodoromate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.pomodoromate.auth.filters.JwtAuthenticationFilter;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.common.filters.HttpLoggingFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.addFilterBefore(httpLoggingFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public Filter httpLoggingFilter() {
        return new HttpLoggingFilter(objectMapper);
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/token"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/logout"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/logout"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
