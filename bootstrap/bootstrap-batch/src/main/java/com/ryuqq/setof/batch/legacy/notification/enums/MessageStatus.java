package com.ryuqq.setof.batch.legacy.notification.enums;

/**
 * 알림톡 메시지 상태
 *
 * @author development-team
 * @since 1.0.0
 */
public enum MessageStatus {
    PENDING("pending"),
    SEND("send"),
    FAILED("failed");

    private final String value;

    MessageStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MessageStatus fromValue(String value) {
        for (MessageStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
