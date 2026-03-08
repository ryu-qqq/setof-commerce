package com.ryuqq.setof.domain.qna.id;

/**
 * LegacyQnaId - Q&A 레거시 식별자 Value Object.
 *
 * <p>레거시 DB의 qna_id (Long) 참조용.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * @param value 레거시 Q&A ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyQnaId(Long value) {

    public static LegacyQnaId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("LegacyQnaId 값은 null일 수 없습니다");
        }
        return new LegacyQnaId(value);
    }

    public static LegacyQnaId forNew() {
        return new LegacyQnaId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
