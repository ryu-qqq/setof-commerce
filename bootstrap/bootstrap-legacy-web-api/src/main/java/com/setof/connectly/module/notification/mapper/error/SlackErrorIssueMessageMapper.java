package com.setof.connectly.module.notification.mapper.error;

import com.setof.connectly.module.notification.dto.error.SlackErrorIssueMessage;
import com.setof.connectly.module.notification.enums.SlackTemplateCode;
import com.setof.connectly.module.notification.mapper.SlackMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class SlackErrorIssueMessageMapper
        implements SlackMessageMapper<SlackErrorIssueMessage, Exception> {

    @Override
    public SlackErrorIssueMessage toSlackMessage(Exception e) {
        return SlackErrorIssueMessage.builder().exception(e).build();
    }

    @Override
    public SlackTemplateCode getSlackTemplateCode() {
        return SlackTemplateCode.ERROR;
    }
}
