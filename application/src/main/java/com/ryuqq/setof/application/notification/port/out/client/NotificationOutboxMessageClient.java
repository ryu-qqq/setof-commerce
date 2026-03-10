package com.ryuqq.setof.application.notification.port.out.client;

import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;

/**
 * NotificationOutboxMessageClient - 알림 아웃박스 메시지 발행 클라이언트 포트.
 *
 * <p>스케줄러가 PENDING 아웃박스를 SQS로 발행할 때 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NotificationOutboxMessageClient {

    /**
     * 아웃박스 메시지를 SQS로 발행.
     *
     * @param outbox 발행할 아웃박스
     */
    void publish(NotificationOutbox outbox);
}
