package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.OrderCancelNotifyMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelNotifyMapper implements AlimTalkMessageMapper<OrderCancelNotifyMessage, ProductOrderSheet> {

    @Override
    public OrderCancelNotifyMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return OrderCancelNotifyMessage.builder()
                .paymentId(productOrderSheet.getPaymentId())
                .productGroupId(productOrderSheet.getProductGroupId())
                .vBankName(productOrderSheet.getVBankName())
                .vBankAmount(productOrderSheet.getVBankAmount().byteValueExact())
                .vBankDate(productOrderSheet.getVBankDate())
                .vBankHolder(productOrderSheet.getVBankHolder())
                .vBankNum(productOrderSheet.getVBankNum())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CANCEL_NOTIFY;
    }


}
