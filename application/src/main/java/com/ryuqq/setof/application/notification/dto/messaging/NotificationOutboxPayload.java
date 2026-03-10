package com.ryuqq.setof.application.notification.dto.messaging;

import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;

/**
 * SQS 메시지 페이로드.
 *
 * <p>Scheduler가 Outbox를 SQS로 발행할 때 사용하는 DTO입니다. Consumer가 수신하여 알림톡 발송을 수행합니다.
 *
 * @param outboxId 아웃박스 ID
 * @param channel 발송 채널 (ALIMTALK, SMS)
 * @param eventType 이벤트 유형 (ORDER_ACCEPTED 등)
 * @param referenceType 참조 엔티티 유형 (ORDER, CANCEL 등)
 * @param referenceId 참조 엔티티 ID
 * @param payload 이벤트 시점 스냅샷 데이터 (JSON)
 * @param recipientPhone 수신자 전화번호
 * @param recipientMemberId 수신자 회원 ID (nullable)
 */
public record NotificationOutboxPayload(
        long outboxId,
        String channel,
        String eventType,
        String referenceType,
        long referenceId,
        String payload,
        String recipientPhone,
        Long recipientMemberId) {

    public static NotificationOutboxPayload from(NotificationOutbox outbox) {
        return new NotificationOutboxPayload(
                outbox.idValue(),
                outbox.channel().name(),
                outbox.eventType().name(),
                outbox.reference().referenceType().name(),
                outbox.referenceId(),
                outbox.payload(),
                outbox.recipientPhone(),
                outbox.recipientMemberId());
    }
}
