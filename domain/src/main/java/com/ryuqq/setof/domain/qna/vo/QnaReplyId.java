package com.ryuqq.setof.domain.qna.vo;

import java.util.Objects;

public final class QnaReplyId {

    private final long value;

    private QnaReplyId(long value) {
        this.value = value;
    }

    public static QnaReplyId of(long value) {
        return new QnaReplyId(value);
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QnaReplyId that = (QnaReplyId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "QnaReplyId{value=" + value + "}";
    }
}
