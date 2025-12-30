package com.connectly.partnerAdmin.module.notification.dto.error;

import com.connectly.partnerAdmin.module.notification.service.slack.SlackMessage;
import lombok.*;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    private String buildErrorMessage(){
        return "Error :" + exception.getClass().getSimpleName()  + "\n" +
                "Error Msg :" + (exception.getMessage() != null ? exception.getMessage() : "");
    }



}
