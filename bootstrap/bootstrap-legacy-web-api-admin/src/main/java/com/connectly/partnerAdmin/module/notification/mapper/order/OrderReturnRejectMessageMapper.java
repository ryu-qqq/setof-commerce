package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderReturnRejectMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnRejectMessageMapper implements AlimTalkMessageMapper<OrderReturnRejectMessage, ProductOrderSheet> {

    @Override
    public OrderReturnRejectMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderReturnRejectMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .returnRejectReason(productOrderSheet.getFullReason())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.RETURN_REJECT;
    }
}
