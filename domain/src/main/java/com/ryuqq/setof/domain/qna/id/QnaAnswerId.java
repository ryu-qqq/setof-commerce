package com.ryuqq.setof.domain.qna.id;

/**
 * QnaAnswerId - Q&A 답변 식별자 Value Object.
 *
 * <p>UUIDv7 기반 String ID.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * @param value 답변 ID (UUIDv7)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaAnswerId(String value) {

    public static QnaAnswerId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("QnaAnswerId 값은 null이거나 빈 문자열일 수 없습니다");
        }
        return new QnaAnswerId(value);
    }

    public static QnaAnswerId forNew() {
        return new QnaAnswerId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
