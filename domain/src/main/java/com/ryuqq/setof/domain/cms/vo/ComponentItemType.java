package com.ryuqq.setof.domain.cms.vo;

/**
 * ComponentItem 타입 열거형
 *
 * <p>컴포넌트 아이템의 유형을 정의합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ComponentItemType {

    /** 상품 아이템 */
    PRODUCT,

    /** 브랜드 아이템 */
    BRAND,

    /** 카테고리 아이템 */
    CATEGORY,

    /** 이미지 아이템 */
    IMAGE,

    /** 탭 아이템 */
    TAB;

    /**
     * 문자열에서 ComponentItemType 변환
     *
     * @param value 문자열 값
     * @return ComponentItemType 인스턴스
     * @throws IllegalArgumentException 잘못된 값인 경우
     */
    public static ComponentItemType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ComponentItemType 값은 필수입니다");
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 ComponentItemType: " + value);
        }
    }
}
