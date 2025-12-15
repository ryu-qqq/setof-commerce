package com.setof.connectly.module.notification.mapper.order;

import com.setof.connectly.module.notification.dto.order.OrderReturnRequestMessage;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelRequestMessageMapper
        implements AlimTalkMessageMapper<OrderReturnRequestMessage, ProductOrderSheet> {

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
        return AlimTalkTemplateCode.CANCEL_REQUEST;
    }
}
