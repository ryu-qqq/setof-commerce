package com.setof.connectly.module.notification.dto.qna;

import com.setof.connectly.module.notification.dto.AbstractAlimTalkMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractQnaMessage extends AbstractAlimTalkMessage {

    private String questionType;
    private String productGroupName;
}
