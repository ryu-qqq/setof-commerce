package com.ryuqq.setof.adapter.out.persistence.orderevent.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.persistence.orderevent.entity.OrderEventJpaEntity;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import com.ryuqq.setof.domain.orderevent.vo.OrderActorType;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventSource;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventType;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * OrderEventJpaEntityMapper - OrderEvent 엔티티 매퍼
 *
 * <p>Domain ↔ JPA Entity 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class OrderEventJpaEntityMapper {

    private final ObjectMapper objectMapper;

    public OrderEventJpaEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Domain → JPA Entity 변환
     *
     * @param domain OrderEvent 도메인 객체
     * @return JPA Entity
     */
    public OrderEventJpaEntity toEntity(OrderEvent domain) {
        return OrderEventJpaEntity.of(
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
                toJsonString(domain.metadata()),
                domain.createdAt());
    }

    /**
     * JPA Entity → Domain 변환
     *
     * @param entity JPA Entity
     * @return OrderEvent 도메인 객체
     */
    public OrderEvent toDomain(OrderEventJpaEntity entity) {
        return OrderEvent.restore(
                entity.getId(),
                entity.getOrderId(),
                OrderEventType.valueOf(entity.getEventType()),
                OrderEventSource.valueOf(entity.getEventSource()),
                entity.getSourceId(),
                entity.getPreviousStatus(),
                entity.getCurrentStatus(),
                OrderActorType.valueOf(entity.getActorType()),
                entity.getActorId(),
                entity.getDescription(),
                parseMetadata(entity.getMetadata()),
                entity.getCreatedAt());
    }

    private String toJsonString(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Map<String, Object> parseMetadata(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }
}
