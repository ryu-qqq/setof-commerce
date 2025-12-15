package com.setof.connectly.module.notification.service.slack;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;

public interface SlackNotificationService<T> {

    void sendSlackMessage(T t);

    void sendSlackNotification(ChatPostMessageRequest request);
}
