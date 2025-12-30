package com.connectly.partnerAdmin.module.notification.dto.order;

import com.connectly.partnerAdmin.module.notification.dto.AbstractAlimTalkMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderReturnRejectMessage extends AbstractAlimTalkMessage {

    private long orderId;
    private String productGroupName;
    private String returnRejectReason;

}
