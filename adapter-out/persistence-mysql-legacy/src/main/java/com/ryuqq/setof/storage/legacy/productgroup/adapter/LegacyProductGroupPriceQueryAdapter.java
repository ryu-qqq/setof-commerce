package com.ryuqq.setof.storage.legacy.productgroup.adapter;

import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.storage.legacy.productgroup.dto.LegacyProductGroupPriceRow;
import com.ryuqq.setof.storage.legacy.productgroup.repository.LegacyProductGroupPriceQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyProductGroupPriceQueryAdapter - 레거시 상품그룹 가격 조회 Adapter.
 *
 * <p>타겟(브랜드, 셀러, 카테고리, 상품)에 해당하는 상품그룹의 정가와 현재가를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyProductGroupPriceQueryAdapter implements LegacyProductGroupPriceQueryPort {

    private final LegacyProductGroupPriceQueryDslRepository repository;

    public LegacyProductGroupPriceQueryAdapter(
            LegacyProductGroupPriceQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId) {
        return repository.findPriceRowsByTarget(targetType.name(), targetId).stream()
                .map(this::toDomainRow)
                .toList();
    }

    private ProductGroupPriceRow toDomainRow(LegacyProductGroupPriceRow row) {
        return new ProductGroupPriceRow(
                row.productGroupId(), row.regularPrice(), row.currentPrice());
    }
}
