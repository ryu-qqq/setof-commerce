package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupPriceQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.ProductGroupPriceQueryPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPriceQueryAdapter - 새 스키마(setof) 상품그룹 가격 조회 Adapter.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceQueryAdapter implements ProductGroupPriceQueryPort {

    private final ProductGroupPriceQueryDslRepository repository;

    public ProductGroupPriceQueryAdapter(ProductGroupPriceQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId) {
        return repository.findPriceRowsByTarget(targetType.name(), targetId);
    }
}
