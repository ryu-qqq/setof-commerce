package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositionQueryPort;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupCompositionReadManager - 상품그룹 Composite 조회 Manager.
 *
 * <p>크로스 도메인 JOIN을 통한 성능 최적화된 조회를 담당합니다. ProductGroupCompositionQueryPort를 의존하여 DB를 조회합니다.
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
            ProductGroupSearchCriteria criteria) {
        return compositionQueryPort.fetchProductGroupThumbnails(criteria);
    }

    /** 상품그룹 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchProductGroupCount(ProductGroupSearchCriteria criteria) {
        return compositionQueryPort.fetchProductGroupCount(criteria);
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

    /** 키워드 검색 결과 목록 조회 (FULLTEXT). */
    @Transactional(readOnly = true)
    public List<ProductGroupThumbnailCompositeResult> fetchSearchResults(
            ProductGroupSearchCriteria criteria) {
        return compositionQueryPort.fetchSearchResults(criteria);
    }

    /** 키워드 검색 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchSearchCount(ProductGroupSearchCriteria criteria) {
        return compositionQueryPort.fetchSearchCount(criteria);
    }
}
