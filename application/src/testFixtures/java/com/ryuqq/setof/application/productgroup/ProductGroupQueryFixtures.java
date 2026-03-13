package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.common.dto.query.CommonCursorParams;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    // ===== ProductGroupThumbnailCompositeResult =====

    public static ProductGroupThumbnailCompositeResult thumbnailCompositeResult(
            Long productGroupId) {
        return new ProductGroupThumbnailCompositeResult(
                productGroupId,
                1L,
                "테스트 상품그룹",
                1L,
                "테스트브랜드",
                "테스트 브랜드",
                "Test Brand",
                "http://example.com/brand-icon.png",
                "http://example.com/thumb.jpg",
                50000,
                45000,
                40000,
                10,
                5000,
                20,
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                4.5,
                100L,
                95.5,
                "Y",
                "N");
    }

    public static List<ProductGroupThumbnailCompositeResult> thumbnailCompositeResults() {
        return List.of(thumbnailCompositeResult(1L), thumbnailCompositeResult(2L));
    }

    // ===== ProductGroupListBundle =====

    public static ProductGroupListBundle listBundle() {
        return new ProductGroupListBundle(thumbnailCompositeResults(), 2L, "RECOMMEND");
    }

    public static ProductGroupListBundle emptyListBundle() {
        return new ProductGroupListBundle(List.of(), 0L, "RECOMMEND");
    }

    public static ProductGroupListBundle listBundleWithSize(int size) {
        List<ProductGroupThumbnailCompositeResult> items =
                java.util.stream.LongStream.rangeClosed(1, size)
                        .mapToObj(ProductGroupQueryFixtures::thumbnailCompositeResult)
                        .toList();
        return new ProductGroupListBundle(items, (long) size, "RECOMMEND");
    }

    // ===== ProductGroupDetailBundle =====

    public static ProductGroupDetailBundle detailBundle(Long productGroupId) {
        return new ProductGroupDetailBundle(
                ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(productGroupId),
                ProductGroupFixtures.activeProductGroup(productGroupId),
                List.of(),
                Optional.empty(),
                Optional.empty());
    }
}
