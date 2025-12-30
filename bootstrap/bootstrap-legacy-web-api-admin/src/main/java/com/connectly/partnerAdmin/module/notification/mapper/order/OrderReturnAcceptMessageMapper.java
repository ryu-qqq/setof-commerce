package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderReturnAcceptMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnAcceptMessageMapper implements AlimTalkMessageMapper<OrderReturnAcceptMessage, ProductOrderSheet> {

    @Override
    public OrderReturnAcceptMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderReturnAcceptMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.RETURN_ACCEPT;
    }
}
