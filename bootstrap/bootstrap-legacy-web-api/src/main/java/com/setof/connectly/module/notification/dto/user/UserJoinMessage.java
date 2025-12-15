package com.setof.connectly.module.notification.dto.user;

import com.setof.connectly.module.notification.dto.AbstractAlimTalkMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class UserJoinMessage extends AbstractAlimTalkMessage {

    private String memberName;
}
