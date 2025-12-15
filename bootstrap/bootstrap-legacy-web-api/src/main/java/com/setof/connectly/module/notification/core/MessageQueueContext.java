package com.setof.connectly.module.notification.core;

import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.MessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MessageQueueContext implements MessageContext {

    private AlimTalkTemplateCode alimTalkTemplateCode;

    private MessageStatus messageStatus;
    private String parameters;

    @Override
    public int hashCode() {
        return (alimTalkTemplateCode.name() + parameters).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageQueueContext) {
            MessageQueueContext o = (MessageQueueContext) obj;
            return this.hashCode() == o.hashCode();
        }
        return false;
    }
}
