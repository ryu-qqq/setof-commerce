package com.ryuqq.setof.application.productgroup.factory;

import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import org.springframework.stereotype.Component;

/**
 * ProductGroupQueryFactory - 상품그룹 검색 조건 생성 Factory.
 *
 * <p>ProductGroupSearchParams → LegacyProductGroupSearchCondition / LegacySearchCondition 변환을
 * 담당합니다.
 *
 * <p>커서 파싱("lastDomainId,cursorValue" 형식)과 orderType 기본값 결정 로직을 포함합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupQueryFactory {

    /**
     * ProductGroupSearchParams → LegacyProductGroupSearchCondition 변환.
     *
     * <p>커서 포맷: "lastDomainId,cursorValue" → lastDomainId + cursorValue 분리
     *
     * @param params 검색 파라미터
     * @return LegacyProductGroupSearchCondition
     */
    public LegacyProductGroupSearchCondition createCondition(ProductGroupSearchParams params) {
        Long lastDomainId = null;
        String cursorValue = null;

        if (params.hasCursor()) {
            String[] parts = params.cursor().split(",", 2);
            if (parts.length >= 1) {
                lastDomainId = parseLong(parts[0]);
            }
            if (parts.length >= 2) {
                cursorValue = parts[1];
            }
        }

        return new LegacyProductGroupSearchCondition(
                null,
                null,
                lastDomainId,
                cursorValue,
                params.lowestPrice(),
                params.highestPrice(),
                params.categoryId(),
                params.brandId(),
                params.sellerId(),
                params.categoryIds(),
                params.brandIds(),
                resolveOrderType(params.orderType()),
                params.size());
    }

    /**
     * ProductGroupSearchParams → LegacySearchCondition 변환 (키워드 검색용).
     *
     * <p>커서 포맷: "lastDomainId,cursorValue" → lastDomainId + cursorValue 분리
     *
     * @param params 검색 파라미터
     * @return LegacySearchCondition
     */
    public LegacySearchCondition createSearchCondition(ProductGroupSearchParams params) {
        Long lastDomainId = null;
        String cursorValue = null;

        if (params.hasCursor()) {
            String[] parts = params.cursor().split(",", 2);
            if (parts.length >= 1) {
                lastDomainId = parseLong(parts[0]);
            }
            if (parts.length >= 2) {
                cursorValue = parts[1];
            }
        }

        return new LegacySearchCondition(
                params.searchWord(),
                params.productGroupId(),
                lastDomainId,
                cursorValue,
                params.lowestPrice(),
                params.highestPrice(),
                params.categoryId(),
                params.brandId(),
                params.sellerId(),
                params.categoryIds(),
                params.brandIds(),
                resolveOrderType(params.orderType()),
                params.size());
    }

    private String resolveOrderType(String orderType) {
        if (orderType == null || orderType.isBlank()) {
            return "RECOMMEND";
        }
        return orderType.toUpperCase();
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
