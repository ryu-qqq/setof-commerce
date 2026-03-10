package com.ryuqq.setof.adapter.out.persistence.notification;

import com.ryuqq.setof.application.notification.port.out.query.NotificationOutboxQueryPort;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import com.ryuqq.setof.domain.notification.id.NotificationOutboxId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class NotificationOutboxQueryAdapter implements NotificationOutboxQueryPort {

    @Override
    public Optional<NotificationOutbox> findById(NotificationOutboxId id) {
        return Optional.empty();
    }

    @Override
    public List<NotificationOutbox> findPending(int limit) {
        return List.of();
    }

    @Override
    public List<NotificationOutbox> findStuckPublished(Instant threshold, int limit) {
        return List.of();
    }
}
