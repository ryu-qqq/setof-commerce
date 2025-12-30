package com.ryuqq.setof.adapter.out.persistence.orderevent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * OrderEventJpaEntity - 주문 이벤트 JPA Entity
 *
 * <p>order_event 테이블과 매핑되는 JPA Entity입니다.
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Entity
@Table(
        name = "order_event",
        indexes = {
            @Index(name = "idx_order_event_order_id", columnList = "order_id"),
            @Index(name = "idx_order_event_source", columnList = "event_source, source_id"),
            @Index(name = "idx_order_event_created_at", columnList = "created_at")
        })
public class OrderEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "event_source", nullable = false, length = 20)
    private String eventSource;

    @Column(name = "source_id", length = 36)
    private String sourceId;

    @Column(name = "previous_status", length = 30)
    private String previousStatus;

    @Column(name = "current_status", length = 30)
    private String currentStatus;

    @Column(name = "actor_type", nullable = false, length = 20)
    private String actorType;

    @Column(name = "actor_id", length = 36)
    private String actorId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected OrderEventJpaEntity() {
        // JPA 기본 생성자
    }

    private OrderEventJpaEntity(
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
            String metadata,
            Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.sourceId = sourceId;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
        this.actorType = actorType;
        this.actorId = actorId;
        this.description = description;
        this.metadata = metadata;
        this.createdAt = createdAt;
    }

    public static OrderEventJpaEntity of(
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
            String metadata,
            Instant createdAt) {
        return new OrderEventJpaEntity(
                id,
                orderId,
                eventType,
                eventSource,
                sourceId,
                previousStatus,
                currentStatus,
                actorType,
                actorId,
                description,
                metadata,
                createdAt);
    }

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventSource() {
        return eventSource;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getActorType() {
        return actorType;
    }

    public String getActorId() {
        return actorId;
    }

    public String getDescription() {
        return description;
    }

    public String getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
