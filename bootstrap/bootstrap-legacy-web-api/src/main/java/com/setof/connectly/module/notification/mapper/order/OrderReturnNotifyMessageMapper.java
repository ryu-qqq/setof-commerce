package com.setof.connectly.module.notification.mapper.order;

import com.setof.connectly.module.notification.dto.order.OrderReturnNotifyMessage;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnNotifyMessageMapper
        implements AlimTalkMessageMapper<OrderReturnNotifyMessage, ProductOrderSheet> {
    @Override
    public OrderReturnNotifyMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderReturnNotifyMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .orderAmount(productOrderSheet.getOrderAmount())
                .productGroupId(productOrderSheet.getProductGroupId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .returnReason(productOrderSheet.getReason())
                .returnDetailReason(productOrderSheet.getDetailReason())
                .recipientNo(productOrderSheet.getCsPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.RETURN_REQUEST_S;
    }
}
