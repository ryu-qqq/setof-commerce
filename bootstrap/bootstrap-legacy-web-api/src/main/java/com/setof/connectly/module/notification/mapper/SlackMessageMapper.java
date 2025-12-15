package com.setof.connectly.module.notification.mapper;

import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.service.slack.SlackMessage;

public interface SlackMessageMapper<T extends SlackMessage, R> {
    T toSlackMessage(R r);

    SlackTemplateCode getSlackTemplateCode();
}
