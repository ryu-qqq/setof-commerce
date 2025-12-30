package com.connectly.partnerAdmin.module.notification.dto.order;

import com.connectly.partnerAdmin.module.notification.dto.AbstractAlimTalkMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderCancelMessage extends AbstractAlimTalkMessage {

    private long orderId;
    private String productGroupName;
    private String saleCancelReason;

}
