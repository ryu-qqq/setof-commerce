package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderReturnNotifyMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnNotifyMessageMapper implements AlimTalkMessageMapper<OrderReturnNotifyMessage, ProductOrderSheet> {
    @Override
    public OrderReturnNotifyMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderReturnNotifyMessage.builder()
                .orderId(productOrderSheet.getOrderId())
                .orderAmount(productOrderSheet.getOrderAmount().toPlainStringWithoutDecimal())
                .productGroupId(productOrderSheet.getProductGroupId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .returnReason(productOrderSheet.getReason())
                .returnDetailReasonDetail(productOrderSheet.getDetailReason())
                .recipientNo(productOrderSheet.getCsPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.RETURN_REQUEST_S;
    }
}
