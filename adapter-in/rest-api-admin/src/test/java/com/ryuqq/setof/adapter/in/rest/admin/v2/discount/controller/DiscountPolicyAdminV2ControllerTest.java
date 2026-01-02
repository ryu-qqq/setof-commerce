package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.RegisterDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.response.DiscountPolicyV2ApiResponse;
import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeleteDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.application.discount.port.in.command.ActivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.DeactivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.DeleteDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.RegisterDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPoliciesUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * DiscountPolicy Admin V2 Controller 통합 테스트
 *
 * <p>할인 정책 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("DiscountPolicyAdminV2Controller 통합 테스트")
class DiscountPolicyAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/discount-policies";
    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");

    @Autowired private RegisterDiscountPolicyUseCase registerDiscountPolicyUseCase;

    @Autowired private UpdateDiscountPolicyUseCase updateDiscountPolicyUseCase;

    @Autowired private ActivateDiscountPolicyUseCase activateDiscountPolicyUseCase;

    @Autowired private DeactivateDiscountPolicyUseCase deactivateDiscountPolicyUseCase;

    @Autowired private DeleteDiscountPolicyUseCase deleteDiscountPolicyUseCase;

    @Autowired private GetDiscountPolicyUseCase getDiscountPolicyUseCase;

    @Autowired private GetDiscountPoliciesUseCase getDiscountPoliciesUseCase;

    @Nested
    @DisplayName("POST /api/v2/admin/sellers/{sellerId}/discount-policies - 할인 정책 등록")
    class RegisterDiscountPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 정률 할인 정책을 등록하면 201 Created를 반환한다")
        void registerRateDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            RegisterDiscountPolicyV2ApiRequest request = createRateDiscountRequest();

            given(registerDiscountPolicyUseCase.execute(any(RegisterDiscountPolicyCommand.class)))
                    .willReturn(1L);

            // When
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(1L);

            verify(registerDiscountPolicyUseCase).execute(any(RegisterDiscountPolicyCommand.class));
        }

        @Test
        @DisplayName("유효한 요청으로 정액 할인 정책을 등록하면 201 Created를 반환한다")
        void registerFixedDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            RegisterDiscountPolicyV2ApiRequest request = createFixedDiscountRequest();

            given(registerDiscountPolicyUseCase.execute(any(RegisterDiscountPolicyCommand.class)))
                    .willReturn(2L);

            // When
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(2L);

            verify(registerDiscountPolicyUseCase).execute(any(RegisterDiscountPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "GET /api/v2/admin/sellers/{sellerId}/discount-policies/{discountPolicyId} - 할인 정책 상세"
                    + " 조회")
    class GetDiscountPolicyTest {

        @Test
        @DisplayName("존재하는 할인 정책 ID로 조회하면 200 OK와 정책 정보를 반환한다")
        void getDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long discountPolicyId = 1L;
            DiscountPolicyResponse policyResponse =
                    createRateDiscountPolicyResponse(discountPolicyId, sellerId);

            given(getDiscountPolicyUseCase.execute(discountPolicyId)).willReturn(policyResponse);

            // When
            ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{discountPolicyId}",
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().discountPolicyId()).isEqualTo(discountPolicyId);
            assertThat(response.getBody().data().policyName()).isEqualTo("여름 시즌 할인");
            assertThat(response.getBody().data().discountGroup()).isEqualTo("PRODUCT");
            assertThat(response.getBody().data().discountType()).isEqualTo("RATE");

            verify(getDiscountPolicyUseCase).execute(discountPolicyId);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/discount-policies - 할인 정책 목록 조회")
    class GetDiscountPoliciesTest {

        @Test
        @DisplayName("셀러의 할인 정책 목록을 조회하면 200 OK와 목록을 반환한다")
        void getDiscountPolicies_success() {
            // Given
            Long sellerId = 1L;
            DiscountPolicyResponse policyResponse = createRateDiscountPolicyResponse(1L, sellerId);

            given(getDiscountPoliciesUseCase.execute(any(DiscountPolicySearchQuery.class)))
                    .willReturn(List.of(policyResponse));

            // When
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);
            assertThat(response.getBody().data().get(0).policyName()).isEqualTo("여름 시즌 할인");

            verify(getDiscountPoliciesUseCase).execute(any(DiscountPolicySearchQuery.class));
        }

        @Test
        @DisplayName("빈 목록 조회 시 200 OK와 빈 배열을 반환한다")
        void getDiscountPolicies_empty() {
            // Given
            Long sellerId = 1L;

            given(getDiscountPoliciesUseCase.execute(any(DiscountPolicySearchQuery.class)))
                    .willReturn(List.of());

            // When
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/discount-policies/valid - 유효한 할인 정책 조회")
    class GetValidDiscountPoliciesTest {

        @Test
        @DisplayName("현재 유효한 할인 정책만 조회하면 200 OK와 목록을 반환한다")
        void getValidDiscountPolicies_success() {
            // Given
            Long sellerId = 1L;
            DiscountPolicyResponse policyResponse = createRateDiscountPolicyResponse(1L, sellerId);

            given(getDiscountPoliciesUseCase.execute(any(DiscountPolicySearchQuery.class)))
                    .willReturn(List.of(policyResponse));

            // When
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    get(BASE_URL + "/valid", new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);

            verify(getDiscountPoliciesUseCase).execute(any(DiscountPolicySearchQuery.class));
        }
    }

    @Nested
    @DisplayName(
            "PUT /api/v2/admin/sellers/{sellerId}/discount-policies/{discountPolicyId} - 할인 정책 수정")
    class UpdateDiscountPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 할인 정책을 수정하면 200 OK를 반환한다")
        void updateDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long discountPolicyId = 1L;
            UpdateDiscountPolicyV2ApiRequest request = createUpdateRequest();

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{discountPolicyId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateDiscountPolicyUseCase).execute(any(UpdateDiscountPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/discount-policies/{discountPolicyId}/activate -"
                    + " 할인 정책 활성화")
    class ActivateDiscountPolicyTest {

        @Test
        @DisplayName("할인 정책을 활성화하면 200 OK를 반환한다")
        void activateDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long discountPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{discountPolicyId}/activate",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(activateDiscountPolicyUseCase).execute(any(ActivateDiscountPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/discount-policies/{discountPolicyId}/deactivate"
                    + " - 할인 정책 비활성화")
    class DeactivateDiscountPolicyTest {

        @Test
        @DisplayName("할인 정책을 비활성화하면 200 OK를 반환한다")
        void deactivateDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long discountPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{discountPolicyId}/deactivate",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(deactivateDiscountPolicyUseCase)
                    .execute(any(DeactivateDiscountPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/discount-policies/{discountPolicyId}/delete -"
                    + " 할인 정책 삭제")
    class DeleteDiscountPolicyTest {

        @Test
        @DisplayName("할인 정책을 삭제하면 200 OK를 반환한다")
        void deleteDiscountPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long discountPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{discountPolicyId}/delete",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            discountPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(deleteDiscountPolicyUseCase).execute(any(DeleteDiscountPolicyCommand.class));
        }
    }

    // ========== Helper Methods - Request Objects ==========

    private RegisterDiscountPolicyV2ApiRequest createRateDiscountRequest() {
        return new RegisterDiscountPolicyV2ApiRequest(
                "여름 시즌 할인",
                "PRODUCT",
                "RATE",
                "ALL",
                null,
                10,
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
                true);
    }

    private RegisterDiscountPolicyV2ApiRequest createFixedDiscountRequest() {
        return new RegisterDiscountPolicyV2ApiRequest(
                "정액 할인 이벤트",
                "MEMBER",
                "FIXED_PRICE",
                "ALL",
                null,
                null,
                5000L,
                null,
                50000L,
                FIXED_TIME,
                FUTURE_TIME,
                2,
                500,
                new BigDecimal("60"),
                new BigDecimal("40"),
                200,
                true);
    }

    private UpdateDiscountPolicyV2ApiRequest createUpdateRequest() {
        return new UpdateDiscountPolicyV2ApiRequest(
                "수정된 할인 정책",
                20000L,
                50000L,
                Instant.parse("2026-12-31T23:59:59Z"),
                2,
                2000,
                new BigDecimal("60"),
                new BigDecimal("40"),
                50);
    }

    // ========== Helper Methods - Response Objects ==========

    private DiscountPolicyResponse createRateDiscountPolicyResponse(Long policyId, Long sellerId) {
        return DiscountPolicyResponse.of(
                policyId,
                sellerId,
                "여름 시즌 할인",
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
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

    private DiscountPolicyResponse createFixedDiscountPolicyResponse(Long policyId, Long sellerId) {
        return DiscountPolicyResponse.of(
                policyId,
                sellerId,
                "정액 할인 이벤트",
                DiscountGroup.MEMBER,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                null,
                List.of(),
                null,
                5000L,
                null,
                50000L,
                FIXED_TIME,
                FUTURE_TIME,
                2,
                500,
                new BigDecimal("60"),
                new BigDecimal("40"),
                200,
                true,
                false,
                true,
                FIXED_TIME,
                FIXED_TIME);
    }
}
