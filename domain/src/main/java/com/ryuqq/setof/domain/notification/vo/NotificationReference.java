package com.ryuqq.setof.domain.notification.vo;

/**
 * 알림 참조 정보 Value Object.
 *
 * <p>알림 대상 엔티티의 유형과 ID, 그리고 이벤트 시점의 스냅샷 데이터를 담습니다. payload는 JSON 형식으로, Consumer가 메시지 조립 시 기본 데이터로
 * 사용합니다.
 *
 * <p>예시:
 *
 * <pre>
 * referenceType = ORDER
 * referenceId = 12345
 * payload = {"orderNo":"ORD-001","memberId":456,"productName":"나이키 에어맥스"}
 * </pre>
 *
 * @param referenceType 참조 엔티티 유형
 * @param referenceId 참조 엔티티 ID
 * @param payload 이벤트 시점 스냅샷 데이터 (JSON, nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NotificationReference(
        NotificationReferenceType referenceType, long referenceId, String payload) {

    public NotificationReference {
        if (referenceType == null) {
            throw new IllegalArgumentException("참조 유형은 필수입니다");
        }
        if (referenceId <= 0) {
            throw new IllegalArgumentException("참조 ID는 0보다 커야 합니다: " + referenceId);
        }
    }

    public static NotificationReference of(
            NotificationReferenceType referenceType, long referenceId, String payload) {
        return new NotificationReference(referenceType, referenceId, payload);
    }

    public static NotificationReference withoutPayload(
            NotificationReferenceType referenceType, long referenceId) {
        return new NotificationReference(referenceType, referenceId, null);
    }

    /** payload 존재 여부 */
    public boolean hasPayload() {
        return payload != null && !payload.isBlank();
    }

    /** "ORDER:12345" 형식의 문자열 표현 */
    public String toKey() {
        return referenceType.name() + ":" + referenceId;
    }
}
