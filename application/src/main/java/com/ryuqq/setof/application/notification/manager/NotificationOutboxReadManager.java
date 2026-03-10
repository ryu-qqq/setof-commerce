package com.ryuqq.setof.application.notification.manager;

import com.ryuqq.setof.application.notification.port.out.query.NotificationOutboxQueryPort;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import com.ryuqq.setof.domain.notification.id.NotificationOutboxId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * NotificationOutboxReadManager - 알림 아웃박스 Read Manager.
 *
 * <p>APP-MGR-001: Manager는 @Component로 등록.
 *
 * <p>APP-MGR-002: ReadManager는 QueryPort만 의존.
 *
 * <p>APP-MGR-003: @Transactional(readOnly = true) 필수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NotificationOutboxReadManager {

    private final NotificationOutboxQueryPort queryPort;

    public NotificationOutboxReadManager(NotificationOutboxQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * ID로 아웃박스 조회.
     *
     * @param id 아웃박스 ID
     * @return 아웃박스 도메인 객체
     * @throws IllegalArgumentException 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public NotificationOutbox getById(NotificationOutboxId id) {
        return queryPort
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("알림 아웃박스를 찾을 수 없습니다. id=" + id.value()));
    }

    /**
     * PENDING 상태 아웃박스 목록 조회.
     *
     * @param limit 최대 조회 건수
     * @return PENDING 상태 아웃박스 목록
     */
    @Transactional(readOnly = true)
    public List<NotificationOutbox> findPending(int limit) {
        return queryPort.findPending(limit);
    }

    /**
     * Stuck 상태 (PUBLISHED에서 오래 머문) 아웃박스 목록 조회.
     *
     * @param threshold 기준 시각
     * @param limit 최대 조회 건수
     * @return stuck 아웃박스 목록
     */
    @Transactional(readOnly = true)
    public List<NotificationOutbox> findStuckPublished(Instant threshold, int limit) {
        return queryPort.findStuckPublished(threshold, limit);
    }
}
