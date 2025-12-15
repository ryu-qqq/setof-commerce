package com.ryuqq.setof.application.product.component;

import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

/**
 * 기존 상품 데이터
 *
 * <p>변경 감지를 위한 현재 저장된 데이터 스냅샷
 *
 * @param productGroup 상품그룹
 * @param images 이미지 목록
 * @param description 상세설명 (nullable)
 * @param notice 고시정보 (nullable)
 * @author development-team
 * @since 1.0.0
 */
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record ExistingProductData(
        ProductGroup productGroup,
        List<ProductImage> images,
        ProductDescription description,
        ProductNotice notice) {

    /**
     * 상세설명이 있는지 확인
     *
     * @return 있으면 true
     */
    public boolean hasDescription() {
        return description != null;
    }

    /**
     * 고시정보가 있는지 확인
     *
     * @return 있으면 true
     */
    public boolean hasNotice() {
        return notice != null;
    }
}
