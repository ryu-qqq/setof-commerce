package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response.ShippingPolicyV2ApiResponse;
import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPoliciesUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPolicyUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * ShippingPolicy Admin V2 Controller 통합 테스트
 *
 * <p>배송 정책 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("ShippingPolicyAdminV2Controller 통합 테스트")
class ShippingPolicyAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/shipping-policies";

    @Autowired private RegisterShippingPolicyUseCase registerShippingPolicyUseCase;

    @Autowired private GetShippingPolicyUseCase getShippingPolicyUseCase;

    @Autowired private GetShippingPoliciesUseCase getShippingPoliciesUseCase;

    @Autowired private UpdateShippingPolicyUseCase updateShippingPolicyUseCase;

    @Autowired private SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase;

    @Autowired private DeleteShippingPolicyUseCase deleteShippingPolicyUseCase;

    @Nested
    @DisplayName("POST /api/v2/admin/sellers/{sellerId}/shipping-policies - 배송 정책 등록")
    class RegisterShippingPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 배송 정책을 등록하면 201 Created를 반환한다")
        void registerShippingPolicy_success() {
            // Given
            Long sellerId = 1L;
            RegisterShippingPolicyV2ApiRequest request =
                    new RegisterShippingPolicyV2ApiRequest(
                            sellerId, "기본 배송 정책", 3000, 50000, "주문 후 2-3일 내 배송", true, 1);

            given(registerShippingPolicyUseCase.execute(any(RegisterShippingPolicyCommand.class)))
                    .willReturn(1L);

            // When
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(1L);

            verify(registerShippingPolicyUseCase).execute(any(RegisterShippingPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "GET /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId} - 배송 정책 상세"
                    + " 조회")
    class GetShippingPolicyTest {

        @Test
        @DisplayName("존재하는 배송 정책 ID로 조회하면 200 OK와 정책 정보를 반환한다")
        void getShippingPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long shippingPolicyId = 1L;
            ShippingPolicyResponse policyResponse =
                    new ShippingPolicyResponse(
                            shippingPolicyId,
                            sellerId,
                            "기본 배송 정책",
                            3000,
                            50000,
                            "주문 후 2-3일 내 배송",
                            true,
                            1);

            given(getShippingPolicyUseCase.execute(shippingPolicyId)).willReturn(policyResponse);

            // When
            ResponseEntity<ApiResponse<ShippingPolicyV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{shippingPolicyId}",
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            shippingPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().shippingPolicyId()).isEqualTo(shippingPolicyId);
            assertThat(response.getBody().data().policyName()).isEqualTo("기본 배송 정책");

            verify(getShippingPolicyUseCase).execute(shippingPolicyId);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/shipping-policies - 배송 정책 목록 조회")
    class GetShippingPoliciesTest {

        @Test
        @DisplayName("셀러의 배송 정책 목록을 조회하면 200 OK와 목록을 반환한다")
        void getShippingPolicies_success() {
            // Given
            Long sellerId = 1L;
            ShippingPolicyResponse policyResponse =
                    new ShippingPolicyResponse(
                            1L, sellerId, "기본 배송 정책", 3000, 50000, "주문 후 2-3일 내 배송", true, 1);

            given(getShippingPoliciesUseCase.execute(any(ShippingPolicySearchQuery.class)))
                    .willReturn(List.of(policyResponse));

            // When
            ResponseEntity<ApiResponse<List<ShippingPolicyV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);

            verify(getShippingPoliciesUseCase).execute(any(ShippingPolicySearchQuery.class));
        }
    }

    @Nested
    @DisplayName(
            "PUT /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId} - 배송 정책 수정")
    class UpdateShippingPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 배송 정책을 수정하면 200 OK를 반환한다")
        void updateShippingPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long shippingPolicyId = 1L;
            UpdateShippingPolicyV2ApiRequest request =
                    new UpdateShippingPolicyV2ApiRequest(
                            "수정된 배송 정책", 2500, 30000, "주문 후 1-2일 내 배송", 2);

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{shippingPolicyId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            shippingPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateShippingPolicyUseCase).execute(any(UpdateShippingPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId}/default -"
                    + " 기본 정책 설정")
    class SetDefaultShippingPolicyTest {

        @Test
        @DisplayName("배송 정책을 기본 정책으로 설정하면 200 OK를 반환한다")
        void setDefaultShippingPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long shippingPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{shippingPolicyId}/default",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            shippingPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(setDefaultShippingPolicyUseCase)
                    .execute(any(SetDefaultShippingPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId}/delete -"
                    + " 배송 정책 삭제")
    class DeleteShippingPolicyTest {

        @Test
        @DisplayName("배송 정책을 삭제하면 200 OK를 반환한다")
        void deleteShippingPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long shippingPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{shippingPolicyId}/delete",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            shippingPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(deleteShippingPolicyUseCase).execute(any(DeleteShippingPolicyCommand.class));
        }
    }
}
