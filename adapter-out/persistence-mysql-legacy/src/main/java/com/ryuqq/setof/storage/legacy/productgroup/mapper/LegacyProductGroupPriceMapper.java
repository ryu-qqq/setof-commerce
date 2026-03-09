package com.ryuqq.setof.storage.legacy.productgroup.mapper;

import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.storage.legacy.productgroup.dto.LegacyProductGroupPriceUpdateRow;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 상품그룹 가격 매퍼.
 *
 * <p>도메인 DTO와 레거시 저장소 DTO 간 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyProductGroupPriceMapper {

    public List<LegacyProductGroupPriceUpdateRow> toUpdateRows(
            List<ProductGroupPriceUpdateData> updates) {
        return updates.stream().map(this::toUpdateRow).toList();
    }

    public LegacyProductGroupPriceUpdateRow toUpdateRow(ProductGroupPriceUpdateData update) {
        return new LegacyProductGroupPriceUpdateRow(
                update.productGroupId(),
                update.salePrice(),
                update.discountRate(),
                update.directDiscountRate(),
                update.directDiscountPrice());
    }
}
