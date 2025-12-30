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
public class OrderReturnNotifyMessage extends AbstractAlimTalkMessage {

    private long orderId;
    private long productGroupId;
    private String productGroupName;
    private long orderAmount;
    private String returnReason;
    private String returnDetailReasonDetail;


}
