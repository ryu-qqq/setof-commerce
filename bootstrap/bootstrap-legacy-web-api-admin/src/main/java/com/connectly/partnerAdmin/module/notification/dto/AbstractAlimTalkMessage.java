package com.connectly.partnerAdmin.module.notification.dto;

import com.connectly.partnerAdmin.module.notification.service.alimtalk.AlimTalkMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractAlimTalkMessage implements AlimTalkMessage {
    private String recipientNo;
    @Override
    public void setRecipientNo(String recipientNo) {
        this.recipientNo = recipientNo;
    }
}
