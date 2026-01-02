package com.connectly.partnerAdmin.module.notification.service.alimtalk;


import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.enums.MessageStatus;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageProvider;
import com.connectly.partnerAdmin.module.user.entity.Users;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserAlimTalkMessageConversion extends AlimTalkMessageConversion<Users>{


    public UserAlimTalkMessageConversion(AlimTalkMessageProvider<? extends AlimTalkMessage, Users> alimTalkMessageProvider) {
        super(alimTalkMessageProvider);
    }

    @Override
    public List<MessageQueueContext> convert(Users users) {
        AlimTalkMessageMapper<? extends AlimTalkMessage, Users> mapperProvider = getMapperProvider(AlimTalkTemplateCode.MEMBER_JOIN);
        AlimTalkMessage alimTalkMessage = mapperProvider.toAlimTalkMessage(users);
        String parameter = toJson(alimTalkMessage);
        MessageQueueContext messageQueueContext = new MessageQueueContext(AlimTalkTemplateCode.MEMBER_JOIN, MessageStatus.PENDING, parameter);

        return Collections.singletonList(messageQueueContext);

    }
}
