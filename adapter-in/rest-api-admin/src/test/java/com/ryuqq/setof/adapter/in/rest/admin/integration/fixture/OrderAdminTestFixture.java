package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.query.OrderFilterV1ApiRequest;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.response.OrderItemResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Order Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 Order 관련 상수 및 Request/Response 빌더를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class OrderAdminTestFixture {

    private OrderAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Order Constants
    // ============================================================

    public static final String DEFAULT_ORDER_ID = "550e8400-e29b-41d4-a716-446655440000";
    public static final String DEFAULT_ORDER_NUMBER = "ORD-2024-00001";
    public static final String DEFAULT_CHECKOUT_ID = "660e8400-e29b-41d4-a716-446655440000";
    public static final String DEFAULT_PAYMENT_ID = "770e8400-e29b-41d4-a716-446655440000";
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final String DEFAULT_MEMBER_ID = "01901234-5678-7abc-def0-123456789abc";
    public static final String DEFAULT_ORDER_STATUS = "ORDERED";
    public static final String COMPLETED_ORDER_STATUS = "COMPLETED";

    // ============================================================
    // Receiver Constants
    // ============================================================

    public static final String DEFAULT_RECEIVER_NAME = "홍길동";
    public static final String DEFAULT_RECEIVER_PHONE = "01012345678";
    public static final String DEFAULT_ADDRESS = "서울시 강남구 테헤란로 123";
    public static final String DEFAULT_ADDRESS_DETAIL = "123동 456호";
    public static final String DEFAULT_ZIP_CODE = "12345";
    public static final String DEFAULT_MEMO = "문 앞에 놓아주세요";

    // ============================================================
    // Amount Constants
    // ============================================================

    public static final BigDecimal DEFAULT_TOTAL_ITEM_AMOUNT = new BigDecimal("100000");
    public static final BigDecimal DEFAULT_SHIPPING_FEE = new BigDecimal("3000");
    public static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal("103000");

    // ============================================================
    // Order Item Constants
    // ============================================================

    public static final String DEFAULT_ORDER_ITEM_ID = "880e8400-e29b-41d4-a716-446655440000";
    public static final Long DEFAULT_PRODUCT_ID = 100L;
    public static final Long DEFAULT_PRODUCT_STOCK_ID = 1000L;
    public static final String DEFAULT_PRODUCT_NAME = "테스트 상품";
    public static final String DEFAULT_PRODUCT_IMAGE = "https://example.com/product.jpg";
    public static final String DEFAULT_OPTION_NAME = "빨강 / M";
    public static final String DEFAULT_BRAND_NAME = "테스트 브랜드";
    public static final String DEFAULT_SELLER_NAME = "테스트 셀러";

    // ============================================================
    // Payment Constants
    // ============================================================

    public static final String DEFAULT_PG_PROVIDER = "PORTONE";
    public static final String DEFAULT_PG_TRANSACTION_ID = "portone_123456";
    public static final String DEFAULT_PAYMENT_METHOD = "CARD";
    public static final String DEFAULT_PAYMENT_STATUS = "APPROVED";

    // ============================================================
    // Request Builders
    // ============================================================

    /**
     * 기본 주문 필터 요청을 생성합니다.
     *
     * @return OrderFilterV1ApiRequest
     */
    public static OrderFilterV1ApiRequest createOrderFilterV1Request() {
        return new OrderFilterV1ApiRequest(
                null, // lastDomainId
                null, // orderStatusList
                "MONTH", // periodType
                null, // sellerId
                null, // searchKeyword
                null, // startDate
                null // endDate
                );
    }

    /**
     * 커스텀 필터 요청을 생성합니다.
     *
     * @param sellerId 셀러 ID
     * @param orderStatusList 주문 상태 목록
     * @return OrderFilterV1ApiRequest
     */
    public static OrderFilterV1ApiRequest createOrderFilterV1Request(
            Long sellerId, List<String> orderStatusList) {
        return new OrderFilterV1ApiRequest(
                null, // lastDomainId
                orderStatusList,
                "MONTH", // periodType
                sellerId,
                null, // searchKeyword
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now());
    }

    // ============================================================
    // Response Builders for Mocking
    // ============================================================

    /**
     * OrderResponse Mock 데이터를 생성합니다.
     *
     * @return OrderResponse
     */
    public static OrderResponse createOrderResponse() {
        return createOrderResponse(DEFAULT_ORDER_ID, DEFAULT_ORDER_STATUS);
    }

    /**
     * 커스텀 값으로 OrderResponse Mock 데이터를 생성합니다.
     *
     * @param orderId 주문 ID
     * @param status 주문 상태
     * @return OrderResponse
     */
    public static OrderResponse createOrderResponse(String orderId, String status) {
        return new OrderResponse(
                orderId,
                DEFAULT_ORDER_NUMBER,
                DEFAULT_CHECKOUT_ID,
                DEFAULT_PAYMENT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_MEMBER_ID,
                status,
                List.of(createOrderItemResponse()),
                DEFAULT_RECEIVER_NAME,
                DEFAULT_RECEIVER_PHONE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_ZIP_CODE,
                DEFAULT_MEMO,
                DEFAULT_TOTAL_ITEM_AMOUNT,
                DEFAULT_SHIPPING_FEE,
                DEFAULT_TOTAL_AMOUNT,
                Instant.now(),
                null, // confirmedAt
                null, // shippedAt
                null, // deliveredAt
                null, // completedAt
                null // cancelledAt
                );
    }

    /**
     * 결제 정보 없이 OrderResponse를 생성합니다.
     *
     * @return OrderResponse
     */
    public static OrderResponse createOrderResponseWithoutPayment() {
        return new OrderResponse(
                DEFAULT_ORDER_ID,
                DEFAULT_ORDER_NUMBER,
                DEFAULT_CHECKOUT_ID,
                null, // paymentId
                DEFAULT_SELLER_ID,
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_STATUS,
                List.of(createOrderItemResponse()),
                DEFAULT_RECEIVER_NAME,
                DEFAULT_RECEIVER_PHONE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_ZIP_CODE,
                DEFAULT_MEMO,
                DEFAULT_TOTAL_ITEM_AMOUNT,
                DEFAULT_SHIPPING_FEE,
                DEFAULT_TOTAL_AMOUNT,
                Instant.now(),
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * OrderItemResponse Mock 데이터를 생성합니다.
     *
     * @return OrderItemResponse
     */
    public static OrderItemResponse createOrderItemResponse() {
        return new OrderItemResponse(
                DEFAULT_ORDER_ITEM_ID,
                DEFAULT_PRODUCT_ID,
                DEFAULT_PRODUCT_STOCK_ID,
                2, // orderedQuantity
                0, // cancelledQuantity
                0, // refundedQuantity
                2, // effectiveQuantity
                new BigDecimal("50000"), // unitPrice
                DEFAULT_TOTAL_ITEM_AMOUNT, // totalPrice
                "ORDERED",
                DEFAULT_PRODUCT_NAME,
                DEFAULT_PRODUCT_IMAGE,
                DEFAULT_OPTION_NAME,
                DEFAULT_BRAND_NAME,
                DEFAULT_SELLER_NAME);
    }

    /**
     * PaymentResponse Mock 데이터를 생성합니다.
     *
     * @return PaymentResponse
     */
    public static PaymentResponse createPaymentResponse() {
        return createPaymentResponse(DEFAULT_PAYMENT_ID, DEFAULT_PAYMENT_STATUS);
    }

    /**
     * 커스텀 값으로 PaymentResponse Mock 데이터를 생성합니다.
     *
     * @param paymentId 결제 ID
     * @param status 결제 상태
     * @return PaymentResponse
     */
    public static PaymentResponse createPaymentResponse(String paymentId, String status) {
        return new PaymentResponse(
                paymentId,
                DEFAULT_CHECKOUT_ID,
                DEFAULT_PG_PROVIDER,
                DEFAULT_PG_TRANSACTION_ID,
                DEFAULT_PAYMENT_METHOD,
                status,
                DEFAULT_TOTAL_AMOUNT, // requestedAmount
                DEFAULT_TOTAL_AMOUNT, // approvedAmount
                BigDecimal.ZERO, // refundedAmount
                Instant.now(), // approvedAt
                null, // cancelledAt
                Instant.now() // createdAt
                );
    }

    /**
     * 여러 OrderResponse를 생성합니다.
     *
     * @param count 개수
     * @return OrderResponse 목록
     */
    public static List<OrderResponse> createOrderResponses(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(
                        i -> {
                            String orderId = UUID.randomUUID().toString();
                            return createOrderResponse(orderId, DEFAULT_ORDER_STATUS);
                        })
                .toList();
    }

    /**
     * SliceResponse 형태의 OrderResponse Mock 데이터를 생성합니다.
     *
     * @param count 개수
     * @param hasNext 다음 페이지 존재 여부
     * @return SliceResponse
     */
    public static SliceResponse<OrderResponse> createOrderSliceResponse(
            int count, boolean hasNext) {
        List<OrderResponse> content = createOrderResponses(count);
        return SliceResponse.of(content, 20, hasNext, hasNext ? "next_cursor" : null);
    }
}
