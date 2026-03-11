package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.query.ProductGroupPriceQueryPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 새 스키마(setof) 상품그룹 가격 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceReadManager {

    private final ProductGroupPriceQueryPort priceQueryPort;

    public ProductGroupPriceReadManager(ProductGroupPriceQueryPort priceQueryPort) {
        this.priceQueryPort = priceQueryPort;
    }

    /**
     * 타겟에 해당하는 상품그룹의 가격 정보 목록 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 상품그룹별 가격 정보 목록
     */
    public List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId) {
        return priceQueryPort.findByTarget(targetType, targetId);
    }
}
