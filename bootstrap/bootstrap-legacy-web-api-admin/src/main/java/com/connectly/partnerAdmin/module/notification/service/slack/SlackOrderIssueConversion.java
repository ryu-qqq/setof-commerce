package com.connectly.partnerAdmin.module.notification.service.slack;

import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageProvider;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SlackOrderIssueConversion extends SlackMessageConversion<PaymentResponse>{

    public SlackOrderIssueConversion(SlackMessageProvider<? extends SlackMessage, PaymentResponse> slackMessageProvider) {
        super(slackMessageProvider);
    }

    @Override
    public List<BaseMessageContext> convert(PaymentResponse paymentResponse) {
        SlackMessageMapper<? extends SlackMessage, PaymentResponse> mapperProvider = getMapperProvider(SlackTemplateCode.ORDER_COMPLETED);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(paymentResponse);
        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));
        return Collections.singletonList(baseMessageContext);
    }

}
