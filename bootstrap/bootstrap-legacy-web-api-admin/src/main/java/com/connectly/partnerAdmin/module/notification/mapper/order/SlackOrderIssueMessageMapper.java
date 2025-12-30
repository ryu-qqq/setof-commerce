package com.connectly.partnerAdmin.module.notification.mapper.order;


import com.connectly.partnerAdmin.module.notification.dto.order.SlackOrderIssueMessage;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class SlackOrderIssueMessageMapper implements SlackMessageMapper<SlackOrderIssueMessage, PaymentResponse> {

    //Todo::
    @Override
    public SlackOrderIssueMessage toSlackMessage(PaymentResponse paymentResponse) {
        return SlackOrderIssueMessage.builder()
                .orderProducts(paymentResponse.getOrderProducts())
                .paymentDetail(paymentResponse.getPayment())
                .buyerInfo(paymentResponse.getBuyerInfo())
//                .salePriceAmount(paymentResponse.getSalePriceAmount())
//                .totalSalePrice(paymentResponse.getTotalSaleAmount())
                .usedMileageAmount(paymentResponse.getPayment().getUsedMileageAmount().toPlainStringWithoutDecimal())
                .build();
    }

    @Override
    public SlackTemplateCode getSlackTemplateCode() {
        return SlackTemplateCode.ORDER_COMPLETED;
    }
}
