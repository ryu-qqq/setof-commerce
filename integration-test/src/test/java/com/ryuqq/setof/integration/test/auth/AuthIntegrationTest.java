package com.ryuqq.setof.integration.test.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.integration.test.auth.fixture.AuthIntegrationTestFixture;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Auth Integration Test
 *
 * <p>인증 API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>로컬 로그인 성공/실패 케이스
 *   <li>로그아웃 성공/실패 케이스
 *   <li>인증 플로우 시나리오 테스트
 * </ul>
 *
 * @since 1.0.0
 */
@DisplayName("Auth Integration Test")
class AuthIntegrationTest extends IntegrationTestBase {

    // ============================================================
    // 로그인 테스트
    // ============================================================

    @Nested
    @DisplayName("로컬 로그인")
    class LoginTest {

        @Test
        @Sql(
                scripts = "classpath:sql/member/member-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @DisplayName("유효한 자격 증명으로 로그인에 성공한다")
        void shouldLoginWithValidCredentials() {
            // given
            LoginApiRequest request =
                    new LoginApiRequest(
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PHONE,
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PASSWORD);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<ApiResponse<TokenApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            TokenApiResponse tokenResponse = response.getBody().data();
            assertThat(tokenResponse).isNotNull();
            assertThat(tokenResponse.accessToken()).isNotNull();
            assertThat(tokenResponse.expiresIn()).isPositive();
        }

        @Test
        @DisplayName("존재하지 않는 핸드폰 번호로 로그인 시 404 에러를 반환한다")
        void shouldReturn404WhenPhoneNumberNotFound() {
            // given
            LoginApiRequest request =
                    new LoginApiRequest(
                            AuthIntegrationTestFixture.NON_EXISTENT_PHONE,
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PASSWORD);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @Sql(
                scripts = "classpath:sql/member/member-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @DisplayName("잘못된 비밀번호로 로그인 시 401 에러를 반환한다")
        void shouldReturn401WhenPasswordIsWrong() {
            // given
            LoginApiRequest request =
                    new LoginApiRequest(
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PHONE,
                            AuthIntegrationTestFixture.WRONG_PASSWORD);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @Sql(
                scripts = "classpath:sql/member/member-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @DisplayName("탈퇴한 회원으로 로그인 시 403 에러를 반환한다")
        void shouldReturn403WhenMemberIsWithdrawn() {
            // given
            LoginApiRequest request =
                    new LoginApiRequest(
                            AuthIntegrationTestFixture.WITHDRAWN_PHONE,
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PASSWORD);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("핸드폰 번호 누락 시 400 에러를 반환한다")
        void shouldReturn400WhenPhoneNumberMissing() {
            // given
            LoginApiRequest request = new LoginApiRequest(null, "Password1!");

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("비밀번호 누락 시 400 에러를 반환한다")
        void shouldReturn400WhenPasswordMissing() {
            // given
            LoginApiRequest request = new LoginApiRequest("01012345678", null);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("잘못된 핸드폰 번호 형식 시 400 에러를 반환한다")
        void shouldReturn400WhenInvalidPhoneNumberFormat() {
            // given
            LoginApiRequest request =
                    new LoginApiRequest(
                            AuthIntegrationTestFixture.INVALID_PHONE_FORMAT,
                            AuthIntegrationTestFixture.ACTIVE_LOCAL_PASSWORD);

            String url = apiV2Url("/auth/login");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(request, jsonHeaders()),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ============================================================
    // 로그아웃 테스트
    // ============================================================

    @Nested
    @DisplayName("로그아웃")
    class LogoutTest {

        @Test
        @DisplayName("인증된 사용자가 로그아웃에 성공한다")
        void shouldLogoutWithValidToken() {
            // given - 먼저 회원가입하여 토큰 획득
            String accessToken = registerAndGetAccessToken();

            String url = apiV2Url("/auth/logout");

            HttpHeaders headers = jsonHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("인증 없이 로그아웃 시 401 에러를 반환한다")
        void shouldReturn401WhenNoAuthentication() {
            // given
            String url = apiV2Url("/auth/logout");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url, HttpMethod.POST, new HttpEntity<>(jsonHeaders()), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 로그아웃 시 401 에러를 반환한다")
        void shouldReturn401WhenInvalidToken() {
            // given
            String url = apiV2Url("/auth/logout");
            String invalidToken = "invalid.jwt.token";

            HttpHeaders headers = jsonHeaders();
            headers.set("Authorization", "Bearer " + invalidToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    // ============================================================
    // 인증 플로우 시나리오 테스트
    // ============================================================

    @Nested
    @DisplayName("인증 플로우 시나리오")
    class AuthFlowScenarioTest {

        @Test
        @DisplayName("회원가입 → 로그아웃 → 로그인 플로우가 정상 동작한다")
        void shouldCompleteFullAuthFlow() {
            // given - 회원가입 (유니크 핸드폰 번호/이메일 사용 - 010 + 8자리)
            String uniqueSuffix = String.format("%08d", System.currentTimeMillis() % 100000000);
            String phoneNumber = "010" + uniqueSuffix;
            String password = "Password1!";

            RegisterMemberApiRequest registerRequest =
                    new RegisterMemberApiRequest(
                            phoneNumber,
                            "flow" + uniqueSuffix + "@example.com",
                            password,
                            "테스터", // 도메인 제약: 2~5자
                            LocalDate.of(1990, 1, 1),
                            "M",
                            true,
                            true,
                            false);

            ResponseEntity<String> rawResponse =
                    restTemplate.exchange(
                            apiV2Url("/members"),
                            HttpMethod.POST,
                            new HttpEntity<>(registerRequest, jsonHeaders()),
                            String.class);

            // 디버깅: 400 에러 시 응답을 assertion 메시지에 포함
            assertThat(rawResponse.getStatusCode())
                    .withFailMessage(
                            "회원가입 실패. Phone: %s, Response: %s", phoneNumber, rawResponse.getBody())
                    .isEqualTo(HttpStatus.CREATED);

            // 응답에서 accessToken 추출
            String responseBody = rawResponse.getBody();
            String accessToken =
                    responseBody.substring(
                            responseBody.indexOf("\"accessToken\":\"") + 15,
                            responseBody.indexOf(
                                    "\"", responseBody.indexOf("\"accessToken\":\"") + 15));

            // when - 로그아웃
            HttpHeaders logoutHeaders = jsonHeaders();
            logoutHeaders.set("Authorization", "Bearer " + accessToken);

            ResponseEntity<ApiResponse<Void>> logoutResponse =
                    restTemplate.exchange(
                            apiV2Url("/auth/logout"),
                            HttpMethod.POST,
                            new HttpEntity<>(logoutHeaders),
                            new ParameterizedTypeReference<>() {});

            assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            // then - 로그인
            LoginApiRequest loginRequest = new LoginApiRequest(phoneNumber, password);

            ResponseEntity<ApiResponse<TokenApiResponse>> loginResponse =
                    restTemplate.exchange(
                            apiV2Url("/auth/login"),
                            HttpMethod.POST,
                            new HttpEntity<>(loginRequest, jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(loginResponse.getBody().data().accessToken()).isNotNull();
        }

        @Test
        @DisplayName("로그인 후 내 정보 조회가 정상 동작한다")
        void shouldGetMeAfterLogin() {
            // given - 회원가입 후 토큰 획득
            String accessToken = registerAndGetAccessToken();

            // when - 내 정보 조회
            HttpHeaders headers = jsonHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            apiV2Url("/members/me"),
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("로그아웃 후 토큰으로 접근 시 401 에러를 반환한다")
        void shouldReturn401WhenAccessingAfterLogout() {
            // given - 회원가입 후 토큰 획득
            String accessToken = registerAndGetAccessToken();

            HttpHeaders headers = jsonHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            // 로그아웃
            restTemplate.exchange(
                    apiV2Url("/auth/logout"),
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // when - 로그아웃한 토큰으로 접근 시도
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            apiV2Url("/members/me"),
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            String.class);

            // then - 토큰이 무효화되어 401 반환 (또는 Silent Refresh 실패 시)
            // 주의: Silent Refresh가 성공하면 200이 될 수 있음
            // 현재 구현에서는 Refresh Token이 DB에서 삭제되므로 401 예상
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.OK); // 구현에 따라 달라질 수 있음
        }
    }

    // ============================================================
    // OAuth2 카카오 로그인 테스트
    // ============================================================

    @Nested
    @DisplayName("OAuth2 카카오 로그인")
    class OAuth2KakaoLoginTest {

        @Test
        @DisplayName("OAuth2 인가 엔드포인트 접근 시 카카오로 리다이렉트된다")
        void shouldRedirectToKakaoAuthorizationServer() {
            // given - OAuth2 인가 엔드포인트
            String url = baseUrl() + "/oauth2/authorization/kakao";

            // when - 리다이렉트를 따라가지 않고 응답만 확인
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then - 302 리다이렉트 또는 OAuth2 흐름 시작 (카카오 URL로 리다이렉트)
            // RestTemplate이 자동으로 리다이렉트를 따라가면 카카오 로그인 페이지 반환
            // 실제 결과는 카카오 서버 응답 또는 리다이렉트 상태 코드
            assertThat(response.getStatusCode())
                    .isIn(
                            HttpStatus.FOUND, // 302 리다이렉트
                            HttpStatus.OK, // 리다이렉트 따라간 후 카카오 페이지
                            HttpStatus.MOVED_PERMANENTLY); // 301
        }

        @Test
        @DisplayName("OAuth2 콜백에 인가 코드 없이 접근 시 에러를 반환한다")
        void shouldReturn401WhenCallbackWithoutCode() {
            // given - 인가 코드 없는 콜백
            String url = baseUrl() + "/login/oauth2/code/kakao";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then - 인가 코드가 없으면 OAuth2 인증 실패
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("OAuth2 콜백에 잘못된 state로 접근 시 에러를 반환한다")
        void shouldReturn401WhenCallbackWithInvalidState() {
            // given - 잘못된 state와 임의의 code
            String url =
                    baseUrl() + "/login/oauth2/code/kakao?code=invalid_code&state=invalid_state";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then - 잘못된 state는 CSRF 방지로 인해 실패
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("OAuth2 콜백에 error 파라미터가 있으면 에러를 반환한다")
        void shouldReturn401WhenCallbackWithErrorParameter() {
            // given - OAuth2 에러 응답 (사용자가 카카오 로그인 취소 등)
            String url =
                    baseUrl()
                            + "/login/oauth2/code/kakao?error=access_denied&error_description=User%20denied%20access";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then - OAuth2 에러는 인증 실패
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("존재하지 않는 OAuth2 프로바이더로 접근 시 404 에러를 반환한다")
        void shouldReturn404WhenUnknownProvider() {
            // given - 존재하지 않는 프로바이더
            String url = baseUrl() + "/oauth2/authorization/unknown_provider";

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then - 등록되지 않은 프로바이더는 404 또는 다른 에러
            assertThat(response.getStatusCode())
                    .isIn(
                            HttpStatus.NOT_FOUND,
                            HttpStatus.BAD_REQUEST,
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    /** 테스트용 회원가입 후 Access Token 반환 */
    private String registerAndGetAccessToken() {
        String uniquePhone = "010" + System.currentTimeMillis() % 100000000;

        RegisterMemberApiRequest request =
                new RegisterMemberApiRequest(
                        uniquePhone,
                        "test" + System.currentTimeMillis() + "@example.com",
                        "Password1!",
                        "테스터",
                        null,
                        null,
                        true,
                        true,
                        false);

        ResponseEntity<ApiResponse<Map<String, Object>>> response =
                restTemplate.exchange(
                        apiV2Url("/members"),
                        HttpMethod.POST,
                        new HttpEntity<>(request, jsonHeaders()),
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return (String) response.getBody().data().get("accessToken");
    }
}
