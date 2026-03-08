package com.ryuqq.setof.domain.qna.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * QnaSortKey - Q&A 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaSortKey implements SortKey {

    CREATED_AT("createdAt");

    private final String fieldName;

    QnaSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static QnaSortKey defaultKey() {
        return CREATED_AT;
    }
}
