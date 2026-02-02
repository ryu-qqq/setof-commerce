package com.ryuqq.setof.domain.brand.aggregate;

import com.ryuqq.setof.domain.brand.vo.DisplayOrder;

/**
 * Brand 업데이트 데이터 번들.
 *
 * <p>브랜드 수정 시 필요한 데이터를 묶어서 전달합니다.
 *
 * @param displayOrder 표시 순서 (필수)
 * @param displayed 표시 여부
 */
public record BrandUpdateData(DisplayOrder displayOrder, boolean displayed) {

    public static BrandUpdateData of(int displayOrder, boolean displayed) {
        return new BrandUpdateData(DisplayOrder.of(displayOrder), displayed);
    }
}
