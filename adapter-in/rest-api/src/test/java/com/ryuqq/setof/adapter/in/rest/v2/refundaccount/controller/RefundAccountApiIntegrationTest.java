package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.RegisterRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.UpdateRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.response.RefundAccountV2ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * RefundAccount API 통합 테스트
 *
 * <p>환불계좌 CRUD 기능을 검증합니다. 회원당 최대 1개의 환불계좌만 등록 가능합니다.
 *
 * <p>엔드포인트:
 *
 * <ul>
 *   <li>GET /v2/members/me/refund-account - 조회
 *   <li>POST /v2/members/me/refund-account - 등록
 *   <li>PUT /v2/members/me/refund-account - 수정
 *   <li>PATCH /v2/members/me/refund-account/delete - 삭제 (Soft Delete)
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@DisplayName("RefundAccount API 통합 테스트")
class RefundAccountApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/members/me/refund-account";
    private static final String DELETE_URL = BASE_URL + "/delete";

    @Nested
    @DisplayName("환불계좌 조회 API")
    class GetRefundAccount {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/refund-accounts-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("GET /v2/members/me/refund-account - 환불계좌 조회 성공")
        void getRefundAccount_success() {
            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    getAuthenticated(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            RefundAccountV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.id()).isEqualTo(1L);
            assertThat(data.bankId()).isEqualTo(1L);
            assertThat(data.bankName()).isEqualTo("KB국민은행");
            assertThat(data.bankCode()).isEqualTo("004");
            assertThat(data.accountHolderName()).isEqualTo("홍길동");
            assertThat(data.isVerified()).isTrue();
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("GET /v2/members/me/refund-account - 환불계좌 없으면 null 반환")
        void getRefundAccount_notFound_returnsNull() {
            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    getAuthenticated(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isNull();
        }
    }

    @Nested
    @DisplayName("환불계좌 등록 API")
    class RegisterRefundAccount {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/banks-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("POST /v2/members/me/refund-account - 환불계좌 등록 성공")
        void registerRefundAccount_success() {
            // Given
            RegisterRefundAccountV2ApiRequest request =
                    new RegisterRefundAccountV2ApiRequest(1L, "1234567890123", "홍길동");

            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    postAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            RefundAccountV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.id()).isNotNull();
            assertThat(data.bankId()).isEqualTo(1L);
            assertThat(data.accountHolderName()).isEqualTo("홍길동");
            assertThat(data.isVerified()).isTrue();
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("POST /v2/members/me/refund-account - 필수 필드 누락 시 400 에러")
        void registerRefundAccount_missingRequiredFields_returns400() {
            // Given - bankId 누락 (null)
            RegisterRefundAccountV2ApiRequest request =
                    new RegisterRefundAccountV2ApiRequest(null, "1234567890123", "홍길동");

            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    postAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("POST /v2/members/me/refund-account - 계좌번호 형식 오류 시 400 에러")
        void registerRefundAccount_invalidAccountNumber_returns400() {
            // Given - 계좌번호 형식 오류 (9자리)
            RegisterRefundAccountV2ApiRequest request =
                    new RegisterRefundAccountV2ApiRequest(1L, "123456789", "홍길동");

            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    postAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("환불계좌 수정 API")
    class UpdateRefundAccount {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/refund-accounts-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PUT /v2/members/me/refund-account - 환불계좌 수정 성공")
        void updateRefundAccount_success() {
            // Given
            UpdateRefundAccountV2ApiRequest request =
                    new UpdateRefundAccountV2ApiRequest(2L, "9876543210123", "김철수");

            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    putAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            RefundAccountV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.bankId()).isEqualTo(2L);
            assertThat(data.accountHolderName()).isEqualTo("김철수");
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PUT /v2/members/me/refund-account - 환불계좌 없으면 404 에러")
        void updateRefundAccount_notFound_returns404() {
            // Given
            UpdateRefundAccountV2ApiRequest request =
                    new UpdateRefundAccountV2ApiRequest(1L, "1234567890123", "홍길동");

            // When
            ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> response =
                    putAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("환불계좌 삭제 API")
    class DeleteRefundAccount {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/refund-accounts-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PATCH /v2/members/me/refund-account/delete - 환불계좌 삭제 성공 (Soft Delete)")
        void deleteRefundAccount_success() {
            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patchAuthenticated(DELETE_URL, null, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PATCH /v2/members/me/refund-account/delete - 환불계좌 없으면 404 에러")
        void deleteRefundAccount_notFound_returns404() {
            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patchAuthenticated(DELETE_URL, null, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
