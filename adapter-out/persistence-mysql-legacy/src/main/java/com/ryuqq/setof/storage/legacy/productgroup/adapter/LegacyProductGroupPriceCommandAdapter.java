package com.ryuqq.setof.storage.legacy.productgroup.adapter;

import com.ryuqq.setof.application.discount.port.out.command.LegacyProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.storage.legacy.productgroup.mapper.LegacyProductGroupPriceMapper;
import com.ryuqq.setof.storage.legacy.productgroup.repository.LegacyProductGroupPriceQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyProductGroupPriceCommandAdapter - 레거시 상품그룹 가격 갱신 Adapter.
 *
 * <p>QueryDSL CASE WHEN UPDATE로 sale_price, discount_rate, direct_discount_rate,
 * direct_discount_price 필드를 일괄 갱신합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyProductGroupPriceCommandAdapter implements LegacyProductGroupPriceCommandPort {

    private final LegacyProductGroupPriceQueryDslRepository repository;
    private final LegacyProductGroupPriceMapper mapper;

    public LegacyProductGroupPriceCommandAdapter(
            LegacyProductGroupPriceQueryDslRepository repository,
            LegacyProductGroupPriceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void persistAll(List<ProductGroupPriceUpdateData> updates) {
        repository.updatePrices(mapper.toUpdateRows(updates));
    }
}
