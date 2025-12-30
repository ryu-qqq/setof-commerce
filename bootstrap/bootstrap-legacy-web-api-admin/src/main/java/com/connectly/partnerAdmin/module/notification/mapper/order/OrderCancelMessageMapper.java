package com.connectly.partnerAdmin.module.notification.mapper.order;

import com.connectly.partnerAdmin.module.notification.dto.order.OrderCancelMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelMessageMapper implements AlimTalkMessageMapper<OrderCancelMessage, ProductOrderSheet> {


    @Override
    public OrderCancelMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderCancelMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .saleCancelReason(productOrderSheet.getFullReason())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CANCEL_SALE;
    }
}
