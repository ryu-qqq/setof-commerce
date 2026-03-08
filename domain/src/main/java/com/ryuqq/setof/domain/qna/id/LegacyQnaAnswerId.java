package com.ryuqq.setof.domain.qna.id;

/**
 * LegacyQnaAnswerId - Q&A 답변 레거시 식별자 Value Object.
 *
 * <p>레거시 DB의 qna_answer_id (Long) 참조용.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * @param value 레거시 Q&A 답변 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyQnaAnswerId(Long value) {

    public static LegacyQnaAnswerId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("LegacyQnaAnswerId 값은 null일 수 없습니다");
        }
        return new LegacyQnaAnswerId(value);
    }

    public static LegacyQnaAnswerId forNew() {
        return new LegacyQnaAnswerId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
