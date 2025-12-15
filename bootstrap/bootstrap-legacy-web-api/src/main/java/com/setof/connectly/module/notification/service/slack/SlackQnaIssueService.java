package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlackQnaIssueService extends AbstractSlackNotificationService<CreateQna> {

    @Value(value = "${slack.channel.monitor2}")
    private String channel;

    private final SlackQnaIssueConversion slackQnaIssueConversion;

    public SlackQnaIssueService(
            MethodsClient methodsClient, SlackQnaIssueConversion slackQnaIssueConversion) {
        super(methodsClient);
        this.slackQnaIssueConversion = slackQnaIssueConversion;
    }

    @Override
    public void sendSlackMessage(CreateQna createQna) {
        List<BaseMessageContext> convert = slackQnaIssueConversion.convert(createQna);
        convert.forEach(
                c -> {
                    ChatPostMessageRequest chatPostMessageRequest =
                            toChatPostMessageRequest(channel, c.getParameters());
                    sendSlackNotification(chatPostMessageRequest);
                });
    }
}
