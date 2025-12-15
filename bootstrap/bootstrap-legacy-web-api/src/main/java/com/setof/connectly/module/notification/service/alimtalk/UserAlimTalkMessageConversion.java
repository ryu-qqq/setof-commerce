package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.MessageStatus;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageProvider;
import com.setof.connectly.module.user.entity.Users;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserAlimTalkMessageConversion extends AlimTalkMessageConversion<Users> {

    public UserAlimTalkMessageConversion(
            AlimTalkMessageProvider<? extends AlimTalkMessage, Users> alimTalkMessageProvider) {
        super(alimTalkMessageProvider);
    }

    @Override
    public List<MessageQueueContext> convert(Users users) {
        AlimTalkMessageMapper<? extends AlimTalkMessage, Users> mapperProvider =
                getMapperProvider(AlimTalkTemplateCode.MEMBER_JOIN);
        AlimTalkMessage alimTalkMessage = mapperProvider.toAlimTalkMessage(users);
        String parameter = toJson(alimTalkMessage);
        MessageQueueContext messageQueueContext =
                new MessageQueueContext(
                        AlimTalkTemplateCode.MEMBER_JOIN, MessageStatus.PENDING, parameter);

        return Collections.singletonList(messageQueueContext);
    }
}
