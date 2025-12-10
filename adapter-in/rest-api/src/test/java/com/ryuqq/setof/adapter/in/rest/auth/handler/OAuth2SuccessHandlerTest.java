package com.ryuqq.setof.adapter.in.rest.auth.handler;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.config.SecurityProperties;
import com.ryuqq.setof.adapter.in.rest.auth.mapper.KakaoOAuth2ApiMapper;
import com.ryuqq.setof.adapter.in.rest.auth.repository.OAuth2CookieAuthorizationRequestRepository;
import com.ryuqq.setof.adapter.in.rest.auth.utils.CookieUtils;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.port.in.command.KakaoOAuthLoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuth2SuccessHandler")
class OAuth2SuccessHandlerTest {

    @Mock private KakaoOAuthLoginUseCase kakaoOAuthLoginUseCase;

    @Mock private KakaoOAuth2ApiMapper kakaoOAuth2ApiMapper;

    @Mock private TokenCookieWriter tokenCookieWriter;

    @Mock private OAuth2CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Mock private SecurityProperties securityProperties;

    @Mock private SecurityProperties.OAuth2Properties oauth2Properties;

    @Mock private HttpServletRequest request;

    @Mock private HttpServletResponse response;

    @Mock private OAuth2AuthenticationToken authentication;

    @Mock private OAuth2User oAuth2User;

    private OAuth2SuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        // lenient stubbing: 일부 테스트에서 사용하지 않는 stubbing
        lenient().when(securityProperties.getOauth2()).thenReturn(oauth2Properties);
        lenient().when(oauth2Properties.getFrontDomainUrl()).thenReturn("http://localhost:3000");

        successHandler =
                new OAuth2SuccessHandler(
                        kakaoOAuthLoginUseCase,
                        kakaoOAuth2ApiMapper,
                        tokenCookieWriter,
                        authorizationRequestRepository,
                        securityProperties);
    }

    @Nested
    @DisplayName("onAuthenticationSuccess")
    class OnAuthenticationSuccessTest {

        @Test
        @DisplayName("인증 성공 시 토큰 쿠키 설정 및 리다이렉트")
        void shouldSetTokenCookiesAndRedirect() throws IOException {
            // Given
            when(authentication.getPrincipal()).thenReturn(oAuth2User);

            KakaoOAuthCommand command = mock(KakaoOAuthCommand.class);
            when(kakaoOAuth2ApiMapper.toKakaoOAuthCommand(oAuth2User, false)).thenReturn(command);

            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            KakaoOAuthResponse oauthResponse =
                    KakaoOAuthResponse.existingKakaoMember("member-id", tokens);
            when(kakaoOAuthLoginUseCase.execute(command)).thenReturn(oauthResponse);

            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                cookieUtilsMock
                        .when(() -> CookieUtils.isIntegrationRequest(request))
                        .thenReturn(false);
                cookieUtilsMock
                        .when(() -> CookieUtils.getRedirectUri(request))
                        .thenReturn(Optional.empty());
                cookieUtilsMock
                        .when(() -> CookieUtils.getReferer(request))
                        .thenReturn(Optional.empty());

                // When
                successHandler.onAuthenticationSuccess(request, response, authentication);

                // Then
                verify(tokenCookieWriter)
                        .addTokenCookies(
                                eq(response), eq("access"), eq("refresh"), eq(3600L), eq(604800L));
                verify(authorizationRequestRepository).removeAuthorizationRequestCookies(response);
            }
        }

        @Test
        @DisplayName("integration=true면 통합 요청으로 처리")
        void shouldHandleIntegrationRequest() throws IOException {
            // Given
            when(authentication.getPrincipal()).thenReturn(oAuth2User);

            KakaoOAuthCommand command = mock(KakaoOAuthCommand.class);
            when(kakaoOAuth2ApiMapper.toKakaoOAuthCommand(oAuth2User, true)).thenReturn(command);

            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            KakaoOAuthResponse oauthResponse =
                    KakaoOAuthResponse.integrated("member-id", tokens, "홍길동");
            when(kakaoOAuthLoginUseCase.execute(command)).thenReturn(oauthResponse);

            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                cookieUtilsMock
                        .when(() -> CookieUtils.isIntegrationRequest(request))
                        .thenReturn(true);
                cookieUtilsMock
                        .when(() -> CookieUtils.getRedirectUri(request))
                        .thenReturn(Optional.empty());
                cookieUtilsMock
                        .when(() -> CookieUtils.getReferer(request))
                        .thenReturn(Optional.empty());

                // When
                successHandler.onAuthenticationSuccess(request, response, authentication);

                // Then
                verify(kakaoOAuth2ApiMapper).toKakaoOAuthCommand(oAuth2User, true);
            }
        }

        @Test
        @DisplayName("OAuth2AuthenticationToken이 아니면 예외 발생")
        void shouldThrowExceptionWhenNotOAuth2Token() {
            // Given
            Authentication invalidAuth = mock(Authentication.class);

            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> successHandler.onAuthenticationSuccess(request, response, invalidAuth));
        }

        @Test
        @DisplayName("신규 회원이면 joined=false로 리다이렉트")
        void shouldRedirectWithJoinedFalseForNewMember() throws IOException {
            // Given
            when(authentication.getPrincipal()).thenReturn(oAuth2User);

            KakaoOAuthCommand command = mock(KakaoOAuthCommand.class);
            when(kakaoOAuth2ApiMapper.toKakaoOAuthCommand(oAuth2User, false)).thenReturn(command);

            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            KakaoOAuthResponse oauthResponse = KakaoOAuthResponse.newMember("member-id", tokens);
            when(kakaoOAuthLoginUseCase.execute(command)).thenReturn(oauthResponse);

            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                cookieUtilsMock
                        .when(() -> CookieUtils.isIntegrationRequest(request))
                        .thenReturn(false);
                cookieUtilsMock
                        .when(() -> CookieUtils.getRedirectUri(request))
                        .thenReturn(Optional.empty());
                cookieUtilsMock
                        .when(() -> CookieUtils.getReferer(request))
                        .thenReturn(Optional.empty());

                // When
                successHandler.onAuthenticationSuccess(request, response, authentication);

                // Then - 신규 회원이므로 joined=false
                verify(kakaoOAuthLoginUseCase).execute(command);
            }
        }

        @Test
        @DisplayName("기존 회원이면 joined=true로 리다이렉트")
        void shouldRedirectWithJoinedTrueForExistingMember() throws IOException {
            // Given
            when(authentication.getPrincipal()).thenReturn(oAuth2User);

            KakaoOAuthCommand command = mock(KakaoOAuthCommand.class);
            when(kakaoOAuth2ApiMapper.toKakaoOAuthCommand(oAuth2User, false)).thenReturn(command);

            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            KakaoOAuthResponse oauthResponse =
                    KakaoOAuthResponse.existingKakaoMember("member-id", tokens);
            when(kakaoOAuthLoginUseCase.execute(command)).thenReturn(oauthResponse);

            try (MockedStatic<CookieUtils> cookieUtilsMock = mockStatic(CookieUtils.class)) {
                cookieUtilsMock
                        .when(() -> CookieUtils.isIntegrationRequest(request))
                        .thenReturn(false);
                cookieUtilsMock
                        .when(() -> CookieUtils.getRedirectUri(request))
                        .thenReturn(Optional.empty());
                cookieUtilsMock
                        .when(() -> CookieUtils.getReferer(request))
                        .thenReturn(Optional.empty());

                // When
                successHandler.onAuthenticationSuccess(request, response, authentication);

                // Then - 기존 회원이므로 joined=true 리다이렉트 발생
                verify(kakaoOAuthLoginUseCase).execute(command);
            }
        }
    }
}
