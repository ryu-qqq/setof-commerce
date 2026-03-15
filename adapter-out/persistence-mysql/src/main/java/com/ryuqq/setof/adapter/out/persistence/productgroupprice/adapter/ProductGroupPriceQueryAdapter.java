package com.ryuqq.setof.adapter.out.persistence.productgroupprice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupprice.repository.ProductGroupPriceQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.application.discount.port.out.query.ProductGroupPriceQueryPort;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPriceQueryAdapter - 상품 그룹 가격 Query 어댑터.
 *
 * <p>ProductGroupPriceQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceQueryAdapter implements ProductGroupPriceQueryPort {

    private final ProductGroupPriceQueryDslRepository queryDslRepository;

    public ProductGroupPriceQueryAdapter(ProductGroupPriceQueryDslRepository queryDslRepository) {
        this.queryDslRepository = queryDslRepository;
    }

    /**
     * 타겟에 해당하는 상품그룹의 가격 정보 목록을 조회합니다.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 상품그룹별 가격 정보 목록
     */
    @Override
    public List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId) {
        return queryDslRepository.findPriceRowsByTarget(targetType.name(), targetId);
    }
}
