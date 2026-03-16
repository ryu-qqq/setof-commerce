package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.port.out.command.ProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품그룹 가격 갱신 매니저.
 *
 * <p>product_group_prices 테이블에 가격 데이터를 저장/갱신합니다.
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
    @Transactional
    public void persistAll(List<ProductGroupPriceUpdateData> updates) {
        if (updates.isEmpty()) {
            return;
        }
        priceCommandPort.persistAll(updates);
    }

    /**
     * 신규 상품그룹의 가격 레코드를 기본값(0)으로 생성.
     *
     * @param productGroupId 상품그룹 ID
     */
    @Transactional
    public void persist(long productGroupId) {
        priceCommandPort.persist(productGroupId);
    }
}
