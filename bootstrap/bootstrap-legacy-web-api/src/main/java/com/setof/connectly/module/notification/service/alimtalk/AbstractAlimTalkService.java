package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.repository.MessageQueueJdbcRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractAlimTalkService<T> implements AlimTalkService<T> {

    private final MessageQueueJdbcRepository messageQueueJdbcRepository;

    @Override
    public void saveMessageQueueContexts(Set<MessageQueueContext> messageQueues) {
        messageQueueJdbcRepository.saveAll(messageQueues);
    }
}
