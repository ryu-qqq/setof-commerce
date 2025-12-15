package com.setof.connectly.module.notification.service.slack;

import com.setof.connectly.module.notification.core.BaseMessageContext;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import com.setof.connectly.module.notification.mapper.SlackMessageProvider;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SlackQnaIssueConversion extends SlackMessageConversion<CreateQna> {

    public SlackQnaIssueConversion(
            SlackMessageProvider<? extends SlackMessage, CreateQna> slackMessageProvider) {
        super(slackMessageProvider);
    }

    @Override
    public List<BaseMessageContext> convert(CreateQna createQna) {
        SlackMessageMapper<? extends SlackMessage, CreateQna> mapperProvider =
                getMapperProvider(SlackTemplateCode.QNA_ISSUE);
        SlackMessage slackMessage = mapperProvider.toSlackMessage(createQna);

        BaseMessageContext baseMessageContext = new BaseMessageContext(toString(slackMessage));

        return Collections.singletonList(baseMessageContext);
    }
}
