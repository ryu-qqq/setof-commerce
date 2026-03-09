package com.ryuqq.setof.storage.legacy.composite.order.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebOrderQueryDto - 레거시 주문 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param orderId 주문 ID
 * @param paymentId 결제 ID
 * @param productId 상품 ID
 * @param sellerId 셀러 ID
 * @param userId 사용자 ID
 * @param orderAmount 주문 금액
 * @param orderStatus 주문 상태 (문자열)
 * @param quantity 수량
 * @param reviewYn 리뷰 작성 여부 (Y/N)
 * @param insertDate 등록일시
 * @param updateDate 수정일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebOrderQueryDto(
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
        LocalDateTime updateDate) {}
