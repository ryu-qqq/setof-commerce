package com.setof.connectly.module.notification.mapper.user;

import com.setof.connectly.module.notification.dto.user.UserJoinMessage;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import com.setof.connectly.module.user.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserJoinMapper implements AlimTalkMessageMapper<UserJoinMessage, Users> {

    @Override
    public UserJoinMessage toAlimTalkMessage(Users users) {
        return UserJoinMessage.builder()
                .memberName(users.getName())
                .recipientNo(users.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.MEMBER_JOIN;
    }
}
