package com.connectly.partnerAdmin.module.notification.dto.order;

import com.connectly.partnerAdmin.module.notification.dto.AbstractAlimTalkMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderCancelNotifyMessage extends AbstractAlimTalkMessage {

    private long paymentId;
    private long productGroupId;

    private String vBankName;
    private String vBankNum;
    private String vBankHolder;
    private long vBankAmount;
    private LocalDateTime vBankDate;


}
