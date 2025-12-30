package com.connectly.partnerAdmin.module.notification.service.slack;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

@Service
public class SlackErrorIssueService extends AbstractSlackNotificationService<Exception>{

    @Value(value = "${slack.channel.monitor3}")
    private String channel;
    private final SlackErrorIssueConversion slackErrorIssueConversion;

    public SlackErrorIssueService(MethodsClient methodsClient, SlackErrorIssueConversion slackErrorIssueConversion) {
        super(methodsClient);
        this.slackErrorIssueConversion = slackErrorIssueConversion;
    }

    @Override
    public void sendSlackMessage(Exception e) {
        List<BaseMessageContext> convert = slackErrorIssueConversion.convert(e);
        convert.forEach(c ->
                {
                    ChatPostMessageRequest chatPostMessageRequest = toChatPostMessageRequest(channel, c.getParameters());
                    sendSlackNotification(chatPostMessageRequest);
                }
        );

    }
    public void sendMessage(String e) {
        ChatPostMessageRequest chatPostMessageRequest = toChatPostMessageRequest(channel, e);
        sendSlackNotification(chatPostMessageRequest);
    }
}
