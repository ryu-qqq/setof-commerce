package com.ryuqq.setof.domain.faq.vo;

/**
 * FAQ 카테고리 식별자 Value Object
 *
 * <p>FAQ 카테고리의 고유 식별자를 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record FaqCategoryId(Long value) {

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException 값이 0 이하일 때
     */
    public FaqCategoryId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("FaqCategoryId must be positive: " + value);
        }
    }

    /**
     * 신규 생성용 (ID 미할당 상태)
     *
     * @return null ID를 가진 FaqCategoryId
     */
    public static FaqCategoryId forNew() {
        return new FaqCategoryId(null);
    }

    /**
     * 기존 ID로 생성
     *
     * @param value ID 값
     * @return 해당 값을 가진 FaqCategoryId
     */
    public static FaqCategoryId of(Long value) {
        return new FaqCategoryId(value);
    }

    /**
     * 신규 엔티티 여부 확인
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return value == null;
    }
}
