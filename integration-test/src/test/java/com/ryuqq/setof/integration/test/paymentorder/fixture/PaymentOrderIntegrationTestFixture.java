package com.ryuqq.setof.integration.test.paymentorder.fixture;

import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CompleteCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command.RequestClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.command.ApprovePaymentV2ApiRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Payment/Order 통합 테스트 Fixture
 *
 * <p>Checkout, Payment, Order, Claim 통합 테스트에서 사용하는 상수 및 Request 빌더를 제공합니다.
 *
 * <p>테스트 데이터는 checkout-order-test-data.sql과 일치해야 합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public final class PaymentOrderIntegrationTestFixture {

    private PaymentOrderIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Member Constants (member-test-data.sql과 일치)
    // ============================================================

    public static final String ACTIVE_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
    public static final String ACTIVE_MEMBER_PHONE = "01012345678";
    public static final String ACTIVE_MEMBER_EMAIL = "active@example.com";

    // ============================================================
    // Seller Constants
    // ============================================================

    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final String DEFAULT_SELLER_NAME = "테스트스토어";

    // ============================================================
    // Product Constants
    // ============================================================

    public static final Long DEFAULT_PRODUCT_ID = 100L;
    public static final Long DEFAULT_PRODUCT_STOCK_ID = 1001L;
    public static final String DEFAULT_PRODUCT_NAME = "테스트 상품";
    public static final String DEFAULT_PRODUCT_IMAGE = "https://example.com/image.jpg";
    public static final String DEFAULT_OPTION_NAME = "블랙 / XL";
    public static final String DEFAULT_BRAND_NAME = "테스트브랜드";
    public static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal("29900");
    public static final int DEFAULT_QUANTITY = 2;

    // ============================================================
    // Shipping Constants
    // ============================================================

    public static final String DEFAULT_RECEIVER_NAME = "홍길동";
    public static final String DEFAULT_RECEIVER_PHONE = "010-1234-5678";
    public static final String DEFAULT_ZIP_CODE = "06234";
    public static final String DEFAULT_ADDRESS = "서울시 강남구 테헤란로";
    public static final String DEFAULT_ADDRESS_DETAIL = "123동 456호";
    public static final String DEFAULT_DELIVERY_REQUEST = "부재 시 경비실에 맡겨주세요";

    // ============================================================
    // Payment Constants
    // ============================================================

    public static final String PG_PROVIDER_TOSS = "TOSS";
    public static final String PG_PROVIDER_KAKAO = "KAKAO";
    public static final String PAYMENT_METHOD_CARD = "CARD";
    public static final String PAYMENT_METHOD_KAKAO_PAY = "KAKAO_PAY";
    public static final String DEFAULT_PG_TRANSACTION_ID = "toss_txn_123456789";

    // ============================================================
    // Status Constants
    // ============================================================

    public static final String CHECKOUT_STATUS_PENDING = "PENDING";
    public static final String CHECKOUT_STATUS_COMPLETED = "COMPLETED";
    public static final String CHECKOUT_STATUS_EXPIRED = "EXPIRED";

    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_APPROVED = "APPROVED";
    public static final String PAYMENT_STATUS_CANCELLED = "CANCELLED";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    public static final String ORDER_STATUS_ORDERED = "ORDERED";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    public static final String CLAIM_STATUS_REQUESTED = "REQUESTED";
    public static final String CLAIM_STATUS_APPROVED = "APPROVED";
    public static final String CLAIM_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String CLAIM_STATUS_COMPLETED = "COMPLETED";
    public static final String CLAIM_STATUS_REJECTED = "REJECTED";
    public static final String CLAIM_STATUS_CANCELLED = "CANCELLED";

    // ============================================================
    // Claim Type Constants
    // ============================================================

    public static final String CLAIM_TYPE_CANCEL = "CANCEL";
    public static final String CLAIM_TYPE_RETURN = "RETURN";
    public static final String CLAIM_TYPE_EXCHANGE = "EXCHANGE";
    public static final String CLAIM_TYPE_PARTIAL_REFUND = "PARTIAL_REFUND";

    // ============================================================
    // Claim Reason Constants
    // ============================================================

    public static final String CLAIM_REASON_SIMPLE_CHANGE_OF_MIND = "SIMPLE_CHANGE_OF_MIND";
    public static final String CLAIM_REASON_WRONG_SIZE = "WRONG_SIZE";
    public static final String CLAIM_REASON_DEFECTIVE_PRODUCT = "DEFECTIVE_PRODUCT";
    public static final String CLAIM_REASON_DAMAGED_DURING_DELIVERY = "DAMAGED_DURING_DELIVERY";
    public static final String CLAIM_REASON_WRONG_ITEM_DELIVERED = "WRONG_ITEM_DELIVERED";

    // ============================================================
    // Pre-created Test Data IDs (checkout-order-test-data.sql)
    // ============================================================

    // Checkout in PENDING state
    public static final String PENDING_CHECKOUT_ID = "550e8400-e29b-41d4-a716-446655440001";
    public static final String PENDING_PAYMENT_ID = "660e8400-e29b-41d4-a716-446655440001";

    // Completed Checkout with ORDERED Order
    public static final String ORDERED_CHECKOUT_ID = "550e8400-e29b-41d4-a716-446655440002";
    public static final String ORDERED_PAYMENT_ID = "660e8400-e29b-41d4-a716-446655440002";
    public static final String ORDERED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440002";
    public static final String ORDERED_ORDER_NUMBER = "ORD-20251223-00000001";
    public static final String ORDERED_ORDER_ITEM_ID = "880e8400-e29b-41d4-a716-446655440002";

    // Order in CONFIRMED state
    public static final String CONFIRMED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440003";
    public static final String CONFIRMED_ORDER_NUMBER = "ORD-20251223-00000002";

    // Order in SHIPPED state
    public static final String SHIPPED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440004";
    public static final String SHIPPED_ORDER_NUMBER = "ORD-20251223-00000003";

    // Order in DELIVERED state
    public static final String DELIVERED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440005";
    public static final String DELIVERED_ORDER_NUMBER = "ORD-20251223-00000004";
    public static final String DELIVERED_ORDER_ITEM_ID = "880e8400-e29b-41d4-a716-446655440005";

    // Order in COMPLETED state (구매확정)
    public static final String COMPLETED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440006";
    public static final String COMPLETED_ORDER_NUMBER = "ORD-20251223-00000005";

    // Order in CANCELLED state
    public static final String CANCELLED_ORDER_ID = "770e8400-e29b-41d4-a716-446655440007";
    public static final String CANCELLED_ORDER_NUMBER = "ORD-20251223-00000006";

    // Expired Checkout
    public static final String EXPIRED_CHECKOUT_ID = "550e8400-e29b-41d4-a716-446655440008";

    // Claim in REQUESTED state
    public static final String REQUESTED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440001";
    public static final String REQUESTED_CLAIM_NUMBER = "CLM-20251223-00000001";

    // Claim in APPROVED state
    public static final String APPROVED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440002";
    public static final String APPROVED_CLAIM_NUMBER = "CLM-20251223-00000002";

    // Claim in COMPLETED state (환불 완료)
    public static final String COMPLETED_CLAIM_ID = "990e8400-e29b-41d4-a716-446655440003";
    public static final String COMPLETED_CLAIM_NUMBER = "CLM-20251223-00000003";

    // Non-existent IDs for negative tests
    public static final String NON_EXISTENT_CHECKOUT_ID = "000e8400-e29b-41d4-a716-000000000000";
    public static final String NON_EXISTENT_ORDER_ID = "000e8400-e29b-41d4-a716-000000000001";
    public static final String NON_EXISTENT_CLAIM_ID = "000e8400-e29b-41d4-a716-000000000002";
    public static final String NON_EXISTENT_ORDER_NUMBER = "ORD-00000000-00000000";

    // ============================================================
    // Request Builders
    // ============================================================

    /** 기본 체크아웃 생성 요청을 생성합니다. */
    public static CreateCheckoutV2ApiRequest createDefaultCheckoutRequest() {
        return new CreateCheckoutV2ApiRequest(
                UUID.randomUUID().toString(),
                List.of(createDefaultCheckoutItemRequest()),
                PG_PROVIDER_TOSS,
                PAYMENT_METHOD_CARD,
                DEFAULT_RECEIVER_NAME,
                DEFAULT_RECEIVER_PHONE,
                DEFAULT_ZIP_CODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_DELIVERY_REQUEST);
    }

    /** 여러 아이템이 포함된 체크아웃 생성 요청을 생성합니다. */
    public static CreateCheckoutV2ApiRequest createMultiItemCheckoutRequest() {
        return new CreateCheckoutV2ApiRequest(
                UUID.randomUUID().toString(),
                List.of(
                        createCheckoutItemRequest(
                                1001L, 100L, 1L, 2, new BigDecimal("29900"), "상품 A"),
                        createCheckoutItemRequest(
                                1002L, 101L, 1L, 1, new BigDecimal("39900"), "상품 B"),
                        createCheckoutItemRequest(
                                1003L, 102L, 2L, 3, new BigDecimal("19900"), "상품 C")),
                PG_PROVIDER_TOSS,
                PAYMENT_METHOD_CARD,
                DEFAULT_RECEIVER_NAME,
                DEFAULT_RECEIVER_PHONE,
                DEFAULT_ZIP_CODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_DELIVERY_REQUEST);
    }

    /** 기본 체크아웃 아이템 요청을 생성합니다. */
    public static CreateCheckoutItemV2ApiRequest createDefaultCheckoutItemRequest() {
        return createCheckoutItemRequest(
                DEFAULT_PRODUCT_STOCK_ID,
                DEFAULT_PRODUCT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_QUANTITY,
                DEFAULT_UNIT_PRICE,
                DEFAULT_PRODUCT_NAME);
    }

    /** 커스텀 체크아웃 아이템 요청을 생성합니다. */
    public static CreateCheckoutItemV2ApiRequest createCheckoutItemRequest(
            Long productStockId,
            Long productId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            String productName) {
        return new CreateCheckoutItemV2ApiRequest(
                productStockId,
                productId,
                sellerId,
                quantity,
                unitPrice,
                productName,
                DEFAULT_PRODUCT_IMAGE,
                DEFAULT_OPTION_NAME,
                DEFAULT_BRAND_NAME,
                DEFAULT_SELLER_NAME);
    }

    /** 체크아웃 완료 요청을 생성합니다. */
    public static CompleteCheckoutV2ApiRequest createCompleteCheckoutRequest(
            String paymentId, BigDecimal approvedAmount) {
        return new CompleteCheckoutV2ApiRequest(
                paymentId, DEFAULT_PG_TRANSACTION_ID, approvedAmount);
    }

    /** 기본 결제 승인 요청을 생성합니다. */
    public static ApprovePaymentV2ApiRequest createApprovePaymentRequest(
            String paymentId, BigDecimal approvedAmount) {
        return new ApprovePaymentV2ApiRequest(paymentId, DEFAULT_PG_TRANSACTION_ID, approvedAmount);
    }

    /** 커스텀 PG 거래 ID로 결제 승인 요청을 생성합니다. */
    public static ApprovePaymentV2ApiRequest createApprovePaymentRequest(
            String paymentId, String pgTransactionId, BigDecimal approvedAmount) {
        return new ApprovePaymentV2ApiRequest(paymentId, pgTransactionId, approvedAmount);
    }

    /** 취소 클레임 요청을 생성합니다 (주문 전체 취소). */
    public static RequestClaimV2ApiRequest createCancelClaimRequest(
            String orderId, int quantity, BigDecimal refundAmount) {
        return new RequestClaimV2ApiRequest(
                orderId,
                null, // 전체 주문 취소
                CLAIM_TYPE_CANCEL,
                CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                null,
                quantity,
                refundAmount);
    }

    /** 반품 클레임 요청을 생성합니다. */
    public static RequestClaimV2ApiRequest createReturnClaimRequest(
            String orderId,
            String orderItemId,
            String claimReason,
            int quantity,
            BigDecimal refundAmount) {
        return new RequestClaimV2ApiRequest(
                orderId,
                orderItemId,
                CLAIM_TYPE_RETURN,
                claimReason,
                "상세 사유입니다",
                quantity,
                refundAmount);
    }

    /** 교환 클레임 요청을 생성합니다. */
    public static RequestClaimV2ApiRequest createExchangeClaimRequest(
            String orderId, String orderItemId, String claimReason, int quantity) {
        return new RequestClaimV2ApiRequest(
                orderId,
                orderItemId,
                CLAIM_TYPE_EXCHANGE,
                claimReason,
                "교환을 원합니다",
                quantity,
                BigDecimal.ZERO);
    }

    /** 부분환불 클레임 요청을 생성합니다. */
    public static RequestClaimV2ApiRequest createPartialRefundClaimRequest(
            String orderId, String orderItemId, int quantity, BigDecimal refundAmount) {
        return new RequestClaimV2ApiRequest(
                orderId,
                orderItemId,
                CLAIM_TYPE_PARTIAL_REFUND,
                CLAIM_REASON_DEFECTIVE_PRODUCT,
                "상품 일부가 불량입니다",
                quantity,
                refundAmount);
    }

    // ============================================================
    // Amount Calculation Helpers
    // ============================================================

    /** 기본 주문 총액을 계산합니다. */
    public static BigDecimal calculateDefaultTotalAmount() {
        return DEFAULT_UNIT_PRICE.multiply(BigDecimal.valueOf(DEFAULT_QUANTITY));
    }

    /** 단가와 수량으로 총액을 계산합니다. */
    public static BigDecimal calculateTotalAmount(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // ============================================================
    // Validation Constants
    // ============================================================

    public static final String INVALID_UUID = "invalid-uuid-format";
    public static final String EMPTY_STRING = "";
    public static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal("-1000");
    public static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO;
    public static final int ZERO_QUANTITY = 0;
    public static final int NEGATIVE_QUANTITY = -1;
}
