package com.ryuqq.setof.batch.legacy.order.dto;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;

/**
 * 주문 업데이트 결과 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record OrderUpdateResult(Long orderId, OrderStatus newStatus) {}
