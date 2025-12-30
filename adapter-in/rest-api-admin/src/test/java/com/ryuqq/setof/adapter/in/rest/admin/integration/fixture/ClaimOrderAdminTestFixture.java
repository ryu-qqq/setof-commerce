package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ApproveClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.CompleteClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmExchangeDeliveredV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmReturnReceivedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterExchangeShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterReturnShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RejectClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ScheduleReturnPickupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.UpdateReturnShippingStatusV2ApiRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Claim/Order Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 상수 및 Request 빌더를 제공합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ClaimOrderAdminTestFixture {

    private ClaimOrderAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Admin Constants
    // ============================================================

    public static final String DEFAULT_ADMIN_ID = "admin-001";
    public static final String SUPER_ADMIN_ID = "super-admin-001";
    public static final Long DEFAULT_SELLER_ID = 1L;

    // ============================================================
    // Order Status Constants
    // ============================================================

    public static final String ORDER_STATUS_ORDERED = "ORDERED";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_PREPARING = "PREPARING";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    // ============================================================
    // Claim Status Constants
    // ============================================================

    public static final String CLAIM_STATUS_REQUESTED = "REQUESTED";
    public static final String CLAIM_STATUS_APPROVED = "APPROVED";
    public static final String CLAIM_STATUS_REJECTED = "REJECTED";
    public static final String CLAIM_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String CLAIM_STATUS_COMPLETED = "COMPLETED";
    public static final String CLAIM_STATUS_CANCELLED = "CANCELLED";

    // ============================================================
    // Claim Type Constants
    // ============================================================

    public static final String CLAIM_TYPE_CANCEL = "CANCEL";
    public static final String CLAIM_TYPE_RETURN = "RETURN";
    public static final String CLAIM_TYPE_EXCHANGE = "EXCHANGE";
    public static final String CLAIM_TYPE_PARTIAL_REFUND = "PARTIAL_REFUND";

    // ============================================================
    // Shipping Method Constants
    // ============================================================

    public static final String SHIPPING_METHOD_SELLER_PICKUP = "SELLER_PICKUP";
    public static final String SHIPPING_METHOD_SELLER_PREPAID_LABEL = "SELLER_PREPAID_LABEL";
    public static final String SHIPPING_METHOD_CUSTOMER_SHIP = "CUSTOMER_SHIP";
    public static final String SHIPPING_METHOD_CUSTOMER_VISIT = "CUSTOMER_VISIT";

    // ============================================================
    // Return Shipping Status Constants
    // ============================================================

    public static final String RETURN_SHIPPING_STATUS_PENDING = "PENDING";
    public static final String RETURN_SHIPPING_STATUS_PICKUP_SCHEDULED = "PICKUP_SCHEDULED";
    public static final String RETURN_SHIPPING_STATUS_PICKED_UP = "PICKED_UP";
    public static final String RETURN_SHIPPING_STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String RETURN_SHIPPING_STATUS_RECEIVED = "RECEIVED";

    // ============================================================
    // Inspection Result Constants
    // ============================================================

    public static final String INSPECTION_RESULT_PASS = "PASS";
    public static final String INSPECTION_RESULT_FAIL = "FAIL";
    public static final String INSPECTION_RESULT_PARTIAL = "PARTIAL";

    // ============================================================
    // Carrier Constants
    // ============================================================

    public static final String CARRIER_CJ = "CJ";
    public static final String CARRIER_HANJIN = "HANJIN";
    public static final String CARRIER_LOTTE = "LOTTE";

    // ============================================================
    // Pre-created Test Data IDs (checkout-order-test-data.sql)
    // ============================================================

    // Orders by status
    public static final String ORDERED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440002";
    public static final String ORDERED_ORDER_NUMBER = "ORD-20251223-00000001";
    public static final String ORDERED_ORDER_ITEM_ID = "880e8400-e29b-41d4-a716-446655440002";

    public static final String CONFIRMED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440003";
    public static final String CONFIRMED_ORDER_NUMBER = "ORD-20251223-00000002";

    public static final String SHIPPED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440004";
    public static final String SHIPPED_ORDER_NUMBER = "ORD-20251223-00000003";

    public static final String DELIVERED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440005";
    public static final String DELIVERED_ORDER_NUMBER = "ORD-20251223-00000004";
    public static final String DELIVERED_ORDER_ITEM_ID = "880e8400-e29b-41d4-a716-446655440005";

    public static final String COMPLETED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440006";
    public static final String COMPLETED_ORDER_NUMBER = "ORD-20251223-00000005";

    public static final String CANCELLED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440007";
    public static final String CANCELLED_ORDER_NUMBER = "ORD-20251223-00000006";

    // Claims by status
    public static final String REQUESTED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440001";
    public static final String REQUESTED_CLAIM_NUMBER = "CLM-20251223-00000001";

    public static final String APPROVED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440002";
    public static final String APPROVED_CLAIM_NUMBER = "CLM-20251223-00000002";

    public static final String COMPLETED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440003";
    public static final String COMPLETED_CLAIM_NUMBER = "CLM-20251223-00000003";

    // Non-existent IDs for negative tests
    public static final String NON_EXISTENT_ORDER_ID = "000e8400-e29b-41d4-a716-000000000001";
    public static final String NON_EXISTENT_CLAIM_ID = "000e8400-e29b-41d4-a716-000000000002";
    public static final String NON_EXISTENT_ORDER_NUMBER = "ORD-00000000-00000000";
    public static final String NON_EXISTENT_CLAIM_NUMBER = "CLM-00000000-00000000";

    // ============================================================
    // Shipping Constants
    // ============================================================

    public static final String DEFAULT_TRACKING_NUMBER = "1234567890123";
    public static final String EXCHANGE_TRACKING_NUMBER = "9876543210987";

    // ============================================================
    // Amount Constants
    // ============================================================

    public static final BigDecimal DEFAULT_REFUND_AMOUNT = new BigDecimal("29900");
    public static final BigDecimal PARTIAL_REFUND_AMOUNT = new BigDecimal("10000");

    // ============================================================
    // Reject Reason Constants
    // ============================================================

    public static final String DEFAULT_REJECT_REASON = "반품 기간 초과로 인해 반품이 불가합니다.";
    public static final String INSPECTION_FAIL_REASON = "상품 상태 불량으로 인해 반품이 거부되었습니다.";

    // ============================================================
    // Inspection Note Constants
    // ============================================================

    public static final String DEFAULT_INSPECTION_NOTE = "상품 상태 양호";
    public static final String FAIL_INSPECTION_NOTE = "상품에 사용 흔적이 있어 반품 불가";

    // ============================================================
    // Validation Constants
    // ============================================================

    public static final String INVALID_UUID = "invalid-uuid-format";
    public static final String EMPTY_STRING = "";

    // ============================================================
    // Request Builders - Claim Approve/Reject
    // ============================================================

    /** 기본 클레임 승인 요청을 생성합니다. */
    public static ApproveClaimV2ApiRequest createApproveClaimRequest() {
        return new ApproveClaimV2ApiRequest(DEFAULT_ADMIN_ID);
    }

    /** 커스텀 관리자 ID로 클레임 승인 요청을 생성합니다. */
    public static ApproveClaimV2ApiRequest createApproveClaimRequest(String adminId) {
        return new ApproveClaimV2ApiRequest(adminId);
    }

    /** 기본 클레임 반려 요청을 생성합니다. */
    public static RejectClaimV2ApiRequest createRejectClaimRequest() {
        return new RejectClaimV2ApiRequest(DEFAULT_ADMIN_ID, DEFAULT_REJECT_REASON);
    }

    /** 커스텀 반려 사유로 클레임 반려 요청을 생성합니다. */
    public static RejectClaimV2ApiRequest createRejectClaimRequest(
            String adminId, String rejectReason) {
        return new RejectClaimV2ApiRequest(adminId, rejectReason);
    }

    /** 기본 클레임 완료 요청을 생성합니다. */
    public static CompleteClaimV2ApiRequest createCompleteClaimRequest() {
        return new CompleteClaimV2ApiRequest(DEFAULT_ADMIN_ID);
    }

    /** 커스텀 관리자 ID로 클레임 완료 요청을 생성합니다. */
    public static CompleteClaimV2ApiRequest createCompleteClaimRequest(String adminId) {
        return new CompleteClaimV2ApiRequest(adminId);
    }

    // ============================================================
    // Request Builders - Return Shipping
    // ============================================================

    /** 기본 반품 배송 등록 요청을 생성합니다 (고객 직접 발송). */
    public static RegisterReturnShippingV2ApiRequest createRegisterReturnShippingRequest() {
        return new RegisterReturnShippingV2ApiRequest(
                SHIPPING_METHOD_CUSTOMER_SHIP, DEFAULT_TRACKING_NUMBER, CARRIER_CJ);
    }

    /** 커스텀 배송 방식으로 반품 배송 등록 요청을 생성합니다. */
    public static RegisterReturnShippingV2ApiRequest createRegisterReturnShippingRequest(
            String shippingMethod, String trackingNumber, String carrier) {
        return new RegisterReturnShippingV2ApiRequest(shippingMethod, trackingNumber, carrier);
    }

    /** 기본 반품 수거 예약 요청을 생성합니다. */
    public static ScheduleReturnPickupV2ApiRequest createScheduleReturnPickupRequest() {
        return new ScheduleReturnPickupV2ApiRequest(
                Instant.now().plus(1, ChronoUnit.DAYS),
                "서울특별시 강남구 테헤란로 123, 456호",
                "010-1234-5678");
    }

    /** 커스텀 일시로 반품 수거 예약 요청을 생성합니다. */
    public static ScheduleReturnPickupV2ApiRequest createScheduleReturnPickupRequest(
            Instant scheduledAt, String pickupAddress, String customerPhone) {
        return new ScheduleReturnPickupV2ApiRequest(scheduledAt, pickupAddress, customerPhone);
    }

    /** 기본 반품 배송 상태 업데이트 요청을 생성합니다. */
    public static UpdateReturnShippingStatusV2ApiRequest createUpdateReturnShippingStatusRequest(
            String status) {
        return new UpdateReturnShippingStatusV2ApiRequest(status, null);
    }

    /** 송장번호와 함께 반품 배송 상태 업데이트 요청을 생성합니다. */
    public static UpdateReturnShippingStatusV2ApiRequest createUpdateReturnShippingStatusRequest(
            String status, String trackingNumber) {
        return new UpdateReturnShippingStatusV2ApiRequest(status, trackingNumber);
    }

    /** 기본 반품 수령 확인 요청을 생성합니다 (검수 통과). */
    public static ConfirmReturnReceivedV2ApiRequest createConfirmReturnReceivedRequest() {
        return new ConfirmReturnReceivedV2ApiRequest(
                INSPECTION_RESULT_PASS, DEFAULT_INSPECTION_NOTE);
    }

    /** 커스텀 검수 결과로 반품 수령 확인 요청을 생성합니다. */
    public static ConfirmReturnReceivedV2ApiRequest createConfirmReturnReceivedRequest(
            String inspectionResult, String inspectionNote) {
        return new ConfirmReturnReceivedV2ApiRequest(inspectionResult, inspectionNote);
    }

    // ============================================================
    // Request Builders - Exchange Shipping
    // ============================================================

    /** 기본 교환품 발송 등록 요청을 생성합니다. */
    public static RegisterExchangeShippingV2ApiRequest createRegisterExchangeShippingRequest() {
        return new RegisterExchangeShippingV2ApiRequest(EXCHANGE_TRACKING_NUMBER, CARRIER_CJ);
    }

    /** 커스텀 송장번호로 교환품 발송 등록 요청을 생성합니다. */
    public static RegisterExchangeShippingV2ApiRequest createRegisterExchangeShippingRequest(
            String trackingNumber, String carrier) {
        return new RegisterExchangeShippingV2ApiRequest(trackingNumber, carrier);
    }

    /** 교환품 배송 완료 확인 요청을 생성합니다 (빈 Request Body). */
    public static ConfirmExchangeDeliveredV2ApiRequest createConfirmExchangeDeliveredRequest() {
        return new ConfirmExchangeDeliveredV2ApiRequest();
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /** 페이지 크기 기본값 */
    public static int getDefaultPageSize() {
        return 20;
    }

    /** 최대 페이지 크기 */
    public static int getMaxPageSize() {
        return 100;
    }
}
