package com.ryuqq.setof.batch.legacy.notification.dto;

import com.ryuqq.setof.batch.legacy.notification.enums.AlimTalkTemplateCode;
import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;
import java.time.LocalDateTime;

/**
 * 메시지 큐 아이템 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record MessageQueueItem(
        Long messageId,
        Long orderId,
        String phoneNumber,
        AlimTalkTemplateCode templateCode,
        String templateVariables,
        MessageStatus status,
        LocalDateTime createdAt) {
    public MessageQueueItem(
            Long messageId,
            Long orderId,
            String phoneNumber,
            String templateCode,
            String templateVariables,
            String status,
            LocalDateTime createdAt) {
        this(
                messageId,
                orderId,
                phoneNumber,
                AlimTalkTemplateCode.valueOf(templateCode),
                templateVariables,
                MessageStatus.valueOf(status),
                createdAt);
    }
}
