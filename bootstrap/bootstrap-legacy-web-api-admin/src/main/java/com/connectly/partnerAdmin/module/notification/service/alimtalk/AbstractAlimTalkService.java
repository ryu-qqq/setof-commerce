package com.connectly.partnerAdmin.module.notification.service.alimtalk;


import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;
import com.connectly.partnerAdmin.module.notification.repository.MessageQueueJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractAlimTalkService<T> implements AlimTalkService<T>{

    private final MessageQueueJdbcRepository messageQueueJdbcRepository;

    @Override
    public void saveMessageQueueContexts(Set<MessageQueueContext> messageQueues) {
        messageQueueJdbcRepository.saveAll(messageQueues);
    }

}
