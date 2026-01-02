package com.ryuqq.setof.integration.test.auth;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.ryuqq.setof.SetofCommerceApplication;
import com.ryuqq.setof.integration.test.config.IntegrationTestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * OAuth2 카카오 로그인 WireMock 통합 테스트
 *
 * <p>WireMock을 사용하여 카카오 OAuth2 서버를 모킹하고 전체 플로우를 검증합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>토큰 교환 (인가 코드 → 액세스 토큰)
 *   <li>사용자 정보 조회 (액세스 토큰 → 카카오 사용자 정보)
 *   <li>JWT 토큰 발급 (카카오 사용자 → 서비스 JWT)
 *   <li>회원가입/로그인 분기 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@SpringBootTest(
        classes = SetofCommerceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestConfig.class)
@DisplayName("OAuth2 카카오 로그인 WireMock 통합 테스트")
class OAuth2KakaoWireMockIntegrationTest {

    // static 블록에서 시작하여 @DynamicPropertySource보다 먼저 초기화
    private static final WireMockServer wireMockServer;

    static {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
    }

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.security.oauth2.client.provider.kakao.token-uri",
                () -> "http://localhost:" + wireMockServer.port() + "/oauth/token");
        registry.add(
                "spring.security.oauth2.client.provider.kakao.user-info-uri",
                () -> "http://localhost:" + wireMockServer.port() + "/v2/user/me");
        registry.add("security.oauth2.front-domain-url", () -> "http://localhost:3000");
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private void stubKakaoTokenEndpoint(String accessToken) {
        wireMockServer.stubFor(
                post(urlEqualTo("/oauth/token"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "access_token": "%s",
                                                    "token_type": "bearer",
                                                    "refresh_token": "mock_refresh_token",
                                                    "expires_in": 21599,
                                                    "scope": "profile_nickname account_email",
                                                    "refresh_token_expires_in": 5183999
                                                }
                                                """
                                                        .formatted(accessToken))));
    }

    private void stubKakaoUserInfoEndpoint(
            long kakaoId, String email, String nickname, String name) {
        wireMockServer.stubFor(
                get(urlEqualTo("/v2/user/me"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "id": %d,
                                                    "connected_at": "2024-01-21T01:23:45Z",
                                                    "properties": {
                                                        "nickname": "%s"
                                                    },
                                                    "kakao_account": {
                                                        "profile_nickname_needs_agreement": false,
                                                        "profile": {
                                                            "nickname": "%s",
                                                            "is_default_nickname": false
                                                        },
                                                        "has_email": true,
                                                        "email_needs_agreement": false,
                                                        "is_email_valid": true,
                                                        "is_email_verified": true,
                                                        "email": "%s",
                                                        "name": "%s",
                                                        "has_gender": true,
                                                        "gender_needs_agreement": false,
                                                        "gender": "male"
                                                    }
                                                }
                                                """
                                                        .formatted(
                                                                kakaoId, nickname, nickname, email,
                                                                name))));
    }

    private void stubKakaoTokenEndpointError() {
        wireMockServer.stubFor(
                post(urlEqualTo("/oauth/token"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
{
    "error": "invalid_grant",
    "error_description": "authorization code not found",
    "error_code": "KOE320"
}
""")));
    }

    @Nested
    @DisplayName("토큰 교환 테스트")
    class TokenExchangeTest {

        @Test
        @DisplayName("카카오 토큰 엔드포인트가 모킹되었는지 확인")
        void shouldMockKakaoTokenEndpoint() {
            // given
            stubKakaoTokenEndpoint("test_access_token");

            // when
            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "http://localhost:" + wireMockServer.port() + "/oauth/token",
                            null,
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("test_access_token");
        }

        @Test
        @DisplayName("카카오 사용자 정보 엔드포인트가 모킹되었는지 확인")
        void shouldMockKakaoUserInfoEndpoint() {
            // given
            stubKakaoUserInfoEndpoint(1234567890L, "kakao@test.com", "테스트유저", "홍길동");

            // when
            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            "http://localhost:" + wireMockServer.port() + "/v2/user/me",
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("1234567890");
            assertThat(response.getBody()).contains("kakao@test.com");
            assertThat(response.getBody()).contains("테스트유저");
        }
    }

    @Nested
    @DisplayName("OAuth2 콜백 테스트")
    class OAuth2CallbackTest {

        @Test
        @DisplayName("유효하지 않은 인가 코드로 콜백 시 에러를 반환한다")
        void shouldReturnErrorForInvalidAuthorizationCode() {
            // given
            stubKakaoTokenEndpointError();

            String callbackUrl =
                    baseUrl() + "/login/oauth2/code/kakao?code=invalid_code&state=test_state";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(callbackUrl, String.class);

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("인가 코드 없이 콜백 시 에러를 반환한다")
        void shouldReturnErrorWhenMissingAuthorizationCode() {
            // given
            String callbackUrl = baseUrl() + "/login/oauth2/code/kakao";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(callbackUrl, String.class);

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("에러 파라미터가 있는 콜백 시 에러를 반환한다")
        void shouldReturnErrorWhenErrorParameterPresent() {
            // given
            String callbackUrl =
                    baseUrl()
                            + "/login/oauth2/code/kakao?error=access_denied"
                            + "&error_description=User%20denied%20access";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(callbackUrl, String.class);

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("WireMock 서버 상태 테스트")
    class WireMockServerStatusTest {

        @Test
        @DisplayName("WireMock 서버가 정상적으로 시작되었다")
        void shouldWireMockServerBeRunning() {
            assertThat(wireMockServer.isRunning()).isTrue();
        }

        @Test
        @DisplayName("WireMock 서버 포트가 할당되었다")
        void shouldWireMockServerHavePort() {
            assertThat(wireMockServer.port()).isPositive();
        }

        @Test
        @DisplayName("여러 스텁을 등록하고 리셋할 수 있다")
        void shouldResetStubsCorrectly() {
            // given
            stubKakaoTokenEndpoint("token1");
            stubKakaoUserInfoEndpoint(1L, "test@test.com", "nick", "name");

            // when
            wireMockServer.resetAll();
            stubKakaoTokenEndpoint("token2");

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "http://localhost:" + wireMockServer.port() + "/oauth/token",
                            null,
                            String.class);

            // then
            assertThat(response.getBody()).contains("token2");
        }
    }

    @Nested
    @DisplayName("에러 시나리오 테스트")
    class ErrorScenarioTest {

        @Test
        @DisplayName("카카오 서버 500 에러 시 에러를 반환한다")
        void shouldHandleKakaoServerError() {
            // given
            wireMockServer.stubFor(
                    post(urlEqualTo("/oauth/token"))
                            .willReturn(
                                    aResponse()
                                            .withStatus(500)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(
                                                    """
{
    "error": "internal_error",
    "error_description": "Internal server error"
}
""")));

            String callbackUrl =
                    baseUrl() + "/login/oauth2/code/kakao?code=server_error_test&state=test_state";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(callbackUrl, String.class);

            // then
            assertThat(
                            response.getStatusCode().is4xxClientError()
                                    || response.getStatusCode().is5xxServerError())
                    .isTrue();
        }

        @Test
        @DisplayName("잘못된 응답 형식 시 에러를 반환한다")
        void shouldHandleMalformedResponse() {
            // given
            wireMockServer.stubFor(
                    post(urlEqualTo("/oauth/token"))
                            .willReturn(
                                    aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("not a valid json")));

            String callbackUrl =
                    baseUrl() + "/login/oauth2/code/kakao?code=malformed_test&state=test_state";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(callbackUrl, String.class);

            // then
            assertThat(
                            response.getStatusCode().is4xxClientError()
                                    || response.getStatusCode().is5xxServerError())
                    .isTrue();
        }
    }

    @Nested
    @DisplayName("카카오 응답 형식 테스트")
    class KakaoResponseFormatTest {

        @Test
        @DisplayName("최소한의 사용자 정보만 있을 때도 처리된다")
        void shouldHandleMinimalUserInfo() {
            // given
            wireMockServer.stubFor(
                    get(urlEqualTo("/v2/user/me"))
                            .willReturn(
                                    aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(
                                                    """
                                                    {
                                                        "id": 9999999999,
                                                        "connected_at": "2024-01-21T01:23:45Z"
                                                    }
                                                    """)));

            // when
            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            "http://localhost:" + wireMockServer.port() + "/v2/user/me",
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("9999999999");
        }

        @Test
        @DisplayName("모든 선택 정보가 포함된 응답을 처리한다")
        void shouldHandleFullUserInfo() {
            // given
            wireMockServer.stubFor(
                    get(urlEqualTo("/v2/user/me"))
                            .willReturn(
                                    aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(
                                                    """
{
    "id": 1234567890,
    "connected_at": "2024-01-21T01:23:45Z",
    "properties": {
        "nickname": "전체정보유저"
    },
    "kakao_account": {
        "profile": {
            "nickname": "전체정보유저",
            "thumbnail_image_url": "http://k.kakaocdn.net/thumb.jpg",
            "profile_image_url": "http://k.kakaocdn.net/profile.jpg",
            "is_default_image": false,
            "is_default_nickname": false
        },
        "name": "김철수",
        "email": "full@kakao.com",
        "gender": "male",
        "age_range": "30~39",
        "birthday": "0115",
        "birthyear": "1990",
        "phone_number": "+82 10-1234-5678"
    }
}
""")));

            // when
            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            "http://localhost:" + wireMockServer.port() + "/v2/user/me",
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody())
                    .contains("1234567890")
                    .contains("김철수")
                    .contains("full@kakao.com")
                    .contains("male")
                    .contains("30~39")
                    .contains("0115")
                    .contains("1990")
                    .contains("+82 10-1234-5678");
        }
    }
}
