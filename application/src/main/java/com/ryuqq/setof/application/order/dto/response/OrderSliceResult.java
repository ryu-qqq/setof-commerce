package com.ryuqq.setof.application.order.dto.response;

import java.util.List;

/**
 * OrderSliceResult - 주문 목록 슬라이스 Application Result DTO.
 *
 * <p>커서 기반 페이징 결과와 상태별 건수를 포함합니다.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * @param content 주문 상세 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param lastOrderId 마지막 주문 ID (커서)
 * @param orderCounts 상태별 건수 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record OrderSliceResult(
        List<OrderDetailResult> content,
        boolean hasNext,
        Long lastOrderId,
        List<OrderStatusCountResult> orderCounts) {}
