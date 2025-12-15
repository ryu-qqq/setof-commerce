package com.ryuqq.setof.application.productdescription.port.out.query;

import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import java.util.Optional;

/**
 * 상품설명 조회 Port Out
 *
 * <p>상품설명 조회를 위한 Port입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductDescriptionQueryPort {

    /**
     * ID로 상품설명 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 상품설명
     */
    ProductDescription findById(Long productDescriptionId);

    /**
     * 상품그룹 ID로 상품설명 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 (없으면 Optional.empty)
     */
    Optional<ProductDescription> findByProductGroupId(Long productGroupId);
}
