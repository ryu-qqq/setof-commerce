package com.ryuqq.setof.domain.qna.id;

/**
 * QnaId - Q&A 식별자 Value Object (새 스키마).
 *
 * <p>UUIDv7 기반 String ID. 외부(Application Factory/IdGenerator)에서 생성하여 주입.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * <p>DOM-ID-002: String ID는 외부 주입. Domain에서 UUID.randomUUID() 금지.
 *
 * @param value Q&A ID (UUIDv7)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaId(String value) {

    public static QnaId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("QnaId 값은 null이거나 빈 문자열일 수 없습니다");
        }
        return new QnaId(value);
    }

    public static QnaId forNew() {
        return new QnaId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
