package com.setof.connectly.module.notification.dto.order;

import com.setof.connectly.module.notification.dto.AbstractAlimTalkMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderReturnRejectMessage extends AbstractAlimTalkMessage {

    private long orderId;
    private String productGroupName;
    private String returnRejectReason;
}
