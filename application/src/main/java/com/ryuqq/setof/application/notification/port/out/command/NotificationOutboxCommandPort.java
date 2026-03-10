package com.ryuqq.setof.application.notification.port.out.command;

import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;

/**
 * NotificationOutboxCommandPort - 알림 아웃박스 Command Port.
 *
 * <p>APP-PRT-001: Command Port는 쓰기 메서드만 정의.
 *
 * <p>APP-PRT-003: Port는 Domain 객체만 반환 (DTO 금지).
 *
 * <p>persist만 제공. ID 유무에 따라 Adapter에서 INSERT/UPDATE 자동 판단.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NotificationOutboxCommandPort {

    /**
     * 알림 아웃박스 저장 (INSERT or UPDATE).
     *
     * <p>ID가 null이면 INSERT, 존재하면 UPDATE로 동작합니다.
     *
     * @param outbox 알림 아웃박스 도메인 객체
     */
    void persist(NotificationOutbox outbox);
}
