package com.connectly.partnerAdmin.module.notification.mapper.order;

import com.connectly.partnerAdmin.module.notification.dto.order.DeliveryStartMessage;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class DeliveryStartMessageMapper implements AlimTalkMessageMapper<DeliveryStartMessage, ProductOrderSheet> {


    @Override
    public DeliveryStartMessage toAlimTalkMessage(ProductOrderSheet productOrderSheet) {
        return DeliveryStartMessage.builder()
                .productGroupName(productOrderSheet.getSubStringProductGroupName())
                .recipientNo(productOrderSheet.getPhoneNumber())
                .deliveryCompany(productOrderSheet.getShipmentCompanyCode().getDisplayName())
                .invoiceNo(productOrderSheet.getInvoice())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.DELIVERY_START;
    }


}
