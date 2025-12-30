package com.connectly.partnerAdmin.module.notification.mapper.qna;


import com.connectly.partnerAdmin.module.notification.dto.qna.SlackQnaIssueMessage;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQna;
import org.springframework.stereotype.Component;

@Component
public class SlackQnaMessageMapper implements SlackMessageMapper<SlackQnaIssueMessage, CreateQna> {
    @Override
    public SlackQnaIssueMessage toSlackMessage(CreateQna createQna) {
        return SlackQnaIssueMessage.builder()
                .qnaType(createQna.getQnaType().getName())
                .content(createQna.getQnaContents().getContent())
                .title(createQna.getQnaContents().getTitle())
                .build();

    }

    @Override
    public SlackTemplateCode getSlackTemplateCode() {
        return SlackTemplateCode.QNA_ISSUE;
    }

}
