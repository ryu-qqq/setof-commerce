package com.ryuqq.setof.domain.cms.vo;

/**
 * 상품 리스트 표시 타입 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ListType {
    /** 1단 리스트 */
    ONE_STEP,
    /** 2단 리스트 */
    TWO_STEP,
    /** 다중 리스트 */
    MULTI,
    /** 컬럼 형태 */
    COLUMN,
    /** 가로 행 형태 */
    ROW,
    /** 그리드 형태 */
    GRID,
    /** 없음 */
    NONE
}
