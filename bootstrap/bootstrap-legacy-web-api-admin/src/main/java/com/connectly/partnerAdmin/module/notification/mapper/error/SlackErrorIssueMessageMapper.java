package com.connectly.partnerAdmin.module.notification.mapper.error;


import com.connectly.partnerAdmin.module.notification.dto.error.SlackErrorIssueMessage;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class SlackErrorIssueMessageMapper implements SlackMessageMapper<SlackErrorIssueMessage, Exception> {


    @Override
    public SlackErrorIssueMessage toSlackMessage(Exception e) {
        return SlackErrorIssueMessage.builder()
                .exception(e)
                .build();
    }

    @Override
    public SlackTemplateCode getSlackTemplateCode() {
        return SlackTemplateCode.ERROR;
    }
}
