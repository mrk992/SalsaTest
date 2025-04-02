package com.salsa.test.salsa.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RateLimitFilterTest {

  private JwtService jwtService;
  private StringRedisTemplate redisTemplate;
  private ValueOperations<String, String> valueOperations;
  private FilterChain filterChain;
  private RateLimitFilter rateLimitFilter;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    jwtService = mock(JwtService.class);
    redisTemplate = mock(StringRedisTemplate.class);
    valueOperations = mock(ValueOperations.class);
    filterChain = mock(FilterChain.class);
    rateLimitFilter = new RateLimitFilter(redisTemplate, jwtService);
    response = new MockHttpServletResponse();

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  void shouldAllowRequestWithinLimit() throws ServletException, IOException {
    var request = requestWithAuthHeader("Bearer token");
    when(jwtService.validateAndGetSubject("token")).thenReturn("user123");
    when(valueOperations.increment("rate-limit:user123")).thenReturn(1L);

    rateLimitFilter.doFilterInternal(request, response, filterChain);

    assertEquals(200, response.getStatus());
    verify(redisTemplate).expire("rate-limit:user123", Duration.ofMinutes(1));
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldRejectRequestWhenLimitExceeded() throws ServletException, IOException {
    var request = requestWithAuthHeader("Bearer token");
    when(jwtService.validateAndGetSubject("token")).thenReturn("user123");
    when(valueOperations.increment("rate-limit:user123")).thenReturn(21L);

    rateLimitFilter.doFilterInternal(request, response, filterChain);

    assertEquals(429, response.getStatus());
    assertEquals("Rate limit exceeded", response.getContentAsString());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void shouldRejectInvalidToken() throws ServletException, IOException {
    var request = requestWithAuthHeader("Bearer invalid");
    when(jwtService.validateAndGetSubject("invalid")).thenThrow(new RuntimeException("Invalid token"));

    rateLimitFilter.doFilterInternal(request, response, filterChain);

    assertEquals(401, response.getStatus());
    assertEquals("Invalid or missing token", response.getContentAsString());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void shouldPassIfNoAuthHeader() throws ServletException, IOException {
    var request = new MockHttpServletRequest();

    rateLimitFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  private MockHttpServletRequest requestWithAuthHeader(String header) {
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", header);
    return request;
  }
}

