package com.ryuqq.setof.application.selleroption.port.out.query;

import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;

/**
 * SellerOptionGroupQueryPort - 셀러 옵션 그룹 Query 출력 포트.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerOptionGroupQueryPort {

    /**
     * 상품 그룹 ID로 셀러 옵션 그룹 목록을 조회합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 셀러 옵션 그룹 목록
     */
    List<SellerOptionGroup> findByProductGroupId(ProductGroupId productGroupId);
}
