package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.notification.core.MessageConversion;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import com.setof.connectly.module.notification.mapper.SlackMessageProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class SlackMessageConversion<R> implements MessageConversion<R> {

    private final SlackMessageProvider<? extends SlackMessage, R> slackMessageProvider;

    public abstract List<BaseMessageContext> convert(R r);

    protected String toString(Object o) {
        return o.toString();
    }

    protected SlackMessageMapper<? extends SlackMessage, R> getMapperProvider(
            SlackTemplateCode slackTemplateCode) {
        return slackMessageProvider.get(slackTemplateCode);
    }
}
