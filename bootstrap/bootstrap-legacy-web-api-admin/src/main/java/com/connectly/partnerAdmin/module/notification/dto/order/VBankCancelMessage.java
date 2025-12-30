package com.connectly.partnerAdmin.module.notification.dto.order;

import com.connectly.partnerAdmin.module.notification.dto.AbstractAlimTalkMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class VBankCancelMessage extends AbstractAlimTalkMessage {

    private long paymentId;
    private String productGroupName;
}
