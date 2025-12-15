package com.setof.connectly.module.notification.mapper.order;

import com.setof.connectly.module.notification.dto.order.OrderCancelCompleteMessage;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelCompleteMapper
        implements AlimTalkMessageMapper<OrderCancelCompleteMessage, ProductOrderSheet> {

    @Override
    public OrderCancelCompleteMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderCancelCompleteMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CANCEL_COMPLETE;
    }
}
