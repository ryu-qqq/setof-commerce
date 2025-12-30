package com.connectly.partnerAdmin.module.notification.service.slack;


import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;
import com.connectly.partnerAdmin.module.payment.service.PaymentFetchService;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class SlackOrderIssueService extends AbstractSlackNotificationService<Long>{

    @Value(value = "${slack.channel.monitor}")
    private String channel;
    private final PaymentFetchService paymentFetchService;

    private final SlackOrderIssueConversion slackOrderIssueConversion;

    public SlackOrderIssueService(MethodsClient methodsClient, PaymentFetchService paymentFetchService, SlackOrderIssueConversion slackOrderIssueConversion) {
        super(methodsClient);
        this.paymentFetchService = paymentFetchService;
        this.slackOrderIssueConversion = slackOrderIssueConversion;
    }

    @Override
    public void sendSlackMessage(Long paymentId) {
        PaymentResponse paymentResponse = paymentFetchService.fetchPayment(paymentId);
        List<BaseMessageContext> convert = slackOrderIssueConversion.convert(paymentResponse);


        convert.forEach(c ->
                {
                    ChatPostMessageRequest chatPostMessageRequest = toChatPostMessageRequest(channel, c.getParameters());
                    sendSlackNotification(chatPostMessageRequest);
                }
        );


    }
}
