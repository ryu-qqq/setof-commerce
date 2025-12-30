package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ClaimOrderAdminTestFixture;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ApproveClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.CompleteClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmExchangeDeliveredV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmReturnReceivedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterExchangeShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterReturnShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RejectClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ScheduleReturnPickupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.UpdateReturnShippingStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.response.ClaimAdminV2ApiResponse;
import com.ryuqq.setof.application.claim.dto.command.ApproveClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.CompleteClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RejectClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;
import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;
import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.command.ApproveClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.CompleteClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmExchangeDeliveredUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmReturnReceivedUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RegisterExchangeShippingUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RegisterReturnShippingUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RejectClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ScheduleReturnPickupUseCase;
import com.ryuqq.setof.application.claim.port.in.command.UpdateReturnShippingStatusUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetAdminClaimsUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimUseCase;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.exception.ClaimStatusException;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
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
 * Claim Admin API 통합 테스트
 *
 * <p>Admin Claim API의 엔드포인트 동작을 검증합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query: 목록 조회, 단건 조회
 *   <li>Command: 승인, 반려, 완료, 반품/교환 배송 관리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Admin Claim API 통합 테스트")
class ClaimAdminIntegrationTest extends ApiIntegrationTestSupport {

    @Autowired private GetClaimUseCase getClaimUseCase;

    @Autowired private GetAdminClaimsUseCase getAdminClaimsUseCase;

    @Autowired private ApproveClaimUseCase approveClaimUseCase;

    @Autowired private RejectClaimUseCase rejectClaimUseCase;

    @Autowired private CompleteClaimUseCase completeClaimUseCase;

    @Autowired private RegisterReturnShippingUseCase registerReturnShippingUseCase;

    @Autowired private ScheduleReturnPickupUseCase scheduleReturnPickupUseCase;

    @Autowired private UpdateReturnShippingStatusUseCase updateReturnShippingStatusUseCase;

    @Autowired private ConfirmReturnReceivedUseCase confirmReturnReceivedUseCase;

    @Autowired private RegisterExchangeShippingUseCase registerExchangeShippingUseCase;

    @Autowired private ConfirmExchangeDeliveredUseCase confirmExchangeDeliveredUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("클레임 목록 조회")
    class GetClaimsTest {

        @Test
        @DisplayName("ACLM-001: 기본 파라미터로 클레임 목록 조회 성공")
        void getClaims_withDefaultParams_returnsClaimList() {
            // Given
            ClaimResponse claimResponse =
                    createMockClaimResponse(
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID,
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_REQUESTED);

            SliceResponse<ClaimResponse> sliceResponse =
                    SliceResponse.of(List.of(claimResponse), 1, false, null);

            given(getAdminClaimsUseCase.getClaims(any(GetAdminClaimsQuery.class)))
                    .willReturn(sliceResponse);

            // When
            ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "?pageSize=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);
            assertThat(response.getBody().data().content().get(0).claimId())
                    .isEqualTo(ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID);
        }

        @Test
        @DisplayName("ACLM-002: 상태 필터로 클레임 목록 조회 성공")
        void getClaims_withStatusFilter_returnsFilteredClaimList() {
            // Given
            ClaimResponse claimResponse =
                    createMockClaimResponse(
                            ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID,
                            ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_APPROVED);

            SliceResponse<ClaimResponse> sliceResponse =
                    SliceResponse.of(List.of(claimResponse), 1, false, null);

            given(getAdminClaimsUseCase.getClaims(any(GetAdminClaimsQuery.class)))
                    .willReturn(sliceResponse);

            // When
            ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "?claimStatuses=APPROVED&pageSize=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().content()).hasSize(1);
            assertThat(response.getBody().data().content().get(0).status())
                    .isEqualTo(ClaimOrderAdminTestFixture.CLAIM_STATUS_APPROVED);
        }

        @Test
        @DisplayName("ACLM-003: 클레임 유형 필터로 목록 조회 성공")
        void getClaims_withTypeFilter_returnsFilteredClaimList() {
            // Given
            ClaimResponse claimResponse =
                    createMockClaimResponse(
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID,
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_REQUESTED);

            SliceResponse<ClaimResponse> sliceResponse =
                    SliceResponse.of(List.of(claimResponse), 1, false, null);

            given(getAdminClaimsUseCase.getClaims(any(GetAdminClaimsQuery.class)))
                    .willReturn(sliceResponse);

            // When
            ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "?claimTypes=RETURN&pageSize=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().content()).hasSize(1);
        }

        @Test
        @DisplayName("ACLM-004: 빈 결과 반환 시 빈 목록 응답")
        void getClaims_whenNoResults_returnsEmptyList() {
            // Given
            SliceResponse<ClaimResponse> emptySlice = SliceResponse.of(List.of(), 0, false, null);

            given(getAdminClaimsUseCase.getClaims(any(GetAdminClaimsQuery.class)))
                    .willReturn(emptySlice);

            // When
            ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "?claimStatuses=CANCELLED&pageSize=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().content()).isEmpty();
        }

        @Test
        @DisplayName("ACLM-005: 커서 기반 페이지네이션 조회 성공")
        void getClaims_withCursor_returnsNextPage() {
            // Given
            ClaimResponse claimResponse =
                    createMockClaimResponse(
                            ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID,
                            ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_APPROVED);

            SliceResponse<ClaimResponse> sliceResponse =
                    SliceResponse.of(List.of(claimResponse), 1, false, null);

            given(getAdminClaimsUseCase.getClaims(any(GetAdminClaimsQuery.class)))
                    .willReturn(sliceResponse);

            // When
            String lastClaimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "?lastClaimId=" + lastClaimId + "&pageSize=10",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().content()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("클레임 단건 조회")
    class GetClaimTest {

        @Test
        @DisplayName("ACLM-006: 클레임 ID로 단건 조회 성공")
        void getClaim_byClaimId_returnsClaim() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            ClaimResponse claimResponse =
                    createMockClaimResponse(
                            claimId,
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_REQUESTED);

            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimResponse);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "/" + claimId,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().claimId()).isEqualTo(claimId);
            assertThat(response.getBody().data().claimNumber())
                    .isEqualTo(ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER);
        }
    }

    // ========================================================================
    // Command Tests - Approve/Reject/Complete
    // ========================================================================

    @Nested
    @DisplayName("클레임 승인")
    class ApproveClaimTest {

        @Test
        @DisplayName("ACLM-010: REQUESTED 상태 클레임 승인 성공")
        void approveClaim_withRequestedStatus_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            ApproveClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createApproveClaimRequest();

            ClaimResponse approvedClaim =
                    createMockClaimResponse(
                            claimId,
                            ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_APPROVED);

            doNothing().when(approveClaimUseCase).approve(any(ApproveClaimCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(approvedClaim);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.APPROVE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().status())
                    .isEqualTo(ClaimOrderAdminTestFixture.CLAIM_STATUS_APPROVED);
            verify(approveClaimUseCase).approve(any(ApproveClaimCommand.class));
        }
    }

    @Nested
    @DisplayName("클레임 반려")
    class RejectClaimTest {

        @Test
        @DisplayName("ACLM-013: REQUESTED 상태 클레임 반려 성공")
        void rejectClaim_withRequestedStatus_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            RejectClaimV2ApiRequest request = ClaimOrderAdminTestFixture.createRejectClaimRequest();

            ClaimResponse rejectedClaim =
                    createMockClaimResponseWithRejectReason(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_REJECTED,
                            ClaimOrderAdminTestFixture.DEFAULT_REJECT_REASON);

            doNothing().when(rejectClaimUseCase).reject(any(RejectClaimCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(rejectedClaim);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.REJECT_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().status())
                    .isEqualTo(ClaimOrderAdminTestFixture.CLAIM_STATUS_REJECTED);
            assertThat(response.getBody().data().rejectReason())
                    .isEqualTo(ClaimOrderAdminTestFixture.DEFAULT_REJECT_REASON);
            verify(rejectClaimUseCase).reject(any(RejectClaimCommand.class));
        }
    }

    @Nested
    @DisplayName("클레임 완료")
    class CompleteClaimTest {

        @Test
        @DisplayName("ACLM-016: IN_PROGRESS 상태 클레임 완료 처리 성공")
        void completeClaim_withInProgressStatus_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            CompleteClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createCompleteClaimRequest();

            ClaimResponse completedClaim =
                    createMockClaimResponse(
                            claimId,
                            ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_COMPLETED);

            doNothing().when(completeClaimUseCase).complete(any(CompleteClaimCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(completedClaim);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.COMPLETE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().status())
                    .isEqualTo(ClaimOrderAdminTestFixture.CLAIM_STATUS_COMPLETED);
            verify(completeClaimUseCase).complete(any(CompleteClaimCommand.class));
        }
    }

    // ========================================================================
    // Command Tests - Return Shipping
    // ========================================================================

    @Nested
    @DisplayName("반품 배송 등록")
    class RegisterReturnShippingTest {

        @Test
        @DisplayName("ACLM-019: 반품 배송 등록 성공 (고객 직접 발송)")
        void registerReturnShipping_customerShip_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            RegisterReturnShippingV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createRegisterReturnShippingRequest();

            ClaimResponse claimWithShipping =
                    createMockClaimResponseWithReturnShipping(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER,
                            ClaimOrderAdminTestFixture.CARRIER_CJ);

            doNothing()
                    .when(registerReturnShippingUseCase)
                    .registerShipping(any(RegisterReturnShippingCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimWithShipping);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_SHIPPING_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().returnTrackingNumber())
                    .isEqualTo(ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER);
            assertThat(response.getBody().data().returnCarrier())
                    .isEqualTo(ClaimOrderAdminTestFixture.CARRIER_CJ);
            verify(registerReturnShippingUseCase)
                    .registerShipping(any(RegisterReturnShippingCommand.class));
        }
    }

    @Nested
    @DisplayName("반품 수거 예약")
    class ScheduleReturnPickupTest {

        @Test
        @DisplayName("ACLM-022: 방문수거 예약 성공")
        void scheduleReturnPickup_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            ScheduleReturnPickupV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createScheduleReturnPickupRequest();

            ClaimResponse claimWithPickup =
                    createMockClaimResponseWithPickupScheduled(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            request.scheduledAt());

            doNothing()
                    .when(scheduleReturnPickupUseCase)
                    .schedulePickup(any(ScheduleReturnPickupCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimWithPickup);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_PICKUP_SCHEDULE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().returnPickupScheduledAt()).isNotNull();
            assertThat(response.getBody().data().returnShippingStatus())
                    .isEqualTo(ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_PICKUP_SCHEDULED);
            verify(scheduleReturnPickupUseCase)
                    .schedulePickup(any(ScheduleReturnPickupCommand.class));
        }
    }

    @Nested
    @DisplayName("반품 배송 상태 업데이트")
    class UpdateReturnShippingStatusTest {

        @Test
        @DisplayName("ACLM-025: 반품 배송 상태 IN_TRANSIT로 업데이트 성공")
        void updateReturnShippingStatus_toInTransit_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            UpdateReturnShippingStatusV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createUpdateReturnShippingStatusRequest(
                            ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_IN_TRANSIT);

            ClaimResponse claimWithStatus =
                    createMockClaimResponseWithReturnShippingStatus(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_IN_TRANSIT);

            doNothing()
                    .when(updateReturnShippingStatusUseCase)
                    .updateStatus(any(UpdateReturnShippingStatusCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimWithStatus);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    patch(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_SHIPPING_STATUS_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().returnShippingStatus())
                    .isEqualTo(ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_IN_TRANSIT);
            verify(updateReturnShippingStatusUseCase)
                    .updateStatus(any(UpdateReturnShippingStatusCommand.class));
        }
    }

    @Nested
    @DisplayName("반품 수령 확인")
    class ConfirmReturnReceivedTest {

        @Test
        @DisplayName("ACLM-028: 반품 수령 확인 및 검수 통과 처리 성공")
        void confirmReturnReceived_withPassInspection_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            ConfirmReturnReceivedV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createConfirmReturnReceivedRequest();

            ClaimResponse claimReceived =
                    createMockClaimResponseWithInspection(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            ClaimOrderAdminTestFixture.INSPECTION_RESULT_PASS,
                            ClaimOrderAdminTestFixture.DEFAULT_INSPECTION_NOTE);

            doNothing()
                    .when(confirmReturnReceivedUseCase)
                    .confirmReceived(any(ConfirmReturnReceivedCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimReceived);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_RECEIVED_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().inspectionResult())
                    .isEqualTo(ClaimOrderAdminTestFixture.INSPECTION_RESULT_PASS);
            assertThat(response.getBody().data().inspectionNote())
                    .isEqualTo(ClaimOrderAdminTestFixture.DEFAULT_INSPECTION_NOTE);
            assertThat(response.getBody().data().returnReceivedAt()).isNotNull();
            verify(confirmReturnReceivedUseCase)
                    .confirmReceived(any(ConfirmReturnReceivedCommand.class));
        }

        @Test
        @DisplayName("ACLM-029: 검수 불합격 시 반품 수령 확인 성공")
        void confirmReturnReceived_withFailInspection_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            ConfirmReturnReceivedV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createConfirmReturnReceivedRequest(
                            ClaimOrderAdminTestFixture.INSPECTION_RESULT_FAIL,
                            ClaimOrderAdminTestFixture.FAIL_INSPECTION_NOTE);

            ClaimResponse claimReceived =
                    createMockClaimResponseWithInspection(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            ClaimOrderAdminTestFixture.INSPECTION_RESULT_FAIL,
                            ClaimOrderAdminTestFixture.FAIL_INSPECTION_NOTE);

            doNothing()
                    .when(confirmReturnReceivedUseCase)
                    .confirmReceived(any(ConfirmReturnReceivedCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimReceived);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_RECEIVED_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().inspectionResult())
                    .isEqualTo(ClaimOrderAdminTestFixture.INSPECTION_RESULT_FAIL);
            verify(confirmReturnReceivedUseCase)
                    .confirmReceived(any(ConfirmReturnReceivedCommand.class));
        }
    }

    // ========================================================================
    // Command Tests - Exchange Shipping
    // ========================================================================

    @Nested
    @DisplayName("교환품 발송")
    class RegisterExchangeShippingTest {

        @Test
        @DisplayName("ACLM-031: 교환품 발송 등록 성공")
        void registerExchangeShipping_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            RegisterExchangeShippingV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createRegisterExchangeShippingRequest();

            ClaimResponse claimWithExchangeShipping =
                    createMockClaimResponseWithExchangeShipping(
                            claimId,
                            ClaimOrderAdminTestFixture.CLAIM_STATUS_IN_PROGRESS,
                            ClaimOrderAdminTestFixture.EXCHANGE_TRACKING_NUMBER,
                            ClaimOrderAdminTestFixture.CARRIER_CJ);

            doNothing()
                    .when(registerExchangeShippingUseCase)
                    .registerShipping(any(RegisterExchangeShippingCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimWithExchangeShipping);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.EXCHANGE_SHIPPING_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().exchangeTrackingNumber())
                    .isEqualTo(ClaimOrderAdminTestFixture.EXCHANGE_TRACKING_NUMBER);
            assertThat(response.getBody().data().exchangeCarrier())
                    .isEqualTo(ClaimOrderAdminTestFixture.CARRIER_CJ);
            assertThat(response.getBody().data().exchangeShippedAt()).isNotNull();
            verify(registerExchangeShippingUseCase)
                    .registerShipping(any(RegisterExchangeShippingCommand.class));
        }
    }

    @Nested
    @DisplayName("교환품 수령 확인")
    class ConfirmExchangeDeliveredTest {

        @Test
        @DisplayName("ACLM-034: 교환품 수령 확인 성공 (클레임 자동 완료)")
        void confirmExchangeDelivered_success() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            ConfirmExchangeDeliveredV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createConfirmExchangeDeliveredRequest();

            ClaimResponse claimCompleted =
                    createMockClaimResponseWithExchangeDelivered(
                            claimId, ClaimOrderAdminTestFixture.CLAIM_STATUS_COMPLETED);

            doNothing()
                    .when(confirmExchangeDeliveredUseCase)
                    .confirmDelivered(any(ConfirmExchangeDeliveredCommand.class));
            given(getClaimUseCase.getByClaimId(claimId)).willReturn(claimCompleted);

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.EXCHANGE_DELIVERED_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().data().status())
                    .isEqualTo(ClaimOrderAdminTestFixture.CLAIM_STATUS_COMPLETED);
            assertThat(response.getBody().data().exchangeDeliveredAt()).isNotNull();
            verify(confirmExchangeDeliveredUseCase)
                    .confirmDelivered(any(ConfirmExchangeDeliveredCommand.class));
        }
    }

    // ========================================================================
    // State Transition Failure Tests (409 Conflict)
    // ========================================================================

    @Nested
    @DisplayName("상태 전이 실패 테스트 (409 Conflict)")
    class StatusTransitionFailureTest {

        @Test
        @DisplayName("ACLM-040: APPROVED 상태에서 재승인을 시도하면 409를 반환한다")
        void approveClaim_alreadyApproved_returns409() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.APPROVED_CLAIM_ID;
            ApproveClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createApproveClaimRequest();

            willThrow(ClaimStatusException.cannotApprove(ClaimStatus.APPROVED))
                    .given(approveClaimUseCase)
                    .approve(any(ApproveClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.APPROVE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("ACLM-041: REJECTED 상태에서 완료 처리를 시도하면 409를 반환한다")
        void completeClaim_rejectedStatus_returns409() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            CompleteClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createCompleteClaimRequest();

            willThrow(ClaimStatusException.cannotComplete(ClaimStatus.REJECTED))
                    .given(completeClaimUseCase)
                    .complete(any(CompleteClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.COMPLETE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("ACLM-042: COMPLETED 상태에서 반려를 시도하면 409를 반환한다")
        void rejectClaim_completedStatus_returns409() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.COMPLETED_CLAIM_ID;
            RejectClaimV2ApiRequest request = ClaimOrderAdminTestFixture.createRejectClaimRequest();

            willThrow(ClaimStatusException.cannotReject(ClaimStatus.COMPLETED))
                    .given(rejectClaimUseCase)
                    .reject(any(RejectClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.REJECT_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("ACLM-043: CANCELLED 상태에서 승인을 시도하면 409를 반환한다")
        void approveClaim_cancelledStatus_returns409() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.REQUESTED_CLAIM_ID;
            ApproveClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createApproveClaimRequest();

            willThrow(ClaimStatusException.cannotApprove(ClaimStatus.CANCELLED))
                    .given(approveClaimUseCase)
                    .approve(any(ApproveClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.APPROVE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    // ========================================================================
    // Not Found Tests (404)
    // ========================================================================

    @Nested
    @DisplayName("존재하지 않는 리소스 테스트 (404 Not Found)")
    class NotFoundTest {

        @Test
        @DisplayName("ACLM-044: 존재하지 않는 클레임 단건 조회 → 404")
        void getClaim_nonExistent_returns404() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.NON_EXISTENT_CLAIM_ID;

            given(getClaimUseCase.getByClaimId(claimId))
                    .willThrow(ClaimNotFoundException.byId(claimId));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    get(
                            ApiV2Paths.Claims.BASE + "/" + claimId,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("ACLM-045: 존재하지 않는 클레임 승인 시도 → 404")
        void approveClaim_nonExistent_returns404() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.NON_EXISTENT_CLAIM_ID;
            ApproveClaimV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createApproveClaimRequest();

            willThrow(ClaimNotFoundException.byId(claimId))
                    .given(approveClaimUseCase)
                    .approve(any(ApproveClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.APPROVE_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("ACLM-046: 존재하지 않는 클레임 반려 시도 → 404")
        void rejectClaim_nonExistent_returns404() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.NON_EXISTENT_CLAIM_ID;
            RejectClaimV2ApiRequest request = ClaimOrderAdminTestFixture.createRejectClaimRequest();

            willThrow(ClaimNotFoundException.byId(claimId))
                    .given(rejectClaimUseCase)
                    .reject(any(RejectClaimCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE + "/" + claimId + ApiV2Paths.Claims.REJECT_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("ACLM-047: 존재하지 않는 클레임 반품 배송 등록 → 404")
        void registerReturnShipping_nonExistent_returns404() {
            // Given
            String claimId = ClaimOrderAdminTestFixture.NON_EXISTENT_CLAIM_ID;
            RegisterReturnShippingV2ApiRequest request =
                    ClaimOrderAdminTestFixture.createRegisterReturnShippingRequest();

            willThrow(ClaimNotFoundException.byId(claimId))
                    .given(registerReturnShippingUseCase)
                    .registerShipping(any(RegisterReturnShippingCommand.class));

            // When
            ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> response =
                    post(
                            ApiV2Paths.Claims.BASE
                                    + "/"
                                    + claimId
                                    + ApiV2Paths.Claims.RETURN_SHIPPING_PATH,
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Helper Methods - Mock Response Factory
    // ========================================================================

    private ClaimResponse createMockClaimResponse(
            String claimId, String claimNumber, String status) {
        return new ClaimResponse(
                1L,
                claimId,
                claimNumber,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithRejectReason(
            String claimId, String status, String rejectReason) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.REQUESTED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                rejectReason,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithReturnShipping(
            String claimId, String status, String trackingNumber, String carrier) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                trackingNumber,
                carrier,
                null,
                null,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_CUSTOMER_SHIP,
                ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_IN_TRANSIT,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithPickupScheduled(
            String claimId, String status, Instant scheduledAt) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                null,
                null,
                null,
                null,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_SELLER_PICKUP,
                ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_PICKUP_SCHEDULED,
                scheduledAt,
                "서울특별시 강남구 테헤란로 123, 456호",
                "010-1234-5678",
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithReturnShippingStatus(
            String claimId, String status, String shippingStatus) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER,
                ClaimOrderAdminTestFixture.CARRIER_CJ,
                null,
                null,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_CUSTOMER_SHIP,
                shippingStatus,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithInspection(
            String claimId, String status, String inspectionResult, String inspectionNote) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_RETURN,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                ClaimOrderAdminTestFixture.DEFAULT_REFUND_AMOUNT,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER,
                ClaimOrderAdminTestFixture.CARRIER_CJ,
                null,
                null,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_CUSTOMER_SHIP,
                ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_RECEIVED,
                null,
                null,
                null,
                Instant.now(),
                inspectionResult,
                inspectionNote,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithExchangeShipping(
            String claimId, String status, String trackingNumber, String carrier) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_EXCHANGE,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                BigDecimal.ZERO,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER,
                ClaimOrderAdminTestFixture.CARRIER_CJ,
                trackingNumber,
                carrier,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_CUSTOMER_SHIP,
                ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_RECEIVED,
                null,
                null,
                null,
                Instant.now(),
                ClaimOrderAdminTestFixture.INSPECTION_RESULT_PASS,
                ClaimOrderAdminTestFixture.DEFAULT_INSPECTION_NOTE,
                Instant.now(),
                null,
                Instant.now(),
                Instant.now());
    }

    private ClaimResponse createMockClaimResponseWithExchangeDelivered(
            String claimId, String status) {
        return new ClaimResponse(
                1L,
                claimId,
                ClaimOrderAdminTestFixture.APPROVED_CLAIM_NUMBER,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ID,
                ClaimOrderAdminTestFixture.DELIVERED_ORDER_ITEM_ID,
                ClaimOrderAdminTestFixture.CLAIM_TYPE_EXCHANGE,
                "WRONG_SIZE",
                "사이즈가 맞지 않습니다",
                1,
                BigDecimal.ZERO,
                status,
                ClaimOrderAdminTestFixture.DEFAULT_ADMIN_ID,
                Instant.now(),
                null,
                ClaimOrderAdminTestFixture.DEFAULT_TRACKING_NUMBER,
                ClaimOrderAdminTestFixture.CARRIER_CJ,
                ClaimOrderAdminTestFixture.EXCHANGE_TRACKING_NUMBER,
                ClaimOrderAdminTestFixture.CARRIER_CJ,
                ClaimOrderAdminTestFixture.SHIPPING_METHOD_CUSTOMER_SHIP,
                ClaimOrderAdminTestFixture.RETURN_SHIPPING_STATUS_RECEIVED,
                null,
                null,
                null,
                Instant.now(),
                ClaimOrderAdminTestFixture.INSPECTION_RESULT_PASS,
                ClaimOrderAdminTestFixture.DEFAULT_INSPECTION_NOTE,
                Instant.now(),
                Instant.now(),
                Instant.now(),
                Instant.now());
    }
}
