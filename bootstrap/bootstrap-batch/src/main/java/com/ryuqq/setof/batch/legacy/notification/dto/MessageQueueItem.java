package com.ryuqq.setof.batch.legacy.notification.dto;

import com.ryuqq.setof.batch.legacy.notification.enums.AlimTalkTemplateCode;
import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;
import java.time.LocalDateTime;

/**
 * 메시지 큐 아이템 DTO
 *
 * <p>실제 DB 스키마: message_id, TEMPLATE_CODE, PARAMETERS (JSON), STATUS, INSERT_DATE
 *
 * <p>PARAMETERS JSON 예시: {"memberName": "홍길동", "recipientNo": "01012345678", "templateCode":
 * "MEMBER_JOIN"}
 *
 * @author development-team
 * @since 1.0.0
 */
public record MessageQueueItem(
        Long messageId,
        String phoneNumber,
        AlimTalkTemplateCode templateCode,
        String templateVariables,
        MessageStatus status,
        LocalDateTime createdAt) {}
