package com.ryuqq.setof.domain.qna.id;

/**
 * QnaImageId - Q&A 이미지 식별자 Value Object.
 *
 * <p>보안 민감하지 않은 하위 엔티티이므로 Long ID 사용.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * <p>DOM-ID-002: Long ID는 forNew()로 null 생성 (DB auto-increment 대기).
 *
 * @param value 이미지 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaImageId(Long value) {

    public static QnaImageId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("QnaImageId 값은 null일 수 없습니다");
        }
        return new QnaImageId(value);
    }

    public static QnaImageId forNew() {
        return new QnaImageId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
