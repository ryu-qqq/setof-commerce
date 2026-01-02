package com.ryuqq.setof.application.order.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 주문 응답 DTO
 *
 * @param orderId 주문 ID (UUID String)
 * @param orderNumber 주문 번호
 * @param checkoutId 체크아웃 ID (UUID String)
 * @param paymentId 결제 ID (UUID String)
 * @param sellerId 판매자 ID
 * @param memberId 회원 ID (UUIDv7 String)
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
 * @since 1.0.0
 */
public record OrderResponse(
        String orderId,
        String orderNumber,
        String checkoutId,
        String paymentId,
        Long sellerId,
        String memberId,
        String status,
        List<OrderItemResponse> items,
        String receiverName,
        String receiverPhone,
        String address,
        String addressDetail,
        String zipCode,
        String memo,
        BigDecimal totalItemAmount,
        BigDecimal shippingFee,
        BigDecimal totalAmount,
        Instant orderedAt,
        Instant confirmedAt,
        Instant shippedAt,
        Instant deliveredAt,
        Instant completedAt,
        Instant cancelledAt) {}
