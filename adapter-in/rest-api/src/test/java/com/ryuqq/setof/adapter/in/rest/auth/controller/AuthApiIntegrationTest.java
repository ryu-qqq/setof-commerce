package com.ryuqq.setof.adapter.in.rest.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Auth API 통합 테스트
 *
 * <p>Auth REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>POST /api/v2/auth/login - 로컬 로그인
 *   <li>POST /api/v2/auth/logout - 로그아웃
 * </ul>
 *
 * <p><strong>사용 도구:</strong>
 *
 * <ul>
 *   <li>TestRestTemplate - 실제 HTTP 요청/응답 테스트
 *   <li>TestContainers MySQL - 실제 DB 테스트
 *   <li>@Sql - 테스트 데이터 준비
 * </ul>
 *
 * @author Development Team
 * @since 2.0.0
 * @see AuthController
 */
@DisplayName("Auth API 통합 테스트")
class AuthApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String LOGIN_URL = ApiV2Paths.Auth.LOGIN;
    private static final String LOGOUT_URL = ApiV2Paths.Auth.LOGOUT;

    @Nested
    @DisplayName("POST /api/v2/auth/login - 로컬 로그인")
    class Login {

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("성공 - 유효한 자격 증명으로 로그인")
        void login_validCredentials_returnsOk() {
            // Given
            LoginApiRequest request = new LoginApiRequest("01012345678", "Password1!");

            // When
            ResponseEntity<ApiResponse<TokenApiResponse>> response =
                    post(LOGIN_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            TokenApiResponse tokenResponse = response.getBody().data();
            assertThat(tokenResponse).isNotNull();
            assertThat(tokenResponse.expiresIn()).isGreaterThan(0);
        }

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("실패 - 잘못된 비밀번호로 로그인 시 401 에러")
        void login_wrongPassword_returnsUnauthorized() {
            // Given
            LoginApiRequest request = new LoginApiRequest("01012345678", "WrongPassword1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 회원으로 로그인 시 404 에러")
        void login_nonExistentMember_returnsNotFound() {
            // Given
            LoginApiRequest request = new LoginApiRequest("01099990000", "Password1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then - RESTful: 존재하지 않는 리소스는 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("실패 - 핸드폰 번호 누락 시 400 에러")
        void login_missingPhoneNumber_returnsBadRequest() {
            // Given
            LoginApiRequest request = new LoginApiRequest(null, "Password1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("실패 - 비밀번호 누락 시 400 에러")
        void login_missingPassword_returnsBadRequest() {
            // Given
            LoginApiRequest request = new LoginApiRequest("01012345678", null);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("실패 - 잘못된 핸드폰 번호 형식 시 400 에러")
        void login_invalidPhoneNumberFormat_returnsBadRequest() {
            // Given
            LoginApiRequest request = new LoginApiRequest("0201234567", "Password1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("실패 - KAKAO 회원으로 LOCAL 로그인 시 401 에러")
        void login_kakaoMemberWithLocalLogin_returnsUnauthorized() {
            // Given - kakao member has no password
            LoginApiRequest request = new LoginApiRequest("01087654321", "Password1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("실패 - 휴면 회원으로 로그인 시 403 에러")
        void login_inactiveMember_returnsForbidden() {
            // Given - inactive member
            LoginApiRequest request = new LoginApiRequest("01044445555", "Password1!");

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(LOGIN_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.FORBIDDEN, HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("POST /api/v2/auth/logout - 로그아웃")
    class Logout {

        @Test
        @DisplayName("실패 - 인증 없이 로그아웃 시 401 에러")
        void logout_noAuthentication_returnsUnauthorized() {
            // When
            ResponseEntity<ProblemDetail> response =
                    post(LOGOUT_URL, null, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("실패 - 유효하지 않은 토큰으로 로그아웃 시 401 에러")
        void logout_invalidToken_returnsUnauthorized() {
            // Given
            String invalidToken = "invalid.jwt.token";
            HttpEntity<Void> entity = createAuthenticatedEntity(invalidToken);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.exchange(LOGOUT_URL, HttpMethod.POST, entity, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("로그인-로그아웃 시나리오")
    class LoginLogoutScenario {

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("성공 - 로그인 후 토큰 응답 확인")
        void loginScenario_verifyTokenResponse() {
            // Given
            LoginApiRequest loginRequest = new LoginApiRequest("01012345678", "Password1!");

            // When
            ResponseEntity<ApiResponse<TokenApiResponse>> loginResponse =
                    post(LOGIN_URL, loginRequest, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            TokenApiResponse tokenResponse = loginResponse.getBody().data();
            assertThat(tokenResponse.expiresIn()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("연속 로그인 시나리오")
    class ConsecutiveLoginScenario {

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("성공 - 같은 회원이 여러 번 로그인 가능")
        void consecutiveLogin_success() {
            // Given
            LoginApiRequest loginRequest = new LoginApiRequest("01012345678", "Password1!");

            // When - First login
            ResponseEntity<ApiResponse<TokenApiResponse>> firstResponse =
                    post(LOGIN_URL, loginRequest, new ParameterizedTypeReference<>() {});

            // When - Second login
            ResponseEntity<ApiResponse<TokenApiResponse>> secondResponse =
                    post(LOGIN_URL, loginRequest, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }
}
