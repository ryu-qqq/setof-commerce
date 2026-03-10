package com.ryuqq.setof.application.productgroup.port.out.query;

import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Optional;

/**
 * ProductGroupQueryPort - 상품 그룹 Query 출력 포트.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductGroupQueryPort {

    /**
     * ID로 상품 그룹을 조회합니다.
     *
     * @param id 상품 그룹 ID
     * @return 상품 그룹 Optional
     */
    Optional<ProductGroup> findById(ProductGroupId id);

    /**
     * ID 목록으로 상품 그룹 목록을 조회합니다.
     *
     * @param ids 상품 그룹 ID 목록
     * @return 상품 그룹 목록
     */
    List<ProductGroup> findByIds(List<ProductGroupId> ids);
}
