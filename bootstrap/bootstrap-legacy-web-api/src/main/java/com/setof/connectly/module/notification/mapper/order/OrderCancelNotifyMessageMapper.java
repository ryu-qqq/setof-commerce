package com.setof.connectly.module.notification.mapper.order;

import com.setof.connectly.module.notification.dto.order.OrderCancelNotifyMessage;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelNotifyMessageMapper
        implements AlimTalkMessageMapper<OrderCancelNotifyMessage, ProductOrderSheet> {

    @Override
    public OrderCancelNotifyMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderCancelNotifyMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .productGroupId(productOrderSheet.getProductGroupId())
                .recipientNo(productOrderSheet.getCsPhoneNumber())
                .orderAmount(productOrderSheet.getOrderAmount())
                .orderCancelReason(productOrderSheet.getReason())
                .orderCancelReasonDetail(productOrderSheet.getDetailReason())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CANCEL_ORDER_S;
    }
}
