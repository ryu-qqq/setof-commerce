package com.connectly.partnerAdmin.module.notification.mapper;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlackMessageProvider <T extends SlackMessage, R> extends AbstractProvider<SlackTemplateCode, SlackMessageMapper<T, R>> {

    public SlackMessageProvider(List<SlackMessageMapper<T, R>> mappers) {
        for (SlackMessageMapper<T, R> mapper : mappers) {
            map.put(mapper.getSlackTemplateCode(), mapper);
        }
    }

}
