package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;

/**
 * 레거시 product_group 가격 갱신 포트.
 *
 * <p>persistence-mysql-legacy에서 구현합니다. sale_price, discount_rate, direct_discount_rate,
 * direct_discount_price 4개 필드를 배치 UPDATE합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyProductGroupPriceCommandPort {

    /**
     * 여러 상품그룹의 가격 정보를 일괄 갱신.
     *
     * @param updates 갱신 대상 목록
     */
    void persistAll(List<ProductGroupPriceUpdateData> updates);
}
