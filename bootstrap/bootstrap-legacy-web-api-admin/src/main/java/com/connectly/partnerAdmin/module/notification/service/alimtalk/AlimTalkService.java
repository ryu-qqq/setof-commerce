package com.connectly.partnerAdmin.module.notification.service.alimtalk;


import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;

import java.util.Set;

public interface AlimTalkService <T>{

    void sendAlimTalk(T t);
    void saveMessageQueueContexts(Set<MessageQueueContext> messageQueues);

}
