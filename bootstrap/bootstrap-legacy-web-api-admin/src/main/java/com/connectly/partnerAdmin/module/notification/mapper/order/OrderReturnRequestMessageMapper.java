package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderReturnRequestMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnRequestMessageMapper implements AlimTalkMessageMapper<OrderReturnRequestMessage, ProductOrderSheet> {

    @Override
    public OrderReturnRequestMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderReturnRequestMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.RETURN_REQUEST;
    }
}
