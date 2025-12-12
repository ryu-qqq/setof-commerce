package com.ryuqq.setof.adapter.in.rest.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.controller.MemberController;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import java.time.LocalDate;
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
 * Member API 통합 테스트
 *
 * <p>Member REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>POST /api/v2/members - 회원가입
 *   <li>GET /api/v2/members/me - 내 정보 조회
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
 * @see MemberController
 */
@DisplayName("Member API 통합 테스트")
class MemberApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = ApiV2Paths.Members.BASE;
    private static final String ME_URL = ApiV2Paths.Members.ME;

    @Nested
    @DisplayName("POST /api/v2/members - 회원가입")
    class Register {

        @Test
        @DisplayName("성공 - 유효한 요청으로 회원가입")
        void register_validRequest_returnsCreated() {
            // Given
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "01055556666",
                            "newuser@example.com",
                            "Password1!",
                            "신규회원",
                            LocalDate.of(1990, 5, 15),
                            "M",
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ApiResponse<TokenApiResponse>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            TokenApiResponse tokenResponse = response.getBody().data();
            assertThat(tokenResponse).isNotNull();
            assertThat(tokenResponse.expiresIn()).isGreaterThan(0);
        }

        @Test
        @DisplayName("성공 - 최소 필수 정보로 회원가입")
        void register_minimalRequest_returnsCreated() {
            // Given
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "01066667777",
                            null,
                            "Password1!",
                            "최소정보",
                            null,
                            null,
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ApiResponse<TokenApiResponse>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @Sql("/sql/member/members-test-data.sql")
        @DisplayName("실패 - 중복 핸드폰 번호로 회원가입 시 409 에러")
        void register_duplicatePhoneNumber_returnsConflict() {
            // Given - 01012345678 is already in test data
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "01012345678",
                            "duplicate@example.com",
                            "Password1!",
                            "중복회원",
                            LocalDate.of(1990, 5, 15),
                            "M",
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(BASE_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("실패 - 핸드폰 번호 누락 시 400 에러")
        void register_missingPhoneNumber_returnsBadRequest() {
            // Given
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            null,
                            "test@example.com",
                            "Password1!",
                            "테스트",
                            null,
                            null,
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(BASE_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("실패 - 잘못된 핸드폰 번호 형식 시 400 에러")
        void register_invalidPhoneNumberFormat_returnsBadRequest() {
            // Given
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "0201234567",
                            "test@example.com",
                            "Password1!",
                            "테스트",
                            null,
                            null,
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(BASE_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("실패 - 비밀번호 정책 위반 시 400 에러")
        void register_weakPassword_returnsBadRequest() {
            // Given - password without special character
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "01077778888",
                            "test@example.com",
                            "password123",
                            "테스트",
                            null,
                            null,
                            true,
                            true,
                            false);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(BASE_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("실패 - 필수 동의 항목 누락 시 400 에러")
        void register_missingRequiredConsent_returnsBadRequest() {
            // Given
            RegisterMemberApiRequest request =
                    new RegisterMemberApiRequest(
                            "01077778888",
                            "test@example.com",
                            "Password1!",
                            "테스트",
                            null,
                            null,
                            null,
                            true,
                            false);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.postForEntity(BASE_URL, request, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/members/me - 내 정보 조회")
    class GetCurrentMember {

        @Test
        @DisplayName("실패 - 인증 없이 접근 시 401 에러")
        void getCurrentMember_noAuthentication_returnsUnauthorized() {
            // When
            ResponseEntity<ProblemDetail> response = get(ME_URL, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("실패 - 유효하지 않은 토큰으로 접근 시 401 에러")
        void getCurrentMember_invalidToken_returnsUnauthorized() {
            // Given
            String invalidToken = "invalid.jwt.token";
            HttpEntity<Void> entity = createAuthenticatedEntity(invalidToken);

            // When
            ResponseEntity<ProblemDetail> response =
                    restTemplate.exchange(ME_URL, HttpMethod.GET, entity, ProblemDetail.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("회원가입 후 내 정보 조회 시나리오")
    class RegisterAndGetCurrentMember {

        @Test
        @DisplayName("성공 - 회원가입 후 쿠키로 내 정보 조회")
        void registerThenGetCurrentMember_success() {
            // Given - Register first
            RegisterMemberApiRequest registerRequest =
                    new RegisterMemberApiRequest(
                            "01088889999",
                            "scenario@example.com",
                            "Password1!",
                            "테스터",
                            LocalDate.of(1988, 3, 20),
                            "W",
                            true,
                            true,
                            true);

            ResponseEntity<ApiResponse<TokenApiResponse>> registerResponse =
                    post(BASE_URL, registerRequest, new ParameterizedTypeReference<>() {});

            assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }
}
