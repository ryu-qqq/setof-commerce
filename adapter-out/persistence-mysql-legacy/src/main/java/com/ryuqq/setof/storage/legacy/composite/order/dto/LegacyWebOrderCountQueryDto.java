package com.ryuqq.setof.storage.legacy.composite.order.dto;

/**
 * LegacyWebOrderCountQueryDto - 주문 상태별 건수 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * @param orderStatus 주문 상태
 * @param count 해당 상태의 건수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebOrderCountQueryDto(String orderStatus, long count) {}
