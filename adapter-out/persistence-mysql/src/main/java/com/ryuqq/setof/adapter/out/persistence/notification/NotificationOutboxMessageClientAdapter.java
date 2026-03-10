package com.ryuqq.setof.adapter.out.persistence.notification;

import com.ryuqq.setof.application.notification.port.out.client.NotificationOutboxMessageClient;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import org.springframework.stereotype.Component;

@Component
public class NotificationOutboxMessageClientAdapter implements NotificationOutboxMessageClient {

    @Override
    public void publish(NotificationOutbox outbox) {
        // TODO: SQS 연동 후 구현
        throw new UnsupportedOperationException(
                "NotificationOutbox message publishing not yet implemented");
    }
}
