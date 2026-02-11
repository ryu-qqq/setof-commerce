package com.ryuqq.setof.storage.legacy.composite.web.order.dto;

/**
 * LegacyWebOrderStatusCountDto - 상태별 주문 개수 DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * @param orderStatus 주문 상태
 * @param count 개수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebOrderStatusCountDto(String orderStatus, long count) {}
