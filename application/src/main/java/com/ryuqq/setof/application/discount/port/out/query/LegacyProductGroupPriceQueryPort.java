package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;

/**
 * 레거시 상품그룹 가격 조회 포트.
 *
 * <p>타겟에 해당하는 상품그룹 ID 목록과 정가(regular_price), 현재가(current_price)를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyProductGroupPriceQueryPort {

    /**
     * 타겟에 해당하는 상품그룹의 가격 정보 목록 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 상품그룹별 가격 정보 목록
     */
    List<ProductGroupPriceRow> findByTarget(DiscountTargetType targetType, long targetId);

    /**
     * 상품그룹 가격 정보 행.
     *
     * @param productGroupId 상품그룹 ID
     * @param regularPrice 정가
     * @param currentPrice 현재가 (셀러 설정가)
     */
    record ProductGroupPriceRow(long productGroupId, int regularPrice, int currentPrice) {}
}
