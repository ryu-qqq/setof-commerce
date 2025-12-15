package com.setof.connectly.module.notification.repository;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import java.util.Set;

public interface MessageQueueJdbcRepository {

    void saveAll(Set<MessageQueueContext> messageQueues);
}
