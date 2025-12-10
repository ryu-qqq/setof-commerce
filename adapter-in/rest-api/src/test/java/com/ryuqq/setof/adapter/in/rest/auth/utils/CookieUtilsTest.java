package com.ryuqq.setof.adapter.in.rest.auth.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("CookieUtils")
class CookieUtilsTest {

    @Nested
    @DisplayName("getAccessToken")
    class GetAccessTokenTest {

        @Test
        @DisplayName("Access Token 쿠키가 있으면 값 반환")
        void shouldReturnAccessTokenWhenCookieExists() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie accessTokenCookie =
                    new Cookie(CookieUtils.ACCESS_TOKEN_COOKIE, "test_access_token");
            when(request.getCookies()).thenReturn(new Cookie[] {accessTokenCookie});

            // When
            Optional<String> result = CookieUtils.getAccessToken(request);

            // Then
            assertTrue(result.isPresent());
            assertEquals("test_access_token", result.get());
        }

        @Test
        @DisplayName("쿠키가 없으면 빈 Optional 반환")
        void shouldReturnEmptyWhenNoCookies() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(null);

            // When
            Optional<String> result = CookieUtils.getAccessToken(request);

            // Then
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Access Token 쿠키가 없으면 빈 Optional 반환")
        void shouldReturnEmptyWhenAccessTokenCookieNotFound() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie otherCookie = new Cookie("other", "value");
            when(request.getCookies()).thenReturn(new Cookie[] {otherCookie});

            // When
            Optional<String> result = CookieUtils.getAccessToken(request);

            // Then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getRefreshToken")
    class GetRefreshTokenTest {

        @Test
        @DisplayName("Refresh Token 쿠키가 있으면 값 반환")
        void shouldReturnRefreshTokenWhenCookieExists() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie refreshTokenCookie =
                    new Cookie(CookieUtils.REFRESH_TOKEN_COOKIE, "test_refresh_token");
            when(request.getCookies()).thenReturn(new Cookie[] {refreshTokenCookie});

            // When
            Optional<String> result = CookieUtils.getRefreshToken(request);

            // Then
            assertTrue(result.isPresent());
            assertEquals("test_refresh_token", result.get());
        }
    }

    @Nested
    @DisplayName("getCookie")
    class GetCookieTest {

        @Test
        @DisplayName("쿠키가 있으면 Cookie 객체 반환")
        void shouldReturnCookieWhenExists() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie cookie = new Cookie("test_cookie", "test_value");
            when(request.getCookies()).thenReturn(new Cookie[] {cookie});

            // When
            Optional<Cookie> result = CookieUtils.getCookie(request, "test_cookie");

            // Then
            assertTrue(result.isPresent());
            assertEquals("test_cookie", result.get().getName());
            assertEquals("test_value", result.get().getValue());
        }

        @Test
        @DisplayName("쿠키가 없으면 빈 Optional 반환")
        void shouldReturnEmptyWhenCookieNotFound() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(null);

            // When
            Optional<Cookie> result = CookieUtils.getCookie(request, "test_cookie");

            // Then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("addCookie")
    class AddCookieTest {

        @Test
        @DisplayName("쿠키 추가 성공")
        void shouldAddCookie() {
            // Given
            HttpServletResponse response = mock(HttpServletResponse.class);
            ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

            // When
            CookieUtils.addCookie(response, "test_name", "test_value", 3600);

            // Then
            verify(response).addCookie(cookieCaptor.capture());
            Cookie capturedCookie = cookieCaptor.getValue();
            assertEquals("test_name", capturedCookie.getName());
            assertEquals("test_value", capturedCookie.getValue());
            assertEquals("/", capturedCookie.getPath());
            assertTrue(capturedCookie.isHttpOnly());
            assertEquals(3600, capturedCookie.getMaxAge());
        }
    }

    @Nested
    @DisplayName("deleteCookie")
    class DeleteCookieTest {

        @Test
        @DisplayName("쿠키 삭제 성공")
        void shouldDeleteCookie() {
            // Given
            HttpServletResponse response = mock(HttpServletResponse.class);
            ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

            // When
            CookieUtils.deleteCookie(response, "test_name");

            // Then
            verify(response).addCookie(cookieCaptor.capture());
            Cookie capturedCookie = cookieCaptor.getValue();
            assertEquals("test_name", capturedCookie.getName());
            assertEquals("", capturedCookie.getValue());
            assertEquals("/", capturedCookie.getPath());
            assertEquals(0, capturedCookie.getMaxAge());
        }
    }

    @Nested
    @DisplayName("getRedirectUri")
    class GetRedirectUriTest {

        @Test
        @DisplayName("Redirect URI 쿠키가 있으면 값 반환")
        void shouldReturnRedirectUriWhenCookieExists() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie redirectUriCookie =
                    new Cookie(CookieUtils.REDIRECT_URI_COOKIE, "http://localhost:3000/callback");
            when(request.getCookies()).thenReturn(new Cookie[] {redirectUriCookie});

            // When
            Optional<String> result = CookieUtils.getRedirectUri(request);

            // Then
            assertTrue(result.isPresent());
            assertEquals("http://localhost:3000/callback", result.get());
        }

        @Test
        @DisplayName("Redirect URI 쿠키가 없으면 빈 Optional 반환")
        void shouldReturnEmptyWhenRedirectUriCookieNotFound() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(null);

            // When
            Optional<String> result = CookieUtils.getRedirectUri(request);

            // Then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getReferer")
    class GetRefererTest {

        @Test
        @DisplayName("Referer 쿠키가 있으면 값 반환")
        void shouldReturnRefererWhenCookieExists() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie refererCookie = new Cookie(CookieUtils.REFERER_COOKIE, "/products/123");
            when(request.getCookies()).thenReturn(new Cookie[] {refererCookie});

            // When
            Optional<String> result = CookieUtils.getReferer(request);

            // Then
            assertTrue(result.isPresent());
            assertEquals("/products/123", result.get());
        }
    }

    @Nested
    @DisplayName("isIntegrationRequest")
    class IsIntegrationRequestTest {

        @Test
        @DisplayName("Integration 쿠키가 'true'면 true 반환")
        void shouldReturnTrueWhenIntegrationCookieIsTrue() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie integrationCookie = new Cookie(CookieUtils.INTEGRATION_COOKIE, "true");
            when(request.getCookies()).thenReturn(new Cookie[] {integrationCookie});

            // When
            boolean result = CookieUtils.isIntegrationRequest(request);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Integration 쿠키가 'false'면 false 반환")
        void shouldReturnFalseWhenIntegrationCookieIsFalse() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie integrationCookie = new Cookie(CookieUtils.INTEGRATION_COOKIE, "false");
            when(request.getCookies()).thenReturn(new Cookie[] {integrationCookie});

            // When
            boolean result = CookieUtils.isIntegrationRequest(request);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Integration 쿠키가 없으면 false 반환")
        void shouldReturnFalseWhenIntegrationCookieNotFound() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(null);

            // When
            boolean result = CookieUtils.isIntegrationRequest(request);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Integration 쿠키 값이 다른 값이면 false 반환")
        void shouldReturnFalseWhenIntegrationCookieHasOtherValue() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie integrationCookie = new Cookie(CookieUtils.INTEGRATION_COOKIE, "yes");
            when(request.getCookies()).thenReturn(new Cookie[] {integrationCookie});

            // When
            boolean result = CookieUtils.isIntegrationRequest(request);

            // Then
            assertFalse(result);
        }
    }
}
