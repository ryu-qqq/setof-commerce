package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import java.util.Set;

public interface AlimTalkService<T> {

    void sendAlimTalk(T t);

    void saveMessageQueueContexts(Set<MessageQueueContext> messageQueues);
}
