package com.ryuqq.setof.domain.orderevent.aggregate;

import com.ryuqq.setof.domain.orderevent.vo.OrderActorType;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventSource;
import com.ryuqq.setof.domain.orderevent.vo.OrderEventType;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * OrderEvent - 주문 이벤트 (통합 타임라인)
 *
 * <p>주문의 모든 상태 변경과 클레임 이력을 통합 관리하는 불변 객체입니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * OrderEvent event = OrderEvent.create(
 *     orderId,
 *     OrderEventType.ORDER_CONFIRMED,
 *     OrderEventSource.ORDER,
 *     null,
 *     "PENDING",
 *     "CONFIRMED",
 *     OrderActorType.ADMIN,
 *     adminId,
 *     "주문이 확정되었습니다"
 * );
 * }</pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - 모든 메서드 직접 구현
 *   <li>불변 객체 - 모든 필드 final
 *   <li>null 안전성 - 필수 필드 검증
 *   <li>Instant.now() 직접 호출 금지 - 파라미터로 시간 전달
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class OrderEvent {

    private final Long id;
    private final String orderId;
    private final OrderEventType eventType;
    private final OrderEventSource eventSource;
    private final String sourceId;
    private final String previousStatus;
    private final String currentStatus;
    private final OrderActorType actorType;
    private final String actorId;
    private final String description;
    private final Map<String, Object> metadata;
    private final Instant createdAt;

    private OrderEvent(
            Long id,
            String orderId,
            OrderEventType eventType,
            OrderEventSource eventSource,
            String sourceId,
            String previousStatus,
            String currentStatus,
            OrderActorType actorType,
            String actorId,
            String description,
            Map<String, Object> metadata,
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
        this.metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        this.createdAt = createdAt;
    }

    /**
     * 신규 OrderEvent 생성 (ID 없이)
     *
     * @param orderId 주문 ID
     * @param eventType 이벤트 타입
     * @param eventSource 이벤트 출처
     * @param sourceId 출처 ID (클레임 ID 등)
     * @param previousStatus 이전 상태
     * @param currentStatus 현재 상태
     * @param actorType 수행자 타입
     * @param actorId 수행자 ID
     * @param description 설명
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @return 신규 OrderEvent
     */
    public static OrderEvent create(
            String orderId,
            OrderEventType eventType,
            OrderEventSource eventSource,
            String sourceId,
            String previousStatus,
            String currentStatus,
            OrderActorType actorType,
            String actorId,
            String description,
            Instant now) {
        return create(
                orderId,
                eventType,
                eventSource,
                sourceId,
                previousStatus,
                currentStatus,
                actorType,
                actorId,
                description,
                null,
                now);
    }

    /**
     * 신규 OrderEvent 생성 (메타데이터 포함)
     *
     * @param orderId 주문 ID
     * @param eventType 이벤트 타입
     * @param eventSource 이벤트 출처
     * @param sourceId 출처 ID (클레임 ID 등)
     * @param previousStatus 이전 상태
     * @param currentStatus 현재 상태
     * @param actorType 수행자 타입
     * @param actorId 수행자 ID
     * @param description 설명
     * @param metadata 추가 메타데이터
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @return 신규 OrderEvent
     */
    public static OrderEvent create(
            String orderId,
            OrderEventType eventType,
            OrderEventSource eventSource,
            String sourceId,
            String previousStatus,
            String currentStatus,
            OrderActorType actorType,
            String actorId,
            String description,
            Map<String, Object> metadata,
            Instant now) {
        validateRequired(orderId, "orderId");
        validateRequired(eventType, "eventType");
        validateRequired(eventSource, "eventSource");
        validateRequired(actorType, "actorType");
        validateRequired(now, "now");

        String finalDescription =
                (description != null && !description.isBlank())
                        ? description
                        : eventType.defaultDescription();

        return new OrderEvent(
                null,
                orderId,
                eventType,
                eventSource,
                sourceId,
                previousStatus,
                currentStatus,
                actorType,
                actorId,
                finalDescription,
                metadata,
                now);
    }

    /**
     * 영속화된 OrderEvent 복원
     *
     * @param id DB ID
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
     * @param createdAt 생성 시각
     * @return 복원된 OrderEvent
     */
    public static OrderEvent restore(
            Long id,
            String orderId,
            OrderEventType eventType,
            OrderEventSource eventSource,
            String sourceId,
            String previousStatus,
            String currentStatus,
            OrderActorType actorType,
            String actorId,
            String description,
            Map<String, Object> metadata,
            Instant createdAt) {
        return new OrderEvent(
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

    // ========== Getters ==========

    public Long id() {
        return id;
    }

    public String orderId() {
        return orderId;
    }

    public OrderEventType eventType() {
        return eventType;
    }

    public OrderEventSource eventSource() {
        return eventSource;
    }

    public String sourceId() {
        return sourceId;
    }

    public String previousStatus() {
        return previousStatus;
    }

    public String currentStatus() {
        return currentStatus;
    }

    public OrderActorType actorType() {
        return actorType;
    }

    public String actorId() {
        return actorId;
    }

    public String description() {
        return description;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public Instant createdAt() {
        return createdAt;
    }

    // ========== Business Methods ==========

    /**
     * 신규 이벤트인지 확인 (아직 영속화되지 않음)
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * 클레임 관련 이벤트인지 확인
     *
     * @return 클레임 출처이면 true
     */
    public boolean isFromClaim() {
        return eventSource == OrderEventSource.CLAIM;
    }

    // ========== Validation ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        if (value instanceof String str && str.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }

    // ========== Object Methods ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        return Objects.equals(orderId, that.orderId)
                && eventType == that.eventType
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(orderId, eventType, createdAt);
    }

    @Override
    public String toString() {
        return "OrderEvent{"
                + "id="
                + id
                + ", orderId='"
                + orderId
                + '\''
                + ", eventType="
                + eventType
                + ", eventSource="
                + eventSource
                + ", actorType="
                + actorType
                + ", createdAt="
                + createdAt
                + '}';
    }
}
