package com.ryuqq.setof.application.productgroup.port.out.command;

import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;

/**
 * 상품그룹 가격 저장 포트.
 *
 * <p>product_group_prices 테이블에 가격 데이터를 저장/갱신합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupPriceCommandPort {

    /**
     * 여러 상품그룹의 가격 정보를 일괄 갱신.
     *
     * @param updates 갱신 대상 목록
     */
    void persistAll(List<ProductGroupPriceUpdateData> updates);

    /**
     * 신규 상품그룹의 가격 레코드를 기본값(0)으로 생성.
     *
     * @param productGroupId 상품그룹 ID
     */
    void persist(long productGroupId);
}
