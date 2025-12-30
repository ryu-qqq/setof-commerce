package com.connectly.partnerAdmin.module.notification.repository;


import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;
import com.connectly.partnerAdmin.module.notification.entity.MessageQueue;

import java.util.List;
import java.util.Set;

public interface MessageQueueJdbcRepository {

    void saveAll(Set<MessageQueueContext> messageQueues);
}
