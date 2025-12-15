package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import com.setof.connectly.module.notification.mapper.SlackMessageProvider;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SlackErrorIssueConversion extends SlackMessageConversion<Exception> {

    public SlackErrorIssueConversion(
            SlackMessageProvider<? extends SlackMessage, Exception> slackMessageProvider) {
        super(slackMessageProvider);
    }

    @Override
    public List<BaseMessageContext> convert(Exception e) {

        SlackMessageMapper<? extends SlackMessage, Exception> mapperProvider =
                getMapperProvider(SlackTemplateCode.ERROR);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(e);

        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));
        return Collections.singletonList(baseMessageContext);
    }
}
