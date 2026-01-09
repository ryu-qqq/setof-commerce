package com.ryuqq.setof.batch.legacy.notification.dto;

import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;

/**
 * 알림톡 발송 결과 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record AlimTalkSendResult(
        Long messageId, MessageStatus status, String responseCode, String responseMessage) {
    public static AlimTalkSendResult success(Long messageId) {
        return new AlimTalkSendResult(messageId, MessageStatus.SEND, "200", "SUCCESS");
    }

    public static AlimTalkSendResult failure(
            Long messageId, String responseCode, String responseMessage) {
        return new AlimTalkSendResult(
                messageId, MessageStatus.FAILED, responseCode, responseMessage);
    }

    public boolean isSuccess() {
        return status == MessageStatus.SEND;
    }
}
