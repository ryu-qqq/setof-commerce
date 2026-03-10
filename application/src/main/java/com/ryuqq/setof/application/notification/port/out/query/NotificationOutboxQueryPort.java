package com.ryuqq.setof.application.notification.port.out.query;

import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import com.ryuqq.setof.domain.notification.id.NotificationOutboxId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * NotificationOutboxQueryPort - 알림 아웃박스 Query Port.
 *
 * <p>APP-PRT-002: Query Port는 조회 메서드만 정의.
 *
 * <p>APP-PRT-003: Port는 Domain 객체만 반환 (DTO 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NotificationOutboxQueryPort {

    /**
     * ID로 아웃박스 조회.
     *
     * @param id 아웃박스 ID
     * @return 아웃박스 도메인 객체
     */
    Optional<NotificationOutbox> findById(NotificationOutboxId id);

    /**
     * PENDING 상태 아웃박스 목록 조회.
     *
     * @param limit 최대 조회 건수
     * @return PENDING 상태 아웃박스 목록 (createdAt ASC)
     */
    List<NotificationOutbox> findPending(int limit);

    /**
     * PUBLISHED 상태에서 일정 시간 이상 머문 아웃박스 목록 조회.
     *
     * @param threshold 기준 시각 (이 시각 이전에 PUBLISHED된 것)
     * @param limit 최대 조회 건수
     * @return stuck 아웃박스 목록
     */
    List<NotificationOutbox> findStuckPublished(Instant threshold, int limit);
}
