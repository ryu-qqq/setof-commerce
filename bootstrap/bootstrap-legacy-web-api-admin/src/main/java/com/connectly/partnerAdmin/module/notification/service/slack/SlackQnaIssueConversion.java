package com.connectly.partnerAdmin.module.notification.service.slack;

import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageProvider;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQna;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SlackQnaIssueConversion extends SlackMessageConversion<CreateQna>{

    public SlackQnaIssueConversion(SlackMessageProvider<? extends SlackMessage, CreateQna> slackMessageProvider) {
        super(slackMessageProvider);
    }


    @Override
    public List<BaseMessageContext> convert(CreateQna createQna) {
        SlackMessageMapper<? extends SlackMessage, CreateQna> mapperProvider = getMapperProvider(SlackTemplateCode.QNA_ISSUE);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(createQna);

        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));

        return Collections.singletonList(baseMessageContext);
    }
}
