package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import com.setof.connectly.module.notification.mapper.SlackMessageProvider;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SlackOrderIssueConversion extends SlackMessageConversion<PaymentResponse> {

    public SlackOrderIssueConversion(
            SlackMessageProvider<? extends SlackMessage, PaymentResponse> slackMessageProvider) {
        super(slackMessageProvider);
    }

    @Override
    public List<BaseMessageContext> convert(PaymentResponse paymentResponse) {
        SlackMessageMapper<? extends SlackMessage, PaymentResponse> mapperProvider =
                getMapperProvider(SlackTemplateCode.ORDER_COMPLETED);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(paymentResponse);
        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));
        return Collections.singletonList(baseMessageContext);
    }
}
