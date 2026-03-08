package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositionQueryPort;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupCompositionReadManager - 상품그룹 Composite 조회 Manager.
 *
 * <p>크로스 도메인 JOIN을 통한 성능 최적화된 조회를 담당합니다. ProductGroupCompositionQueryPort를 의존하여 레거시 DB를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupCompositionReadManager {

    private final ProductGroupCompositionQueryPort compositionQueryPort;

    public ProductGroupCompositionReadManager(
            ProductGroupCompositionQueryPort compositionQueryPort) {
        this.compositionQueryPort = compositionQueryPort;
    }

    /** 상품그룹 단건 상세 조회. */
    @Transactional(readOnly = true)
    public Optional<ProductGroupDetailCompositeResult> fetchProductGroupDetail(
            Long productGroupId) {
        return compositionQueryPort.fetchProductGroupDetail(productGroupId);
    }

    /** 상품그룹 썸네일 목록 조회 (커서 페이징). */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition) {
        return compositionQueryPort.fetchProductGroupThumbnails(condition);
    }

    /** 상품그룹 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchProductGroupCount(LegacyProductGroupSearchCondition condition) {
        return compositionQueryPort.fetchProductGroupCount(condition);
    }

    /** ID 목록 기반 상품그룹 썸네일 조회 (찜 목록 등). */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }
        return compositionQueryPort.fetchProductGroupsByIds(productGroupIds);
    }

    /** 브랜드별 상품그룹 썸네일 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        return compositionQueryPort.fetchProductGroupsByBrand(brandId, pageSize);
    }

    /** 셀러별 상품그룹 썸네일 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        return compositionQueryPort.fetchProductGroupsBySeller(sellerId, pageSize);
    }

    /** 키워드 검색 결과 목록 조회 (FULLTEXT). */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchSearchResults(
            LegacySearchCondition condition) {
        return compositionQueryPort.fetchSearchResults(condition);
    }

    /** 키워드 검색 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchSearchCount(LegacySearchCondition condition) {
        return compositionQueryPort.fetchSearchCount(condition);
    }
}
