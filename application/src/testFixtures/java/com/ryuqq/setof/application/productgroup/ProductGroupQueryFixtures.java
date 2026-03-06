package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupCursorSearchParams;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupOffsetSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupOffsetSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.List;

/**
 * ProductGroup Query 테스트 Fixtures.
 *
 * <p>ProductGroup 관련 Query 파라미터 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupQueryFixtures {

    private ProductGroupQueryFixtures() {}

    // ===== ProductGroupOffsetSearchParams Fixtures =====

    public static ProductGroupOffsetSearchParams offsetSearchParams() {
        return ProductGroupOffsetSearchParams.of(
                null, null, null, null, null, null, null, null, defaultCommonSearchParams());
    }

    public static ProductGroupOffsetSearchParams offsetSearchParams(Long sellerId) {
        return ProductGroupOffsetSearchParams.of(
                sellerId, null, null, null, null, null, null, null, defaultCommonSearchParams());
    }

    public static ProductGroupOffsetSearchParams offsetSearchParams(int page, int size) {
        return ProductGroupOffsetSearchParams.of(
                null, null, null, null, null, null, null, null, commonSearchParams(page, size));
    }

    public static ProductGroupOffsetSearchParams offsetSearchParamsWithStatus(String status) {
        return ProductGroupOffsetSearchParams.of(
                null, null, null, status, null, null, null, null, defaultCommonSearchParams());
    }

    public static ProductGroupOffsetSearchParams offsetSearchParamsWithPriceRange(
            Long minPrice, Long maxPrice) {
        return ProductGroupOffsetSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                null,
                minPrice,
                maxPrice,
                defaultCommonSearchParams());
    }

    public static ProductGroupOffsetSearchParams offsetSearchParams(
            Long sellerId, Long brandId, Long categoryId, String status, int page, int size) {
        return ProductGroupOffsetSearchParams.of(
                sellerId,
                brandId,
                categoryId,
                status,
                null,
                null,
                null,
                null,
                commonSearchParams(page, size));
    }

    // ===== ProductGroupCursorSearchParams Fixtures =====

    public static ProductGroupCursorSearchParams cursorSearchParams() {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "DESC",
                null,
                null,
                20);
    }

    public static ProductGroupCursorSearchParams cursorSearchParams(int size) {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "DESC",
                null,
                null,
                size);
    }

    public static ProductGroupCursorSearchParams cursorSearchParams(Long cursorId, int size) {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "DESC",
                cursorId,
                null,
                size);
    }

    public static ProductGroupCursorSearchParams cursorSearchParamsWithKeyword(String searchWord) {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                searchWord,
                "createdAt",
                "DESC",
                null,
                null,
                20);
    }

    public static ProductGroupCursorSearchParams cursorSearchParamsWithBrand(Long brandId) {
        return ProductGroupCursorSearchParams.of(
                null,
                brandId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "DESC",
                null,
                null,
                20);
    }

    public static ProductGroupCursorSearchParams cursorSearchParamsWithCategory(Long categoryId) {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                categoryId,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "DESC",
                null,
                null,
                20);
    }

    public static ProductGroupCursorSearchParams cursorSearchParamsWithPriceRange(
            Long lowestPrice, Long highestPrice) {
        return ProductGroupCursorSearchParams.of(
                null,
                null,
                null,
                null,
                null,
                lowestPrice,
                highestPrice,
                null,
                null,
                "createdAt",
                "DESC",
                null,
                null,
                20);
    }

    public static ProductGroupCursorSearchParams cursorSearchParams(
            Long sellerId,
            Long brandId,
            List<Long> categoryIds,
            String searchWord,
            Long cursorId,
            int size) {
        return ProductGroupCursorSearchParams.of(
                sellerId,
                brandId,
                null,
                categoryIds,
                null,
                null,
                null,
                null,
                searchWord,
                "createdAt",
                "DESC",
                cursorId,
                null,
                size);
    }

    // ===== ProductGroupSearchCriteria Fixtures =====

    public static ProductGroupSearchCriteria cursorSearchCriteria(int size) {
        CursorPageRequest<Long> cursorPageRequest = CursorPageRequest.first(size);
        CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                CursorQueryContext.of(
                        ProductGroupSortKey.defaultKey(), SortDirection.DESC, cursorPageRequest);
        return ProductGroupSearchCriteria.of(queryContext);
    }

    public static ProductGroupSearchCriteria cursorSearchCriteria() {
        return cursorSearchCriteria(20);
    }

    // ===== ProductGroupOffsetSearchCriteria Fixtures =====

    public static ProductGroupOffsetSearchCriteria offsetSearchCriteria() {
        return ProductGroupOffsetSearchCriteria.defaultCriteria();
    }

    public static ProductGroupOffsetSearchCriteria offsetSearchCriteria(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        QueryContext<ProductGroupSortKey> queryContext =
                QueryContext.of(ProductGroupSortKey.defaultKey(), SortDirection.DESC, pageRequest);
        return ProductGroupOffsetSearchCriteria.of(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                queryContext);
    }

    // ===== CommonSearchParams Helpers =====

    public static CommonSearchParams defaultCommonSearchParams() {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", 0, 20);
    }

    public static CommonSearchParams commonSearchParams(int page, int size) {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", page, size);
    }
}
