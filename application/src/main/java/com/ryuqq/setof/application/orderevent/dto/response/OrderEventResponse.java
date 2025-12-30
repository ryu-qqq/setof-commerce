package com.ryuqq.setof.application.orderevent.dto.response;

import java.time.Instant;
import java.util.Map;

/**
 * OrderEventResponse - 주문 이벤트 응답 DTO
 *
 * @param id 이벤트 ID
 * @param orderId 주문 ID
 * @param eventType 이벤트 타입
 * @param eventSource 이벤트 출처
 * @param sourceId 출처 ID
 * @param previousStatus 이전 상태
 * @param currentStatus 현재 상태
 * @param actorType 수행자 타입
 * @param actorId 수행자 ID
 * @param description 설명
 * @param metadata 메타데이터
 * @param createdAt 생성 일시
 * @author development-team
 * @since 2.0.0
 */
public record OrderEventResponse(
        Long id,
        String orderId,
        String eventType,
        String eventSource,
        String sourceId,
        String previousStatus,
        String currentStatus,
        String actorType,
        String actorId,
        String description,
        Map<String, Object> metadata,
        Instant createdAt) {}
