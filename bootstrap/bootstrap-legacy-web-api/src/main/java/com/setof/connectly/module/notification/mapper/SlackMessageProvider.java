package com.setof.connectly.module.notification.mapper;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.service.slack.SlackMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SlackMessageProvider<T extends SlackMessage, R>
        extends AbstractProvider<SlackTemplateCode, SlackMessageMapper<T, R>> {

    public SlackMessageProvider(List<SlackMessageMapper<T, R>> mappers) {
        for (SlackMessageMapper<T, R> mapper : mappers) {
            map.put(mapper.getSlackTemplateCode(), mapper);
        }
    }
}
