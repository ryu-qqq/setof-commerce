package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.command.LegacyProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 상품그룹 가격 갱신 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyProductGroupPriceCommandManager {

    private final LegacyProductGroupPriceCommandPort priceCommandPort;

    public LegacyProductGroupPriceCommandManager(
            LegacyProductGroupPriceCommandPort priceCommandPort) {
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
}
