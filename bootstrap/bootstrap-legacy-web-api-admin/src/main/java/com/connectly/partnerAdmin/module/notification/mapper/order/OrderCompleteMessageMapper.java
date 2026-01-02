package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderCompleteMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCompleteMessageMapper implements AlimTalkMessageMapper<OrderCompleteMessage, ProductOrderSheet> {


    @Override
    public OrderCompleteMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderCompleteMessage.builder()
                .paymentId(productOrderSheet.getPaymentId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .paymentAmount(productOrderSheet.getPaymentAmount().toPlainStringWithoutDecimal())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.ORDER_COMPLETE;
    }
}
