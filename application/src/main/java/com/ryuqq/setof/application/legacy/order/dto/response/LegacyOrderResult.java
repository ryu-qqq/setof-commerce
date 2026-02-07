package com.ryuqq.setof.application.legacy.order.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyOrderResult - 레거시 주문 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param orderId 주문 ID
 * @param paymentId 결제 ID
 * @param productId 상품 ID
 * @param sellerId 셀러 ID
 * @param userId 사용자 ID
 * @param orderAmount 주문 금액
 * @param orderStatus 주문 상태
 * @param quantity 수량
 * @param reviewYn 리뷰 작성 여부 (Y/N)
 * @param insertDate 등록일시
 * @param updateDate 수정일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderResult(
        long orderId,
        long paymentId,
        long productId,
        long sellerId,
        long userId,
        long orderAmount,
        String orderStatus,
        int quantity,
        String reviewYn,
        LocalDateTime insertDate,
        LocalDateTime updateDate) {

    /**
     * 팩토리 메서드.
     *
     * @param orderId 주문 ID
     * @param paymentId 결제 ID
     * @param productId 상품 ID
     * @param sellerId 셀러 ID
     * @param userId 사용자 ID
     * @param orderAmount 주문 금액
     * @param orderStatus 주문 상태
     * @param quantity 수량
     * @param reviewYn 리뷰 작성 여부 (Y/N)
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     * @return LegacyOrderResult
     */
    public static LegacyOrderResult of(
            long orderId,
            long paymentId,
            long productId,
            long sellerId,
            long userId,
            long orderAmount,
            String orderStatus,
            int quantity,
            String reviewYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new LegacyOrderResult(
                orderId,
                paymentId,
                productId,
                sellerId,
                userId,
                orderAmount,
                orderStatus,
                quantity,
                reviewYn,
                insertDate,
                updateDate);
    }
}
