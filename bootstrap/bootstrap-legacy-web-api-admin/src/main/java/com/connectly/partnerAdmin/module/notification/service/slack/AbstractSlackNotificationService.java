package com.connectly.partnerAdmin.module.notification.service.slack;


import com.connectly.partnerAdmin.module.notification.exception.SlackNotificationException;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public abstract class AbstractSlackNotificationService <T> implements SlackNotificationService<T> {

    @Value(value = "${slack.token}")
    private String token;

    private final MethodsClient methodsClient;

    @Override
    public void sendSlackNotification(ChatPostMessageRequest request) {
        try {
            methodsClient.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new SlackNotificationException(e.getMessage());
        }
    }

    protected String toJson(Object o){
        return JsonUtils.toJson(o);
    }

    protected ChatPostMessageRequest toChatPostMessageRequest(String channel, String notificationMessage){
        return ChatPostMessageRequest.builder()
                .token(token)
                .channel(channel)
                .text(notificationMessage)
                .build();
    }

}
