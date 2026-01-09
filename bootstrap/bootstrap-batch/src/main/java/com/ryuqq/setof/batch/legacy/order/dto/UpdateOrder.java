package com.ryuqq.setof.batch.legacy.order.dto;

import java.time.LocalDateTime;

/**
 * 주문 업데이트 정보 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record UpdateOrder(Long orderId, String orderStatus, LocalDateTime settlementDate) {}
