package com.ryuqq.setof.adapter.in.rest.v2.order.dto.response;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 주문 API 응답 DTO
 *
 * @param orderId 주문 ID (UUID)
 * @param orderNumber 주문 번호
 * @param checkoutId 체크아웃 ID (UUID)
 * @param paymentId 결제 ID (UUID)
 * @param sellerId 판매자 ID
 * @param memberId 회원 ID (UUIDv7)
 * @param status 주문 상태
 * @param items 주문 상품 목록
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param address 배송 주소
 * @param addressDetail 상세 주소
 * @param zipCode 우편번호
 * @param memo 배송 메모
 * @param totalItemAmount 상품 총액
 * @param shippingFee 배송비
 * @param totalAmount 총 결제 금액
 * @param orderedAt 주문 일시
 * @param confirmedAt 확정 일시
 * @param shippedAt 배송 시작 일시
 * @param deliveredAt 배송 완료 일시
 * @param completedAt 구매 확정 일시
 * @param cancelledAt 취소 일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "주문 응답")
public record OrderV2ApiResponse(
        @Schema(description = "주문 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                String orderId,
        @Schema(description = "주문 번호", example = "ORD-20241215-000001") String orderNumber,
        @Schema(description = "체크아웃 ID (UUID)", example = "660e8400-e29b-41d4-a716-446655440001")
                String checkoutId,
        @Schema(description = "결제 ID (UUID)", example = "770e8400-e29b-41d4-a716-446655440002")
                String paymentId,
        @Schema(description = "판매자 ID", example = "10") Long sellerId,
        @Schema(description = "회원 ID (UUIDv7)", example = "01912345-6789-7abc-def0-123456789abc")
                String memberId,
        @Schema(description = "주문 상태", example = "ORDERED") String status,
        @Schema(description = "주문 상품 목록") List<OrderItemV2ApiResponse> items,
        @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
        @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhone,
        @Schema(description = "배송 주소", example = "서울시 강남구 테헤란로") String address,
        @Schema(description = "상세 주소", example = "123동 456호") String addressDetail,
        @Schema(description = "우편번호", example = "06234") String zipCode,
        @Schema(description = "배송 메모", example = "부재 시 경비실에 맡겨주세요") String memo,
        @Schema(description = "상품 총액", example = "59800") BigDecimal totalItemAmount,
        @Schema(description = "배송비", example = "0") BigDecimal shippingFee,
        @Schema(description = "총 결제 금액", example = "59800") BigDecimal totalAmount,
        @Schema(description = "주문 일시") Instant orderedAt,
        @Schema(description = "확정 일시") Instant confirmedAt,
        @Schema(description = "배송 시작 일시") Instant shippedAt,
        @Schema(description = "배송 완료 일시") Instant deliveredAt,
        @Schema(description = "구매 확정 일시") Instant completedAt,
        @Schema(description = "취소 일시") Instant cancelledAt) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static OrderV2ApiResponse from(OrderResponse response) {
        List<OrderItemV2ApiResponse> itemResponses =
                response.items() != null
                        ? response.items().stream().map(OrderItemV2ApiResponse::from).toList()
                        : List.of();

        return new OrderV2ApiResponse(
                response.orderId(),
                response.orderNumber(),
                response.checkoutId(),
                response.paymentId(),
                response.sellerId(),
                response.memberId(),
                response.status(),
                itemResponses,
                response.receiverName(),
                response.receiverPhone(),
                response.address(),
                response.addressDetail(),
                response.zipCode(),
                response.memo(),
                response.totalItemAmount(),
                response.shippingFee(),
                response.totalAmount(),
                response.orderedAt(),
                response.confirmedAt(),
                response.shippedAt(),
                response.deliveredAt(),
                response.completedAt(),
                response.cancelledAt());
    }
}
