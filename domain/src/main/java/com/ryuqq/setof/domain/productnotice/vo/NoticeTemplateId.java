package com.ryuqq.setof.domain.productnotice.vo;

import com.ryuqq.setof.domain.productnotice.exception.InvalidNoticeTemplateIdException;

/**
 * 상품고시 템플릿 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 템플릿 ID 값
 */
public record NoticeTemplateId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public NoticeTemplateId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return NoticeTemplateId 인스턴스
     * @throws InvalidNoticeTemplateIdException value가 유효하지 않은 경우
     */
    public static NoticeTemplateId of(Long value) {
        return new NoticeTemplateId(value);
    }

    /**
     * Static Factory Method - 신규 생성용 (ID 없음)
     *
     * @return null 값을 가진 NoticeTemplateId 인스턴스
     */
    public static NoticeTemplateId forNew() {
        return new NoticeTemplateId(null);
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return value == null;
    }

    private static void validate(Long value) {
        if (value != null && value <= 0) {
            throw new InvalidNoticeTemplateIdException(value);
        }
    }
}
