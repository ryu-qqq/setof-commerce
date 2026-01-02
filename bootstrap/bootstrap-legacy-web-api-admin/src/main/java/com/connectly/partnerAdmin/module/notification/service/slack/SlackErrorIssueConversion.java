package com.connectly.partnerAdmin.module.notification.service.slack;

import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SlackErrorIssueConversion extends SlackMessageConversion<Exception>{


    public SlackErrorIssueConversion(SlackMessageProvider<? extends SlackMessage, Exception> slackMessageProvider) {
        super(slackMessageProvider);
    }

    @Override
    public List<BaseMessageContext> convert(Exception e) {

        SlackMessageMapper<? extends SlackMessage, Exception> mapperProvider = getMapperProvider(SlackTemplateCode.ERROR);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(e);

        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));
        return Collections.singletonList(baseMessageContext);
    }
}
