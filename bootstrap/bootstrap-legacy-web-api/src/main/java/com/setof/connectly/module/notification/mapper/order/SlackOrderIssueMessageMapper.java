package com.setof.connectly.module.notification.mapper.order;

import com.setof.connectly.module.notification.dto.order.SlackOrderIssueMessage;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class SlackOrderIssueMessageMapper
        implements SlackMessageMapper<SlackOrderIssueMessage, PaymentResponse> {

    @Override
    public SlackOrderIssueMessage toSlackMessage(PaymentResponse paymentResponse) {
        return SlackOrderIssueMessage.builder()
                .orderProducts(paymentResponse.getOrderProducts())
                .paymentDetail(paymentResponse.getPayment())
                .buyerInfo(paymentResponse.getBuyerInfo())
                .salePriceAmount(paymentResponse.getSalePriceAmount())
                .totalSalePrice(paymentResponse.getTotalSaleAmount())
                .usedMileageAmount(paymentResponse.getPayment().getUsedMileageAmount())
                .build();
    }

    @Override
    public SlackTemplateCode getSlackTemplateCode() {
        return SlackTemplateCode.ORDER_COMPLETED;
    }
}
