package com.ryuqq.setof.application.orderevent.dto.command;

import com.ryuqq.setof.domain.orderevent.vo.OrderActorType;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventSource;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventType;
import java.util.Map;

/**
 * RecordOrderEventCommand - 주문 이벤트 기록 Command
 *
 * @param orderId 주문 ID
 * @param eventType 이벤트 타입
 * @param eventSource 이벤트 출처
 * @param sourceId 출처 ID (클레임 ID 등, nullable)
 * @param previousStatus 이전 상태
 * @param currentStatus 현재 상태
 * @param actorType 수행자 타입
 * @param actorId 수행자 ID
 * @param description 설명
 * @param metadata 메타데이터
 * @author development-team
 * @since 2.0.0
 */
public record RecordOrderEventCommand(
        String orderId,
        OrderEventType eventType,
        OrderEventSource eventSource,
        String sourceId,
        String previousStatus,
        String currentStatus,
        OrderActorType actorType,
        String actorId,
        String description,
        Map<String, Object> metadata) {

    /** 주문 이벤트 생성용 간편 팩토리 */
    public static RecordOrderEventCommand forOrder(
            String orderId,
            OrderEventType eventType,
            String previousStatus,
            String currentStatus,
            OrderActorType actorType,
            String actorId,
            String description) {
        return new RecordOrderEventCommand(
                orderId,
                eventType,
                OrderEventSource.ORDER,
                null,
                previousStatus,
                currentStatus,
                actorType,
                actorId,
                description,
                null);
    }

    /** 클레임 이벤트 생성용 간편 팩토리 */
    public static RecordOrderEventCommand forClaim(
            String orderId,
            String claimId,
            OrderEventType eventType,
            OrderActorType actorType,
            String actorId,
            String description) {
        return new RecordOrderEventCommand(
                orderId,
                eventType,
                OrderEventSource.CLAIM,
                claimId,
                null,
                null,
                actorType,
                actorId,
                description,
                null);
    }

    /**
     * String 기반 팩토리 (REST API Layer용)
     *
     * <p>REST API Layer에서 Domain VO를 직접 의존하지 않도록 String으로 파라미터를 받아 변환합니다.
     *
     * @param orderId 주문 ID
     * @param eventType 이벤트 타입 (String)
     * @param eventSource 이벤트 출처 (String)
     * @param sourceId 출처 ID
     * @param actorType 수행자 타입 (String)
     * @param actorId 수행자 ID
     * @param description 설명
     * @return RecordOrderEventCommand
     */
    public static RecordOrderEventCommand ofStrings(
            String orderId,
            String eventType,
            String eventSource,
            String sourceId,
            String actorType,
            String actorId,
            String description) {
        return new RecordOrderEventCommand(
                orderId,
                OrderEventType.valueOf(eventType),
                OrderEventSource.valueOf(eventSource),
                sourceId,
                null,
                null,
                OrderActorType.valueOf(actorType),
                actorId,
                description,
                null);
    }
}
