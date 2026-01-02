package com.connectly.partnerAdmin.module.notification.mapper.order;

import com.connectly.partnerAdmin.module.notification.dto.order.VBankCancelMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class VBankCancelMessageMapper implements AlimTalkMessageMapper<VBankCancelMessage, ProductOrderSheet> {

    @Override
    public VBankCancelMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return VBankCancelMessage.builder()
                .paymentId(productOrderSheet.getOrderId())
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CANCEL_VCOMPLETE;
    }
}
