package com.ryuqq.setof.application.productdescription.port.out.query;

import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.Optional;

/** 상품그룹 상세설명 Query Port. */
public interface ProductGroupDescriptionQueryPort {

    /**
     * 상품그룹 ID로 상세설명을 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세설명 Optional
     */
    Optional<ProductGroupDescription> findByProductGroupId(ProductGroupId productGroupId);
}
