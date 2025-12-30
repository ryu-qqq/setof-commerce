package com.ryuqq.setof.application.orderevent.dto.response;

import java.util.List;

/**
 * OrderTimelineResponse - 주문 타임라인 응답 DTO
 *
 * @param orderId 주문 ID
 * @param events 이벤트 목록 (시간순)
 * @author development-team
 * @since 2.0.0
 */
public record OrderTimelineResponse(String orderId, List<OrderEventResponse> events) {}
