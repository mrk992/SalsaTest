package com.salsa.test.salsa.config;

import com.salsa.test.salsa.infra.security.JwtAuthFilter;
import com.salsa.test.salsa.infra.security.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
                                         JwtAuthFilter jwtAuthFilter,
                                         RateLimitFilter rateLimitFilter) throws Exception {

    return http
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/job-offers").hasRole("EMPLOYER")
            .requestMatchers(HttpMethod.DELETE, "/api/job-offers/**").hasRole("EMPLOYER")
            .requestMatchers("/api/job-offers/**").authenticated()
            .anyRequest().denyAll()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(rateLimitFilter, JwtAuthFilter.class)
        .build();
  }
}