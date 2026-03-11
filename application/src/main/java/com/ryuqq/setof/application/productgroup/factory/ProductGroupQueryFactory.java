package com.ryuqq.setof.application.productgroup.factory;

import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

/**
 * ProductGroupQueryFactory - 상품그룹 검색 조건 생성 Factory.
 *
 * <p>ProductGroupSearchParams → ProductGroupSearchCriteria 변환을 담당합니다.
 *
 * <p>커서 파싱("lastDomainId,cursorValue" 형식)과 orderType → SortKey 변환 로직을 포함합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupQueryFactory {

    /**
     * ProductGroupSearchParams → ProductGroupSearchCriteria 변환 (목록 조회용).
     *
     * <p>커서 포맷: "lastDomainId,cursorValue" → lastDomainId + cursorValue 분리
     *
     * @param params 검색 파라미터
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createCriteria(ProductGroupSearchParams params) {
        return buildCriteria(params, null);
    }

    /**
     * ProductGroupSearchParams → ProductGroupSearchCriteria 변환 (키워드 검색용).
     *
     * <p>커서 포맷: "lastDomainId,cursorValue" → lastDomainId + cursorValue 분리
     *
     * @param params 검색 파라미터
     * @return ProductGroupSearchCriteria (searchWord 포함)
     */
    public ProductGroupSearchCriteria createSearchCriteria(ProductGroupSearchParams params) {
        return buildCriteria(params, params.searchWord());
    }

    private ProductGroupSearchCriteria buildCriteria(
            ProductGroupSearchParams params, String searchWord) {
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

        String orderType = resolveOrderType(params.orderType());
        ProductGroupSortKey sortKey = ProductGroupSortKey.fromLegacyOrderType(orderType);
        SortDirection direction =
                ProductGroupSortKey.isAscendingForLegacy(orderType)
                        ? SortDirection.ASC
                        : SortDirection.DESC;

        CursorPageRequest<Long> pageRequest =
                lastDomainId != null
                        ? CursorPageRequest.afterId(lastDomainId, params.size())
                        : CursorPageRequest.first(params.size());

        CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                CursorQueryContext.of(sortKey, direction, pageRequest);

        return new ProductGroupSearchCriteria(
                params.sellerId() != null ? SellerId.of(params.sellerId()) : null,
                params.brandId() != null ? BrandId.of(params.brandId()) : null,
                params.categoryId() != null ? CategoryId.of(params.categoryId()) : null,
                params.categoryIds() != null ? params.categoryIds() : java.util.List.of(),
                params.brandIds() != null ? params.brandIds() : java.util.List.of(),
                null,
                null,
                searchWord,
                params.lowestPrice(),
                params.highestPrice(),
                cursorValue,
                queryContext);
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
