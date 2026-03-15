package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.common.dto.query.CommonCursorParams;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.List;

/**
 * ProductGroup Application Query 테스트 Fixtures.
 *
 * <p>ProductGroup 조회 관련 Query/Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupQueryFixtures {

    private ProductGroupQueryFixtures() {}

    // ===== ProductGroupSearchParams =====

    public static ProductGroupSearchParams defaultSearchParams() {
        return new ProductGroupSearchParams(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                CommonCursorParams.first(20));
    }

    public static ProductGroupSearchParams searchParamsWithSize(int size) {
        return new ProductGroupSearchParams(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "RECOMMEND",
                CommonCursorParams.first(size));
    }

    public static ProductGroupSearchParams searchParamsWithKeyword(String keyword) {
        return new ProductGroupSearchParams(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                keyword,
                null,
                CommonCursorParams.first(20));
    }

    public static ProductGroupSearchParams searchParamsWithCursor(String cursor, int size) {
        return new ProductGroupSearchParams(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "RECOMMEND",
                new CommonCursorParams(cursor, size));
    }

    // ===== ProductGroupListCompositeResult =====

    public static ProductGroupListCompositeResult listCompositeResult(Long productGroupId) {
        return ProductGroupCompositeQueryFixtures.baseCompositeResult(productGroupId);
    }

    public static List<ProductGroupListCompositeResult> listCompositeResults() {
        return List.of(listCompositeResult(1L), listCompositeResult(2L));
    }

    // ===== ProductGroupListBundle =====

    public static ProductGroupListBundle listBundle() {
        return new ProductGroupListBundle(
                listCompositeResults(), 2L, ProductGroupSortKey.CREATED_AT);
    }

    public static ProductGroupListBundle emptyListBundle() {
        return new ProductGroupListBundle(List.of(), 0L, ProductGroupSortKey.CREATED_AT);
    }

    public static ProductGroupListBundle listBundleWithSize(int size) {
        List<ProductGroupListCompositeResult> items =
                java.util.stream.LongStream.rangeClosed(1, size)
                        .mapToObj(ProductGroupQueryFixtures::listCompositeResult)
                        .toList();
        return new ProductGroupListBundle(items, (long) size, ProductGroupSortKey.CREATED_AT);
    }

    // ===== ProductGroupDetailBundle =====

    public static ProductGroupDetailBundle detailBundle(Long productGroupId) {
        return new ProductGroupDetailBundle(
                ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(productGroupId),
                ProductGroupDetailImageResults.create(List.of()),
                ProductGroupFixtures.activeProductGroup(productGroupId),
                List.of(),
                List.of(),
                List.of());
    }
}
