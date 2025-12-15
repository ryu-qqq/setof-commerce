package com.ryuqq.setof.application.productnotice.port.out.query;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.Optional;

/**
 * 상품고시 조회 Port Out
 *
 * <p>상품고시 조회를 위한 Port입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductNoticeQueryPort {

    /**
     * ID로 상품고시 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 상품고시
     */
    ProductNotice findById(Long productNoticeId);

    /**
     * 상품그룹 ID로 상품고시 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 (없으면 Optional.empty)
     */
    Optional<ProductNotice> findByProductGroupId(Long productGroupId);
}
