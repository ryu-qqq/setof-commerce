package com.ryuqq.setof.adapter.in.rest.auth.repository;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.in.rest.auth.config.SecurityProperties;
import com.ryuqq.setof.adapter.in.rest.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@DisplayName("OAuth2CookieAuthorizationRequestRepository")
class OAuth2CookieAuthorizationRequestRepositoryTest {

    private OAuth2CookieAuthorizationRequestRepository repository;
    private SecurityProperties securityProperties;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        securityProperties = mock(SecurityProperties.class);
        SecurityProperties.OAuth2Properties oauth2Properties =
                mock(SecurityProperties.OAuth2Properties.class);
        when(securityProperties.getOauth2()).thenReturn(oauth2Properties);
        when(oauth2Properties.getCookieExpireSeconds()).thenReturn(180);

        repository = new OAuth2CookieAuthorizationRequestRepository(securityProperties);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Nested
    @DisplayName("loadAuthorizationRequest")
    class LoadAuthorizationRequestTest {

        @Test
        @DisplayName("쿠키가 없으면 null 반환")
        void shouldReturnNullWhenCookieNotFound() {
            // Given
            when(request.getCookies()).thenReturn(null);

            // When
            OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(request);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("saveAuthorizationRequest")
    class SaveAuthorizationRequestTest {

        @Test
        @DisplayName("authorizationRequest가 null이면 쿠키 삭제")
        void shouldDeleteCookiesWhenAuthorizationRequestIsNull() {
            // Given & When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.saveAuthorizationRequest(null, request, response);

                // Then
                cookieUtilsMock.verify(
                        () -> CookieUtils.deleteCookie(eq(response), any(String.class)), times(4));
            }
        }

        @Test
        @DisplayName("redirect_uri 파라미터가 있으면 쿠키에 저장")
        void shouldSaveRedirectUriCookieWhenParameterExists() {
            // Given
            OAuth2AuthorizationRequest authRequest =
                    OAuth2AuthorizationRequest.authorizationCode()
                            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                            .clientId("test-client-id")
                            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                            .build();

            when(request.getParameter(CookieUtils.REDIRECT_URI_COOKIE))
                    .thenReturn("http://localhost:3000/callback");
            when(request.getParameter(CookieUtils.REFERER_COOKIE)).thenReturn(null);
            when(request.getParameter(CookieUtils.INTEGRATION_COOKIE)).thenReturn(null);

            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.saveAuthorizationRequest(authRequest, request, response);

                // Then
                cookieUtilsMock.verify(
                        () ->
                                CookieUtils.addCookie(
                                        eq(response),
                                        eq(CookieUtils.REDIRECT_URI_COOKIE),
                                        eq("http://localhost:3000/callback"),
                                        eq(180)));
            }
        }

        @Test
        @DisplayName("referer 파라미터가 있으면 쿠키에 저장")
        void shouldSaveRefererCookieWhenParameterExists() {
            // Given
            OAuth2AuthorizationRequest authRequest =
                    OAuth2AuthorizationRequest.authorizationCode()
                            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                            .clientId("test-client-id")
                            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                            .build();

            when(request.getParameter(CookieUtils.REDIRECT_URI_COOKIE)).thenReturn(null);
            when(request.getParameter(CookieUtils.REFERER_COOKIE)).thenReturn("/products/123");
            when(request.getParameter(CookieUtils.INTEGRATION_COOKIE)).thenReturn(null);

            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.saveAuthorizationRequest(authRequest, request, response);

                // Then
                cookieUtilsMock.verify(
                        () ->
                                CookieUtils.addCookie(
                                        eq(response),
                                        eq(CookieUtils.REFERER_COOKIE),
                                        eq("/products/123"),
                                        eq(180)));
            }
        }

        @Test
        @DisplayName("integration 파라미터가 있으면 쿠키에 저장")
        void shouldSaveIntegrationCookieWhenParameterExists() {
            // Given
            OAuth2AuthorizationRequest authRequest =
                    OAuth2AuthorizationRequest.authorizationCode()
                            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                            .clientId("test-client-id")
                            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                            .build();

            when(request.getParameter(CookieUtils.REDIRECT_URI_COOKIE)).thenReturn(null);
            when(request.getParameter(CookieUtils.REFERER_COOKIE)).thenReturn(null);
            when(request.getParameter(CookieUtils.INTEGRATION_COOKIE)).thenReturn("true");

            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.saveAuthorizationRequest(authRequest, request, response);

                // Then
                cookieUtilsMock.verify(
                        () ->
                                CookieUtils.addCookie(
                                        eq(response),
                                        eq(CookieUtils.INTEGRATION_COOKIE),
                                        eq("true"),
                                        eq(180)));
            }
        }

        @Test
        @DisplayName("모든 파라미터가 있으면 모든 쿠키 저장")
        void shouldSaveAllCookiesWhenAllParametersExist() {
            // Given
            OAuth2AuthorizationRequest authRequest =
                    OAuth2AuthorizationRequest.authorizationCode()
                            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                            .clientId("test-client-id")
                            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                            .build();

            when(request.getParameter(CookieUtils.REDIRECT_URI_COOKIE))
                    .thenReturn("http://localhost:3000");
            when(request.getParameter(CookieUtils.REFERER_COOKIE)).thenReturn("/products");
            when(request.getParameter(CookieUtils.INTEGRATION_COOKIE)).thenReturn("true");

            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.saveAuthorizationRequest(authRequest, request, response);

                // Then - oauth2_auth_request + redirect_uri + referer + integration = 4 cookies
                cookieUtilsMock.verify(
                        () ->
                                CookieUtils.addCookie(
                                        eq(response),
                                        any(String.class),
                                        any(String.class),
                                        anyInt()),
                        times(4));
            }
        }
    }

    @Nested
    @DisplayName("removeAuthorizationRequest")
    class RemoveAuthorizationRequestTest {

        @Test
        @DisplayName("쿠키가 없으면 null 반환하고 삭제 안함")
        void shouldReturnNullAndNotDeleteWhenCookieNotFound() {
            // Given
            when(request.getCookies()).thenReturn(null);

            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                cookieUtilsMock
                        .when(() -> CookieUtils.getCookie(request, "oauth2_auth_request"))
                        .thenReturn(Optional.empty());

                OAuth2AuthorizationRequest result =
                        repository.removeAuthorizationRequest(request, response);

                // Then
                assertNull(result);
                cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(any(), any()), never());
            }
        }
    }

    @Nested
    @DisplayName("removeAuthorizationRequestCookies")
    class RemoveAuthorizationRequestCookiesTest {

        @Test
        @DisplayName("모든 OAuth2 쿠키 삭제")
        void shouldDeleteAllOAuth2Cookies() {
            // When
            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                repository.removeAuthorizationRequestCookies(response);

                // Then
                cookieUtilsMock.verify(
                        () -> CookieUtils.deleteCookie(response, "oauth2_auth_request"));
                cookieUtilsMock.verify(
                        () -> CookieUtils.deleteCookie(response, CookieUtils.REDIRECT_URI_COOKIE));
                cookieUtilsMock.verify(
                        () -> CookieUtils.deleteCookie(response, CookieUtils.REFERER_COOKIE));
                cookieUtilsMock.verify(
                        () -> CookieUtils.deleteCookie(response, CookieUtils.INTEGRATION_COOKIE));
            }
        }
    }
}
