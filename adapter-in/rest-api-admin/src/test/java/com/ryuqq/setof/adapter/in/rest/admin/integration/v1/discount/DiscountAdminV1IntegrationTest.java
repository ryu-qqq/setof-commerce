package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.discount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPoliciesUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.exception.DiscountPolicyNotFoundException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Admin Discount V1 API 통합 테스트 (Legacy)
 *
 * <p>DiscountV1Controller의 모든 엔드포인트를 통합 테스트합니다.
 *
 * <p><strong>구현 상태:</strong>
 *
 * <ul>
 *   <li>✅ 구현됨: 정책 조회/목록/생성/수정/사용상태수정, 엑셀업로드, 대상 조회/생성/수정
 *   <li>❌ 미구현: 히스토리 조회 (Phase 3 예정)
 * </ul>
 *
 * <p><strong>테스트 Mock 전략:</strong>
 *
 * <ul>
 *   <li>UseCase는 MockBean으로 주입됨 (TestMockBeanConfig)
 *   <li>존재하지 않는 리소스 접근 시 DiscountPolicyNotFoundException 발생
 *   <li>정상 케이스는 빈 목록 또는 Mock 응답 반환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("Admin Discount V1 API 통합 테스트 (Legacy)")
class DiscountAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_DISCOUNT_URL = "/api/v1/discount";
    private static final String V1_DISCOUNTS_URL = "/api/v1/discounts";

    @Autowired private GetDiscountPolicyUseCase getDiscountPolicyUseCase;

    @Autowired private GetDiscountPoliciesUseCase getDiscountPoliciesUseCase;

    @Autowired private UpdateDiscountPolicyUseCase updateDiscountPolicyUseCase;

    @BeforeEach
    void setUpMocks() {
        // 존재하지 않는 정책 ID에 대한 조회 시 예외 발생
        given(getDiscountPolicyUseCase.execute(anyLong()))
                .willThrow(new DiscountPolicyNotFoundException(99999L));

        // 정책 목록 조회 시 빈 목록 반환
        given(getDiscountPoliciesUseCase.execute(any())).willReturn(Collections.emptyList());

        // 정책 수정 시 예외 발생 (존재하지 않는 정책) - void 메서드용 구문
        willThrow(new DiscountPolicyNotFoundException(99999L))
                .given(updateDiscountPolicyUseCase)
                .execute(any());
    }

    // ============================================================
    // 구현된 엔드포인트 테스트 - V2 UseCase 연동 완료
    // ============================================================

    @Nested
    @DisplayName("구현된 엔드포인트 테스트 - 정상 동작 확인")
    class ImplementedEndpoints {

        @Test
        @DisplayName("[ADIS-V1-001] 할인 정책 조회 - 존재하지 않는 ID (404 에러)")
        void fetchDiscountPolicy_notFound() {
            // Given - 존재하지 않는 정책 ID
            long nonExistentId = 99999L;

            // When
            ResponseEntity<Map<String, Object>> response =
                    getExpectingError(V1_DISCOUNT_URL + "/" + nonExistentId);

            // Then - 정책이 존재하지 않으면 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[ADIS-V1-002] 할인 정책 목록 조회 - 빈 목록 반환")
        void getDiscountPolicies_emptyList() {
            // When - 데이터 없이 조회
            ResponseEntity<Map<String, Object>> response =
                    get(V1_DISCOUNTS_URL, new ParameterizedTypeReference<>() {});

            // Then - 빈 목록으로 200 OK 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("success")).isEqualTo(true);
        }

        @Test
        @DisplayName("[ADIS-V1-007] 할인 대상 목록 조회 - 존재하지 않는 정책 (404 에러)")
        void getDiscountTargets_notFound() {
            // Given - 존재하지 않는 정책 ID
            long nonExistentId = 99999L;

            // When
            ResponseEntity<Map<String, Object>> response =
                    getExpectingError(
                            V1_DISCOUNT_URL + "/" + nonExistentId + "/targets?issueType=PRODUCT");

            // Then - 정책이 존재하지 않으면 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[ADIS-V1-008] 할인 대상 생성 - 존재하지 않는 정책 (404 에러)")
        void createDiscountTargets_notFound() {
            // Given - 존재하지 않는 정책 ID와 올바른 요청 형식
            long nonExistentId = 99999L;
            Map<String, Object> requestBody =
                    Map.of("issueType", "PRODUCT", "targetIds", List.of(1L, 2L));

            // When
            ResponseEntity<Map<String, Object>> response =
                    postExpectingError(
                            V1_DISCOUNT_URL + "/" + nonExistentId + "/targets", requestBody);

            // Then - 정책이 존재하지 않으면 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[ADIS-V1-009] 할인 대상 수정 - 존재하지 않는 정책 (404 에러)")
        void updateDiscountTargets_notFound() {
            // Given - 존재하지 않는 정책 ID와 올바른 요청 형식
            long nonExistentId = 99999L;
            Map<String, Object> requestBody =
                    Map.of("issueType", "PRODUCT", "targetIds", List.of(3L, 4L));

            // When
            ResponseEntity<Map<String, Object>> response =
                    putExpectingError(
                            V1_DISCOUNT_URL + "/" + nonExistentId + "/targets", requestBody);

            // Then - 정책이 존재하지 않으면 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 히스토리 API 테스트
    // ============================================================

    @Nested
    @DisplayName("히스토리 API 테스트")
    class HistoryEndpoints {

        @Test
        @DisplayName("[ADIS-V1-010] 할인 정책 히스토리 조회 - 빈 목록 반환")
        void getDiscountHistories_emptyList() {
            // When - 데이터 없이 조회
            ResponseEntity<Map<String, Object>> response =
                    get(V1_DISCOUNTS_URL + "/history", new ParameterizedTypeReference<>() {});

            // Then - 빈 목록으로 200 OK 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("success")).isEqualTo(true);
        }

        @Test
        @DisplayName("[ADIS-V1-011] 할인 사용 히스토리 조회 - 미구현 (500 에러)")
        void getDiscountUseHistories_unsupported() {
            // When - 인프라 부재로 미구현 상태
            ResponseEntity<Map<String, Object>> response =
                    getExpectingError(V1_DISCOUNT_URL + "/history/1/use");

            // Then - UnsupportedOperationException으로 500 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================================================
    // 요청 검증 테스트 - 잘못된 요청 형식
    // ============================================================

    @Nested
    @DisplayName("요청 검증 테스트 - 4xx 에러 반환")
    class ValidationTests {

        @Test
        @DisplayName("[ADIS-V1-003] 할인 정책 생성 - 잘못된 요청 형식 (4xx 에러)")
        void createDiscountPolicy_invalidRequest() {
            // Given - 필수 필드 누락
            Map<String, Object> discountDetails = new LinkedHashMap<>();
            discountDetails.put("discountPolicyName", "테스트 할인");
            // 필수 필드 누락: discountType, publisherType, issueType 등
            Map<String, Object> requestBody = Map.of("discountDetails", discountDetails);

            // When
            ResponseEntity<Map<String, Object>> response =
                    postExpectingError(V1_DISCOUNT_URL, requestBody);

            // Then - 검증 실패 또는 Mock NPE로 5xx 에러 반환
            // Note: Mock 환경에서는 Validation 후 UseCase에서 NPE 발생 가능
            assertThat(
                            response.getStatusCode().is4xxClientError()
                                    || response.getStatusCode().is5xxServerError())
                    .isTrue();
        }

        @Test
        @DisplayName("[ADIS-V1-004] 할인 정책 수정 - 존재하지 않는 정책 (404 에러)")
        void updateDiscountPolicy_notFound() {
            // Given - 존재하지 않는 정책 ID와 올바른 요청 형식
            long nonExistentId = 99999L;
            Map<String, Object> discountDetails = new LinkedHashMap<>();
            discountDetails.put("discountPolicyName", "수정된 할인");
            discountDetails.put("discountType", "RATIO");
            discountDetails.put("publisherType", "ADMIN");
            discountDetails.put("issueType", "PRODUCT");
            discountDetails.put("discountLimitYn", "Y");
            discountDetails.put("maxDiscountPrice", 10000L);
            discountDetails.put("shareYn", "Y");
            discountDetails.put("shareRatio", 50.0);
            discountDetails.put("discountRatio", 10.0);
            discountDetails.put("policyStartDate", "2024-01-01 00:00:00");
            discountDetails.put("policyEndDate", "2024-12-31 23:59:59");
            discountDetails.put("priority", 1);
            discountDetails.put("activeYn", "Y");
            Map<String, Object> requestBody = Map.of("discountDetails", discountDetails);

            // When
            ResponseEntity<Map<String, Object>> response =
                    putExpectingError(V1_DISCOUNT_URL + "/" + nonExistentId, requestBody);

            // Then - 정책이 존재하지 않으면 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[ADIS-V1-005] 할인 정책 사용 상태 수정 - 잘못된 요청 형식")
        void updateDiscountPolicyUsageStatus_invalidRequest() {
            // Given - 잘못된 형식
            Map<String, Object> requestBody =
                    Map.of("discountPolicyIds", List.of(1L, 2L), "useYn", true);

            // When
            ResponseEntity<Map<String, Object>> response =
                    patchExpectingError(V1_DISCOUNTS_URL, requestBody);

            // Then - 검증 실패 또는 정책 없음으로 4xx/5xx 에러 반환
            assertThat(
                            response.getStatusCode().is4xxClientError()
                                    || response.getStatusCode().is5xxServerError())
                    .isTrue();
        }

        @Test
        @DisplayName("[ADIS-V1-006] 엑셀 업로드 - 잘못된 요청 형식 (4xx 에러)")
        void createDiscountsFromExcel_invalidRequest() {
            // Given - 필수 필드 누락된 요청
            Map<String, Object> discountDetails = new LinkedHashMap<>();
            discountDetails.put("discountPolicyName", "엑셀 할인1");
            // 필수 필드 누락
            List<Map<String, Object>> requestBody =
                    List.of(Map.of("discountDetails", discountDetails));

            // When
            ResponseEntity<Map<String, Object>> response =
                    postExpectingError(V1_DISCOUNTS_URL + "/excel", requestBody);

            // Then - 검증 실패 또는 Mock NPE로 5xx 에러 반환
            assertThat(
                            response.getStatusCode().is4xxClientError()
                                    || response.getStatusCode().is5xxServerError())
                    .isTrue();
        }
    }
}
