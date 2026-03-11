package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.command.ProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.AppliedDiscount;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 새 스키마(setof) 상품그룹 가격 갱신 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceCommandManager {

    private final ProductGroupPriceCommandPort priceCommandPort;

    public ProductGroupPriceCommandManager(ProductGroupPriceCommandPort priceCommandPort) {
        this.priceCommandPort = priceCommandPort;
    }

    /**
     * 여러 상품그룹의 가격 정보를 일괄 갱신.
     *
     * @param updates 갱신 대상 목록
     */
    public void persistAll(List<ProductGroupPriceUpdateData> updates) {
        if (updates.isEmpty()) {
            return;
        }
        priceCommandPort.persistAll(updates);
    }

    /**
     * 상품그룹의 적용 할인 내역을 교체.
     *
     * @param productGroupId 상품그룹 ID
     * @param appliedDiscounts 적용 할인 내역 목록
     * @param appliedAt 할인 적용 시각
     */
    public void replaceAppliedDiscounts(
            long productGroupId, List<AppliedDiscount> appliedDiscounts, Instant appliedAt) {
        priceCommandPort.replaceAppliedDiscounts(productGroupId, appliedDiscounts, appliedAt);
    }
}
