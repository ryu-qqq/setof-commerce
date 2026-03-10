package com.ryuqq.setof.adapter.out.persistence.notification;

import com.ryuqq.setof.application.notification.port.out.command.NotificationOutboxCommandPort;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import org.springframework.stereotype.Component;

@Component
public class NotificationOutboxCommandAdapter implements NotificationOutboxCommandPort {

    @Override
    public void persist(NotificationOutbox outbox) {
        // TODO: 알림 아웃박스 테이블 생성 후 구현
        throw new UnsupportedOperationException(
                "NotificationOutbox persistence not yet implemented");
    }
}
