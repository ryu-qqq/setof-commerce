package com.ryuqq.setof.domain.notification.vo;

/**
 * 알림 수신자 정보 Value Object.
 *
 * <p>수신자의 전화번호와 회원 ID를 함께 관리합니다. 회원 ID는 비회원 알림 시 nullable입니다.
 *
 * @param phoneNumber 수신자 전화번호 (필수)
 * @param memberId 회원 ID (nullable, 비회원 시 null)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NotificationRecipient(String phoneNumber, Long memberId) {

    public NotificationRecipient {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("수신자 전화번호는 필수입니다");
        }
    }

    public static NotificationRecipient of(String phoneNumber, Long memberId) {
        return new NotificationRecipient(phoneNumber, memberId);
    }

    public static NotificationRecipient ofPhone(String phoneNumber) {
        return new NotificationRecipient(phoneNumber, null);
    }

    /** 회원 여부 확인 */
    public boolean isMember() {
        return memberId != null;
    }
}
