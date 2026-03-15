package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;

/**
 * 상품그룹 가격 조회 포트.
 *
 * <p>product_group_prices 테이블에서 가격 정보를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupPriceQueryPort {

    /**
     * 타겟에 해당하는 상품그룹의 가격 정보 목록 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 상품그룹별 가격 정보 목록
     */
    List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId);
}
