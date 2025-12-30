package com.ryuqq.setof.domain.qna.vo;

import java.util.Objects;

public final class QnaId {

    private final long value;

    private QnaId(long value) {
        this.value = value;
    }

    public static QnaId of(long value) {
        return new QnaId(value);
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
        QnaId qnaId = (QnaId) o;
        return value == qnaId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "QnaId{value=" + value + "}";
    }
}
