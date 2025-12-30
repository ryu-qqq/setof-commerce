package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest.CreateDiscountDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateUseDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.application.discount.port.in.command.ActivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.DeactivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.RegisterDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPoliciesUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * DiscountV1Controller 통합 테스트
 *
 * <p>V1 Legacy Discount API 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("DiscountV1Controller 통합 테스트")
class DiscountV1ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v1";
    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");

    @Autowired private RegisterDiscountPolicyUseCase registerDiscountPolicyUseCase;

    @Autowired private UpdateDiscountPolicyUseCase updateDiscountPolicyUseCase;

    @Autowired private ActivateDiscountPolicyUseCase activateDiscountPolicyUseCase;

    @Autowired private DeactivateDiscountPolicyUseCase deactivateDiscountPolicyUseCase;

    @Autowired private GetDiscountPolicyUseCase getDiscountPolicyUseCase;

    @Autowired private GetDiscountPoliciesUseCase getDiscountPoliciesUseCase;

    @Nested
    @DisplayName("GET /api/v1/discount/{discountPolicyId} - 할인 정책 단건 조회")
    class FetchDiscountPolicyTest {

        @Test
        @DisplayName("존재하는 할인 정책 ID로 조회하면 200 OK와 정책 정보를 반환한다")
        void fetchDiscountPolicy_success() {
            // Given
            Long discountPolicyId = 1L;
            DiscountPolicyResponse policyResponse =
                    createRateDiscountPolicyResponse(discountPolicyId, 1L);

            given(getDiscountPolicyUseCase.execute(discountPolicyId)).willReturn(policyResponse);

            // When
            ResponseEntity<V1ApiResponse<DiscountPolicyV1ApiResponse>> response =
                    get(
                            BASE_URL + "/discount/{discountPolicyId}",
                            new ParameterizedTypeReference<>() {},
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().discountPolicyId()).isEqualTo(discountPolicyId);

            verify(getDiscountPolicyUseCase).execute(discountPolicyId);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/discounts - 할인 정책 목록 조회")
    class GetDiscountPoliciesTest {

        @Test
        @DisplayName("할인 정책 목록을 조회하면 200 OK와 페이지 응답을 반환한다")
        void getDiscountPolicies_success() {
            // Given
            Long sellerId = 1L;
            DiscountPolicyResponse policyResponse = createRateDiscountPolicyResponse(1L, sellerId);

            given(getDiscountPoliciesUseCase.execute(any(DiscountPolicySearchQuery.class)))
                    .willReturn(List.of(policyResponse));

            // When
            ResponseEntity<V1ApiResponse<V1PageResponse<DiscountPolicyV1ApiResponse>>> response =
                    get(
                            BASE_URL + "/discounts?sellerId={sellerId}",
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);

            verify(getDiscountPoliciesUseCase).execute(any(DiscountPolicySearchQuery.class));
        }

        @Test
        @DisplayName("빈 목록 조회 시 200 OK와 빈 페이지를 반환한다")
        void getDiscountPolicies_empty() {
            // Given
            given(getDiscountPoliciesUseCase.execute(any(DiscountPolicySearchQuery.class)))
                    .willReturn(List.of());

            // When
            ResponseEntity<V1ApiResponse<V1PageResponse<DiscountPolicyV1ApiResponse>>> response =
                    get(BASE_URL + "/discounts", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().content()).isEmpty();
        }
    }

    @Nested
    @DisplayName("POST /api/v1/discount - 할인 정책 생성")
    class CreateDiscountPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 할인 정책을 생성하면 200 OK와 생성된 정책을 반환한다")
        void createDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long createdPolicyId = 1L;
            CreateDiscountV1ApiRequest request = createV1DiscountRequest();
            DiscountPolicyResponse policyResponse =
                    createRateDiscountPolicyResponse(createdPolicyId, sellerId);

            given(registerDiscountPolicyUseCase.execute(any(RegisterDiscountPolicyCommand.class)))
                    .willReturn(createdPolicyId);
            given(getDiscountPolicyUseCase.execute(createdPolicyId)).willReturn(policyResponse);

            // When
            ResponseEntity<V1ApiResponse<DiscountPolicyV1ApiResponse>> response =
                    post(
                            BASE_URL + "/discount?sellerId={sellerId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().discountPolicyId()).isEqualTo(createdPolicyId);

            verify(registerDiscountPolicyUseCase).execute(any(RegisterDiscountPolicyCommand.class));
            verify(getDiscountPolicyUseCase).execute(createdPolicyId);
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/discount/{discountPolicyId} - 할인 정책 수정")
    class UpdateDiscountPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 할인 정책을 수정하면 200 OK와 수정된 정책을 반환한다")
        void updateDiscountPolicy_success() {
            // Given
            Long discountPolicyId = 1L;
            Long sellerId = 1L;
            UpdateDiscountV1ApiRequest request = createUpdateV1DiscountRequest();
            DiscountPolicyResponse policyResponse =
                    createRateDiscountPolicyResponse(discountPolicyId, sellerId);

            given(getDiscountPolicyUseCase.execute(discountPolicyId)).willReturn(policyResponse);

            // When
            ResponseEntity<V1ApiResponse<DiscountPolicyV1ApiResponse>> response =
                    put(
                            BASE_URL + "/discount/{discountPolicyId}?sellerId={sellerId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            discountPolicyId,
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().discountPolicyId()).isEqualTo(discountPolicyId);

            verify(updateDiscountPolicyUseCase).execute(any(UpdateDiscountPolicyCommand.class));
            verify(getDiscountPolicyUseCase).execute(discountPolicyId);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/discounts - 할인 정책 사용 상태 일괄 수정")
    class UpdateDiscountPolicyUsageStatusTest {

        @Test
        @DisplayName("활성화 요청 시 200 OK와 활성화된 정책들을 반환한다")
        void activateDiscountPolicies_success() {
            // Given
            List<Long> policyIds = List.of(1L, 2L);
            UpdateUseDiscountV1ApiRequest request =
                    new UpdateUseDiscountV1ApiRequest(policyIds, "Y");

            DiscountPolicyResponse response1 = createRateDiscountPolicyResponse(1L, 1L);
            DiscountPolicyResponse response2 = createRateDiscountPolicyResponse(2L, 1L);

            given(getDiscountPolicyUseCase.execute(1L)).willReturn(response1);
            given(getDiscountPolicyUseCase.execute(2L)).willReturn(response2);

            // When
            ResponseEntity<V1ApiResponse<List<DiscountPolicyV1ApiResponse>>> response =
                    patch(BASE_URL + "/discounts", request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(2);

            verify(activateDiscountPolicyUseCase, times(2))
                    .execute(any(ActivateDiscountPolicyCommand.class));
        }

        @Test
        @DisplayName("비활성화 요청 시 200 OK와 비활성화된 정책들을 반환한다")
        void deactivateDiscountPolicies_success() {
            // Given
            List<Long> policyIds = List.of(1L);
            UpdateUseDiscountV1ApiRequest request =
                    new UpdateUseDiscountV1ApiRequest(policyIds, "N");

            DiscountPolicyResponse policyResponse = createRateDiscountPolicyResponse(1L, 1L);

            given(getDiscountPolicyUseCase.execute(1L)).willReturn(policyResponse);

            // When
            ResponseEntity<V1ApiResponse<List<DiscountPolicyV1ApiResponse>>> response =
                    patch(BASE_URL + "/discounts", request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);

            verify(deactivateDiscountPolicyUseCase)
                    .execute(any(DeactivateDiscountPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/discounts/excel - 엑셀 업로드로 할인 정책 일괄 생성")
    class CreateDiscountsFromExcelTest {

        @Test
        @DisplayName("엑셀 데이터로 할인 정책을 일괄 생성하면 200 OK와 생성된 정책들을 반환한다")
        void createDiscountsFromExcel_success() {
            // Given
            Long sellerId = 1L;
            List<CreateDiscountV1ApiRequest> requests =
                    List.of(createV1DiscountRequest(), createV1DiscountRequest());

            DiscountPolicyResponse policyResponse1 = createRateDiscountPolicyResponse(1L, sellerId);
            DiscountPolicyResponse policyResponse2 = createRateDiscountPolicyResponse(2L, sellerId);

            given(registerDiscountPolicyUseCase.execute(any(RegisterDiscountPolicyCommand.class)))
                    .willReturn(1L, 2L);
            given(getDiscountPolicyUseCase.execute(1L)).willReturn(policyResponse1);
            given(getDiscountPolicyUseCase.execute(2L)).willReturn(policyResponse2);

            // When
            ResponseEntity<V1ApiResponse<List<DiscountPolicyV1ApiResponse>>> response =
                    post(
                            BASE_URL + "/discounts/excel?sellerId={sellerId}",
                            requests,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(2);
        }
    }

    // ========== Helper Methods - Request Objects ==========

    private CreateDiscountV1ApiRequest createV1DiscountRequest() {
        CreateDiscountDetailsV1ApiRequest details =
                new CreateDiscountDetailsV1ApiRequest(
                        "신년 특가 할인",
                        "RATIO",
                        "ADMIN",
                        "PRODUCT",
                        "Y",
                        10000L,
                        "Y",
                        50.0,
                        10.0,
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                        "테스트 메모",
                        100,
                        "Y");

        return new CreateDiscountV1ApiRequest(null, details);
    }

    private UpdateDiscountV1ApiRequest createUpdateV1DiscountRequest() {
        CreateDiscountDetailsV1ApiRequest details =
                new CreateDiscountDetailsV1ApiRequest(
                        "수정된 할인 정책",
                        "RATIO",
                        "ADMIN",
                        "PRODUCT",
                        "Y",
                        20000L,
                        "Y",
                        60.0,
                        15.0,
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2026, 12, 31, 23, 59, 59),
                        "수정 메모",
                        50,
                        "Y");

        return new UpdateDiscountV1ApiRequest(details, null);
    }

    // ========== Helper Methods - Response Objects ==========

    private DiscountPolicyResponse createRateDiscountPolicyResponse(Long policyId, Long sellerId) {
        return DiscountPolicyResponse.of(
                policyId,
                sellerId,
                "신년 특가 할인",
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.PRODUCT,
                null,
                List.of(),
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                FIXED_TIME,
                FUTURE_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                true,
                false,
                true,
                FIXED_TIME,
                FIXED_TIME);
    }
}
