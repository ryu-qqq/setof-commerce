package com.ryuqq.setof.domain.notification.aggregate;

import com.ryuqq.setof.domain.notification.id.NotificationOutboxId;
import com.ryuqq.setof.domain.notification.vo.NotificationChannel;
import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import com.ryuqq.setof.domain.notification.vo.NotificationRecipient;
import com.ryuqq.setof.domain.notification.vo.NotificationReference;
import com.ryuqq.setof.domain.notification.vo.NotificationStatus;
import java.time.Instant;

/**
 * NotificationOutbox - 알림 아웃박스 Aggregate Root.
 *
 * <p>비즈니스 이벤트 발생 시 알림 발송 요청을 저장하고, 스케줄러가 SQS로 발행하면 Consumer가 메시지를 조립하여 NHN Cloud 알림톡 API를 호출합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>상태 전이는 PENDING → PUBLISHED → COMPLETED/FAILED 순서만 허용
 *   <li>retryCount가 MAX_RETRY를 초과하면 FAILED로 전환
 *   <li>recipient(수신자)는 필수
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class NotificationOutbox {

    private static final int MAX_RETRY = 3;

    private final NotificationOutboxId id;
    private final NotificationChannel channel;
    private final NotificationEventType eventType;
    private final NotificationReference reference;
    private final NotificationRecipient recipient;
    private NotificationStatus status;
    private int retryCount;
    private String failReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private NotificationOutbox(
            NotificationOutboxId id,
            NotificationChannel channel,
            NotificationEventType eventType,
            NotificationReference reference,
            NotificationRecipient recipient,
            NotificationStatus status,
            int retryCount,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.channel = channel;
        this.eventType = eventType;
        this.reference = reference;
        this.recipient = recipient;
        this.status = status;
        this.retryCount = retryCount;
        this.failReason = failReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 알림 아웃박스 생성.
     *
     * @param channel 발송 채널
     * @param eventType 트리거 이벤트 유형
     * @param reference 참조 엔티티 정보 + payload
     * @param recipient 수신자 정보
     * @param now 생성 시각
     * @return 새 NotificationOutbox 인스턴스 (PENDING 상태)
     */
    public static NotificationOutbox forNew(
            NotificationChannel channel,
            NotificationEventType eventType,
            NotificationReference reference,
            NotificationRecipient recipient,
            Instant now) {
        if (channel == null) {
            throw new IllegalArgumentException("발송 채널은 필수입니다");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("이벤트 유형은 필수입니다");
        }
        if (reference == null) {
            throw new IllegalArgumentException("참조 정보는 필수입니다");
        }
        if (recipient == null) {
            throw new IllegalArgumentException("수신자 정보는 필수입니다");
        }

        return new NotificationOutbox(
                NotificationOutboxId.forNew(),
                channel,
                eventType,
                reference,
                recipient,
                NotificationStatus.PENDING,
                0,
                null,
                now,
                now);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @return 복원된 NotificationOutbox 인스턴스
     */
    public static NotificationOutbox reconstitute(
            NotificationOutboxId id,
            NotificationChannel channel,
            NotificationEventType eventType,
            NotificationReference reference,
            NotificationRecipient recipient,
            NotificationStatus status,
            int retryCount,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        return new NotificationOutbox(
                id,
                channel,
                eventType,
                reference,
                recipient,
                status,
                retryCount,
                failReason,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * SQS 발행 완료 처리.
     *
     * @param now 발행 시각
     * @throws IllegalStateException PENDING 상태가 아닐 때
     */
    public void markPublished(Instant now) {
        validateTransition(NotificationStatus.PENDING, NotificationStatus.PUBLISHED);
        this.status = NotificationStatus.PUBLISHED;
        this.updatedAt = now;
    }

    /**
     * 처리 완료 (NHN Cloud 발송 성공).
     *
     * @param now 완료 시각
     * @throws IllegalStateException PUBLISHED 상태가 아닐 때
     */
    public void markCompleted(Instant now) {
        validateTransition(NotificationStatus.PUBLISHED, NotificationStatus.COMPLETED);
        this.status = NotificationStatus.COMPLETED;
        this.updatedAt = now;
    }

    /**
     * 처리 실패 + 재시도 판단.
     *
     * <p>retryCount가 MAX_RETRY 미만이면 PENDING으로 복구, 이상이면 FAILED.
     *
     * @param reason 실패 사유
     * @param now 실패 시각
     */
    public void markFailed(String reason, Instant now) {
        this.retryCount++;
        this.failReason = reason;
        this.updatedAt = now;

        if (this.retryCount >= MAX_RETRY) {
            this.status = NotificationStatus.FAILED;
        } else {
            this.status = NotificationStatus.PENDING;
        }
    }

    /**
     * Stuck 복구 (PUBLISHED 상태에서 오래 머문 경우).
     *
     * @param now 복구 시각
     * @throws IllegalStateException PUBLISHED 상태가 아닐 때
     */
    public void recoverStuck(Instant now) {
        if (this.status != NotificationStatus.PUBLISHED) {
            throw new IllegalStateException("PUBLISHED 상태에서만 stuck 복구 가능합니다. 현재 상태: " + status);
        }
        this.retryCount++;
        this.updatedAt = now;

        if (this.retryCount >= MAX_RETRY) {
            this.status = NotificationStatus.FAILED;
            this.failReason = "Stuck recovery exceeded max retry";
        } else {
            this.status = NotificationStatus.PENDING;
        }
    }

    // ========== Validation ==========

    private void validateTransition(NotificationStatus expected, NotificationStatus target) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    target + " 전이는 " + expected + " 상태에서만 가능합니다. 현재 상태: " + status);
        }
    }

    // ========== Condition Checks ==========

    public boolean isPending() {
        return status == NotificationStatus.PENDING;
    }

    public boolean isPublished() {
        return status == NotificationStatus.PUBLISHED;
    }

    public boolean isMaxRetryExceeded() {
        return retryCount >= MAX_RETRY;
    }

    /** 참조 payload 존재 여부 */
    public boolean hasPayload() {
        return reference.hasPayload();
    }

    // ========== Accessor Methods ==========

    public NotificationOutboxId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public NotificationChannel channel() {
        return channel;
    }

    public NotificationEventType eventType() {
        return eventType;
    }

    public NotificationReference reference() {
        return reference;
    }

    public String referenceKey() {
        return reference.toKey();
    }

    public long referenceId() {
        return reference.referenceId();
    }

    public String payload() {
        return reference.payload();
    }

    public NotificationRecipient recipient() {
        return recipient;
    }

    public String recipientPhone() {
        return recipient.phoneNumber();
    }

    public Long recipientMemberId() {
        return recipient.memberId();
    }

    public NotificationStatus status() {
        return status;
    }

    public int retryCount() {
        return retryCount;
    }

    public String failReason() {
        return failReason;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
