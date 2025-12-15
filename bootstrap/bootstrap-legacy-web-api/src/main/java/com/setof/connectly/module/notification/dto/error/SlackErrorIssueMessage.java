package com.setof.connectly.module.notification.dto.error;

import com.setof.connectly.module.notification.service.slack.SlackMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SlackErrorIssueMessage implements SlackMessage {

    private Exception exception;

    @Override
    public String toString() {
        return buildErrorMessage();
    }

    private String buildErrorMessage() {
        return "Error :"
                + exception.getClass().getSimpleName()
                + "\n"
                + "Error Msg :"
                + (exception.getMessage() != null ? exception.getMessage() : "");
    }
}
