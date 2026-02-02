package com.ryuqq.setof.domain.category.aggregate;

/**
 * Category 업데이트 데이터 번들.
 *
 * <p>카테고리 수정 시 필요한 데이터를 묶어서 전달합니다.
 *
 * @param displayed 표시 여부
 */
public record CategoryUpdateData(boolean displayed) {

    public static CategoryUpdateData of(boolean displayed) {
        return new CategoryUpdateData(displayed);
    }
}
