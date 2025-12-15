package com.setof.connectly.module.notification.dto.order;

import com.setof.connectly.module.notification.dto.AbstractAlimTalkMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderCancelNotifyMessage extends AbstractAlimTalkMessage {

    private long orderId;
    private long productGroupId;
    private String productGroupName;
    private long orderAmount;
    private String orderCancelReason;
    private String orderCancelReasonDetail;
}
