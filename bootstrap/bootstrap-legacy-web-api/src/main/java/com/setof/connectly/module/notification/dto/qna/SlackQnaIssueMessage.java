package com.setof.connectly.module.notification.dto.qna;

import com.setof.connectly.module.notification.service.slack.SlackMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SlackQnaIssueMessage implements SlackMessage {

    private String qnaType;
    private String title;
    private String content;

    @Override
    public String toString() {
        return buildQnaIssueNotificationMessage();
    }

    private String buildQnaIssueNotificationMessage() {
        return "QNA 질문 유형 " + qnaType + "\n" + "질문 제목 : " + title + "\n" + "질문 내용 : " + content;
    }
}
