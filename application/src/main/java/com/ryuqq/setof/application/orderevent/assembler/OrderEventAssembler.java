package com.ryuqq.setof.application.orderevent.assembler;

import com.ryuqq.setof.application.orderevent.dto.response.OrderEventResponse;
import com.ryuqq.setof.application.orderevent.dto.response.OrderTimelineResponse;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * OrderEventAssembler - OrderEvent 응답 조립기
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class OrderEventAssembler {

    /**
     * Domain → Response 변환
     *
     * @param domain OrderEvent 도메인 객체
     * @return 응답 DTO
     */
    public OrderEventResponse toResponse(OrderEvent domain) {
        return new OrderEventResponse(
                domain.id(),
                domain.orderId(),
                domain.eventType().name(),
                domain.eventSource().name(),
                domain.sourceId(),
                domain.previousStatus(),
                domain.currentStatus(),
                domain.actorType().name(),
                domain.actorId(),
                domain.description(),
                domain.metadata(),
                domain.createdAt());
    }

    /**
     * Domain 목록 → 타임라인 응답 변환
     *
     * @param orderId 주문 ID
     * @param events OrderEvent 목록
     * @return 타임라인 응답 DTO
     */
    public OrderTimelineResponse toTimelineResponse(String orderId, List<OrderEvent> events) {
        List<OrderEventResponse> responses = events.stream().map(this::toResponse).toList();
        return new OrderTimelineResponse(orderId, responses);
    }
}
