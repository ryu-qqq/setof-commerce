package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.repository.MessageQueueJdbcRepository;
import com.setof.connectly.module.user.entity.Users;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAlimTalkService extends AbstractAlimTalkService<Users> {

    private final UserAlimTalkMessageConversion userAlimTalkMessageConversion;

    public UserAlimTalkService(
            MessageQueueJdbcRepository messageQueueJdbcRepository,
            UserAlimTalkMessageConversion userAlimTalkMessageConversion) {
        super(messageQueueJdbcRepository);
        this.userAlimTalkMessageConversion = userAlimTalkMessageConversion;
    }

    @Override
    public void sendAlimTalk(Users users) {
        List<MessageQueueContext> messageQueueContexts =
                userAlimTalkMessageConversion.convert(users);
        saveMessageQueueContexts(new HashSet<>(messageQueueContexts));
    }
}
