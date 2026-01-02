package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderAcceptMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderAcceptMessageMapper implements AlimTalkMessageMapper<OrderAcceptMessage, ProductOrderSheet> {

    @Override
    public OrderAcceptMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderAcceptMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .productGroupId(productOrderSheet.getProductGroupId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .orderAmount(productOrderSheet.getOrderAmount().toPlainStringWithoutDecimal())
                .recipientNo(productOrderSheet.getCsPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.ORDER_ACCEPT;
    }

}
