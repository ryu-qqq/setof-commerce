package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.response.RefundPolicyV2ApiResponse;
import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * RefundPolicy Admin V2 Controller 통합 테스트
 *
 * <p>환불 정책 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("RefundPolicyAdminV2Controller 통합 테스트")
class RefundPolicyAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/refund-policies";

    @Autowired private RegisterRefundPolicyUseCase registerRefundPolicyUseCase;

    @Autowired private GetRefundPolicyUseCase getRefundPolicyUseCase;

    @Autowired private GetRefundPoliciesUseCase getRefundPoliciesUseCase;

    @Autowired private UpdateRefundPolicyUseCase updateRefundPolicyUseCase;

    @Autowired private SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase;

    @Autowired private DeleteRefundPolicyUseCase deleteRefundPolicyUseCase;

    @Nested
    @DisplayName("POST /api/v2/admin/sellers/{sellerId}/refund-policies - 환불 정책 등록")
    class RegisterRefundPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 환불 정책을 등록하면 201 Created를 반환한다")
        void registerRefundPolicy_success() {
            // Given
            Long sellerId = 1L;
            RegisterRefundPolicyV2ApiRequest request =
                    new RegisterRefundPolicyV2ApiRequest(
                            "기본 환불 정책",
                            "서울시 강남구",
                            "역삼동 123-45",
                            "06234",
                            7,
                            3000,
                            "상품 수령 후 7일 이내 교환/환불 가능",
                            true,
                            1);

            given(registerRefundPolicyUseCase.execute(any(RegisterRefundPolicyCommand.class)))
                    .willReturn(1L);

            // When
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(1L);

            verify(registerRefundPolicyUseCase).execute(any(RegisterRefundPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "GET /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId} - 환불 정책 상세 조회")
    class GetRefundPolicyTest {

        @Test
        @DisplayName("존재하는 환불 정책 ID로 조회하면 200 OK와 정책 정보를 반환한다")
        void getRefundPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long refundPolicyId = 1L;
            RefundPolicyResponse policyResponse =
                    new RefundPolicyResponse(
                            refundPolicyId,
                            sellerId,
                            "기본 환불 정책",
                            "서울시 강남구",
                            "역삼동 123-45",
                            "06234",
                            7,
                            3000,
                            "상품 수령 후 7일 이내 교환/환불 가능",
                            true,
                            1);

            given(getRefundPolicyUseCase.execute(refundPolicyId, sellerId))
                    .willReturn(policyResponse);

            // When
            ResponseEntity<ApiResponse<RefundPolicyV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{refundPolicyId}",
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            refundPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().refundPolicyId()).isEqualTo(refundPolicyId);
            assertThat(response.getBody().data().policyName()).isEqualTo("기본 환불 정책");

            verify(getRefundPolicyUseCase).execute(refundPolicyId, sellerId);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/refund-policies - 환불 정책 목록 조회")
    class GetRefundPoliciesTest {

        @Test
        @DisplayName("셀러의 환불 정책 목록을 조회하면 200 OK와 목록을 반환한다")
        void getRefundPolicies_success() {
            // Given
            Long sellerId = 1L;
            RefundPolicyResponse policyResponse =
                    new RefundPolicyResponse(
                            1L,
                            sellerId,
                            "기본 환불 정책",
                            "서울시 강남구",
                            "역삼동 123-45",
                            "06234",
                            7,
                            3000,
                            "상품 수령 후 7일 이내 교환/환불 가능",
                            true,
                            1);

            given(getRefundPoliciesUseCase.execute(any(RefundPolicySearchQuery.class)))
                    .willReturn(List.of(policyResponse));

            // When
            ResponseEntity<ApiResponse<List<RefundPolicyV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);

            verify(getRefundPoliciesUseCase).execute(any(RefundPolicySearchQuery.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId} - 환불 정책 수정")
    class UpdateRefundPolicyTest {

        @Test
        @DisplayName("유효한 요청으로 환불 정책을 수정하면 200 OK를 반환한다")
        void updateRefundPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long refundPolicyId = 1L;
            UpdateRefundPolicyV2ApiRequest request =
                    new UpdateRefundPolicyV2ApiRequest(
                            "수정된 환불 정책",
                            "서울시 서초구",
                            "서초동 456-78",
                            "06789",
                            14,
                            2500,
                            "상품 수령 후 14일 이내 교환/환불 가능");

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{refundPolicyId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            refundPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateRefundPolicyUseCase).execute(any(UpdateRefundPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId}/default - 기본"
                    + " 정책 설정")
    class SetDefaultRefundPolicyTest {

        @Test
        @DisplayName("환불 정책을 기본 정책으로 설정하면 200 OK를 반환한다")
        void setDefaultRefundPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long refundPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{refundPolicyId}/default",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            refundPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(setDefaultRefundPolicyUseCase).execute(any(SetDefaultRefundPolicyCommand.class));
        }
    }

    @Nested
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId}/delete - 환불 정책"
                    + " 삭제")
    class DeleteRefundPolicyTest {

        @Test
        @DisplayName("환불 정책을 삭제하면 200 OK를 반환한다")
        void deleteRefundPolicy_success() {
            // Given
            Long sellerId = 1L;
            Long refundPolicyId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{refundPolicyId}/delete",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId,
                            refundPolicyId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(deleteRefundPolicyUseCase).execute(any(DeleteRefundPolicyCommand.class));
        }
    }
}
