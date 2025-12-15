package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SlackOrderIssueService extends AbstractSlackNotificationService<Long> {

    @Value(value = "${slack.channel.monitor}")
    private String channel;

    private final PaymentFindService paymentFindService;

    private final SlackOrderIssueConversion slackOrderIssueConversion;

    public SlackOrderIssueService(
            MethodsClient methodsClient,
            PaymentFindService paymentFindService,
            SlackOrderIssueConversion slackOrderIssueConversion) {
        super(methodsClient);
        this.paymentFindService = paymentFindService;
        this.slackOrderIssueConversion = slackOrderIssueConversion;
    }

    @Override
    public void sendSlackMessage(Long paymentId) {
        PaymentResponse paymentResponse = paymentFindService.fetchPaymentForSlack(paymentId);
        List<BaseMessageContext> convert = slackOrderIssueConversion.convert(paymentResponse);

        convert.forEach(
                c -> {
                    ChatPostMessageRequest chatPostMessageRequest =
                            toChatPostMessageRequest(channel, c.getParameters());
                    sendSlackNotification(chatPostMessageRequest);
                });
    }
}
