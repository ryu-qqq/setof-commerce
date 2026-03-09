package com.ryuqq.setof.domain.notification.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * NotificationOutbox 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum NotificationOutboxSortKey implements SortKey {

    /** 생성일시 순 (기본값) */
    CREATED_AT("createdAt");

    private final String fieldName;

    NotificationOutboxSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static NotificationOutboxSortKey defaultKey() {
        return CREATED_AT;
    }
}
