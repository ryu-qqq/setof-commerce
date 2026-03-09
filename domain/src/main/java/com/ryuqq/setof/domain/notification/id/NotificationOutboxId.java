package com.ryuqq.setof.domain.notification.id;

/** 알림 아웃박스 ID Value Object. */
public record NotificationOutboxId(Long value) {

    public static NotificationOutboxId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("NotificationOutboxId 값은 null일 수 없습니다");
        }
        return new NotificationOutboxId(value);
    }

    public static NotificationOutboxId forNew() {
        return new NotificationOutboxId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
