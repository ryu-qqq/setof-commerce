package com.ryuqq.setof.domain.cms.vo;

/**
 * Component 타입 Enum
 *
 * <p>CMS에서 지원하는 컴포넌트 타입을 정의합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ComponentType {
    /** 빈 공간 컴포넌트 */
    BLANK,
    /** 텍스트 컴포넌트 */
    TEXT,
    /** 제목 컴포넌트 */
    TITLE,
    /** 이미지 컴포넌트 */
    IMAGE,
    /** 상품 리스트 컴포넌트 */
    PRODUCT,
    /** 카테고리 기반 상품 컴포넌트 */
    CATEGORY,
    /** 브랜드 컴포넌트 */
    BRAND,
    /** 탭 컴포넌트 */
    TAB
}
