package com.ryuqq.setof.domain.faq.id;

/**
 * FaqId - FAQ 식별자 Value Object.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * <p>DOM-ID-002: Long ID는 forNew()로 null 생성 (DB auto-increment 대기).
 *
 * @param value FAQ ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqId(Long value) {

    public static FaqId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("FaqId 값은 null일 수 없습니다");
        }
        return new FaqId(value);
    }

    public static FaqId forNew() {
        return new FaqId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
