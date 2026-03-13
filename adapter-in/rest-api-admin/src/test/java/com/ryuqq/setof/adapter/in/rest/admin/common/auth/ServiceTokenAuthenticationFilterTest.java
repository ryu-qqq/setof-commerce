package com.ryuqq.setof.adapter.in.rest.admin.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ServiceTokenAuthenticationFilter 단위 테스트.
 *
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ServiceTokenAuthenticationFilter 단위 테스트")
class ServiceTokenAuthenticationFilterTest {

    private static final String SECRET = "test-secret-token";

    private ServiceTokenProperties properties;
    private ServiceTokenAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        properties = new ServiceTokenProperties();
        filter = new ServiceTokenAuthenticationFilter(properties);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("토큰 검증 비활성화 시")
    class WhenDisabled {

        @BeforeEach
        void setUp() {
            properties.setEnabled(false);
        }

        @Test
        @DisplayName("anonymous 서비스로 인증을 부여한다")
        void grantsAnonymousAccess() throws ServletException, IOException {
            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getPrincipal()).isEqualTo("anonymous");
            assertThat(auth.getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_SERVICE");
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("토큰 검증 활성화 시")
    class WhenEnabled {

        @BeforeEach
        void setUp() {
            properties.setEnabled(true);
            properties.setSecret(SECRET);
        }

        @Test
        @DisplayName("올바른 토큰과 서비스명이면 인증을 부여한다")
        void grantsAccessWithValidTokenAndName() throws ServletException, IOException {
            request.addHeader("X-Service-Token", SECRET);
            request.addHeader("X-Service-Name", "admin-frontend");

            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getPrincipal()).isEqualTo("admin-frontend");
            assertThat(auth.getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_SERVICE");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("올바른 토큰이지만 서비스명이 없으면 unknown으로 인증한다")
        void grantsAccessWithUnknownName() throws ServletException, IOException {
            request.addHeader("X-Service-Token", SECRET);

            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getPrincipal()).isEqualTo("unknown");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("토큰이 일치하지 않으면 인증을 부여하지 않는다")
        void doesNotGrantAccessWithInvalidToken() throws ServletException, IOException {
            request.addHeader("X-Service-Token", "wrong-token");
            request.addHeader("X-Service-Name", "admin-frontend");

            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNull();
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("토큰 헤더가 없으면 인증을 부여하지 않는다")
        void doesNotGrantAccessWithoutToken() throws ServletException, IOException {
            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNull();
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("서비스명이 빈 문자열이면 unknown으로 인증한다")
        void grantsAccessWithBlankServiceName() throws ServletException, IOException {
            request.addHeader("X-Service-Token", SECRET);
            request.addHeader("X-Service-Name", "   ");

            filter.doFilterInternal(request, response, filterChain);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getPrincipal()).isEqualTo("unknown");
            verify(filterChain).doFilter(request, response);
        }
    }
}
