package com.salsa.test.salsa.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(2)
public class RateLimitFilter extends OncePerRequestFilter {

  private static final int MAX_REQUESTS_PER_MINUTE = 20;
  private final StringRedisTemplate redisTemplate;
  private final JwtService jwtService;

  public RateLimitFilter(StringRedisTemplate redisTemplate, JwtService jwtService) {
    this.redisTemplate = redisTemplate;
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      try {
        String userId = jwtService.validateAndGetSubject(token);
        String key = "rate-limit:" + userId;

        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount == 1) {
          redisTemplate.expire(key, Duration.ofMinutes(1));
        }

        if (currentCount != null && currentCount > MAX_REQUESTS_PER_MINUTE) {
          response.setStatus(429);
          response.getWriter().write("Rate limit exceeded");
          return;
        }

      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or missing token");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
