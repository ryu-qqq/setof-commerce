package com.connectly.partnerAdmin.module.notification.service.alimtalk;



import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;
import com.connectly.partnerAdmin.module.notification.repository.MessageQueueJdbcRepository;
import com.connectly.partnerAdmin.module.user.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserAlimTalkService extends AbstractAlimTalkService<Users>{

    private final UserAlimTalkMessageConversion userAlimTalkMessageConversion;


    public UserAlimTalkService(MessageQueueJdbcRepository messageQueueJdbcRepository, UserAlimTalkMessageConversion userAlimTalkMessageConversion) {
        super(messageQueueJdbcRepository);
        this.userAlimTalkMessageConversion = userAlimTalkMessageConversion;
    }


    @Override
    public void sendAlimTalk(Users users) {
        List<MessageQueueContext> messageQueueContexts = userAlimTalkMessageConversion.convert(users);
        saveMessageQueueContexts(new HashSet<>(messageQueueContexts));
    }

}
