package com.connectly.partnerAdmin.module.notification.mapper;


import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackMessage;

public interface SlackMessageMapper <T extends SlackMessage, R>{
    T toSlackMessage(R r);
    SlackTemplateCode getSlackTemplateCode();
}
