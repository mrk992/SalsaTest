package com.salsa.test.salsa.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {

  private JwtService jwtService;
  private JwtAuthFilter jwtAuthFilter;
  private FilterChain filterChain;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    jwtService = mock(JwtService.class);
    jwtAuthFilter = new JwtAuthFilter(jwtService);
    filterChain = mock(FilterChain.class);
    response = new MockHttpServletResponse();
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldSkipIfNoAuthorizationHeader() throws ServletException, IOException {
    var request = new MockHttpServletRequest();

    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldSkipIfAuthorizationHeaderIsInvalid() throws ServletException, IOException {
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", "InvalidToken");

    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldAuthenticateIfTokenIsValid() throws Exception {
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer valid.jwt.token");

    when(jwtService.validateAndGetSubject("valid.jwt.token")).thenReturn("user123");
    when(jwtService.getRoleFromToken("valid.jwt.token")).thenReturn("ADMIN");

    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    var authentication = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(authentication);
    assertEquals("user123", authentication.getPrincipal());
    assertTrue(authentication.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldRespondUnauthorizedIfTokenIsInvalid() throws Exception {
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer invalid.token");

    when(jwtService.validateAndGetSubject("invalid.token"))
        .thenThrow(new RuntimeException("Invalid token"));

    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    assertEquals(401, response.getStatus());
    assertEquals("Invalid Token", response.getContentAsString());
    assertNull(SecurityContextHolder.getContext().getAuthentication());

    verify(filterChain, never()).doFilter(any(), any());
  }
}
