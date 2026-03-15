package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.LegacyProductGroupWebQueryPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupCompositionReadManager - 상품그룹 Composite 조회 Manager.
 *
 * <p>레거시 DB(Web 조회) + 새 DB(Admin/상세/배치) 크로스 도메인 JOIN 조회를 통합 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupCompositionReadManager {

    private final LegacyProductGroupWebQueryPort legacyWebQueryPort;
    private final ProductGroupCompositeQueryPort compositeQueryPort;

    public ProductGroupCompositionReadManager(
            LegacyProductGroupWebQueryPort legacyWebQueryPort,
            ProductGroupCompositeQueryPort compositeQueryPort) {
        this.legacyWebQueryPort = legacyWebQueryPort;
        this.compositeQueryPort = compositeQueryPort;
    }

    // ── 레거시 Web 조회 ──

    /** 상품그룹 단건 상세 조회 (레거시). */
    @Transactional(readOnly = true)
    public Optional<LegacyProductGroupDetailCompositeResult> fetchProductGroupDetail(
            Long productGroupId) {
        return legacyWebQueryPort.fetchProductGroupDetail(productGroupId);
    }

    /** 상품그룹 썸네일 목록 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupListCompositeResult> fetchThumbnailResults(
            ProductGroupSearchCriteria criteria) {
        return legacyWebQueryPort.fetchProductGroupThumbnails(criteria);
    }

    /** 상품그룹 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchProductGroupCount(ProductGroupSearchCriteria criteria) {
        return legacyWebQueryPort.fetchProductGroupCount(criteria);
    }

    /** ID 목록 기반 상품그룹 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupListCompositeResult> fetchThumbnailResultsByIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }
        return legacyWebQueryPort.fetchProductGroupsByIds(productGroupIds);
    }

    /** 브랜드별 상품그룹 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupListCompositeResult> fetchThumbnailResultsByBrand(
            Long brandId, int pageSize) {
        return legacyWebQueryPort.fetchProductGroupsByBrand(brandId, pageSize);
    }

    /** 셀러별 상품그룹 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupListCompositeResult> fetchThumbnailResultsBySeller(
            Long sellerId, int pageSize) {
        return legacyWebQueryPort.fetchProductGroupsBySeller(sellerId, pageSize);
    }

    /** 키워드 검색 결과 목록 조회. */
    @Transactional(readOnly = true)
    public List<ProductGroupListCompositeResult> fetchSearchThumbnailResults(
            ProductGroupSearchCriteria criteria) {
        return legacyWebQueryPort.fetchSearchResults(criteria);
    }

    /** 키워드 검색 전체 건수 조회. */
    @Transactional(readOnly = true)
    public long fetchSearchCount(ProductGroupSearchCriteria criteria) {
        return legacyWebQueryPort.fetchSearchCount(criteria);
    }

    // ── 새 DB: 상세 통합 쿼리 ──

    /** 상세용 1:1 Composite 조회 (PG+정책+Description+Notice 헤더). */
    @Transactional(readOnly = true)
    public ProductGroupDetailCompositeQueryResult getDetailComposite(Long productGroupId) {
        return compositeQueryPort
                .findDetailCompositeById(productGroupId)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));
    }

    /** 이미지 + Variant 통합 조회 (내부에서 LARGE_WEBP variant URL 해석). */
    @Transactional(readOnly = true)
    public ProductGroupDetailImageResults fetchDetailImageResults(Long productGroupId) {
        return ProductGroupDetailImageResults.create(
                compositeQueryPort.findImagesWithVariantsByProductGroupId(productGroupId));
    }

    /** 고시정보 항목 배치 조회. */
    @Transactional(readOnly = true)
    public List<ProductNoticeEntryResult> fetchNoticeEntries(Long noticeId) {
        return compositeQueryPort.findNoticeEntriesByNoticeId(noticeId);
    }

    /** 상세설명 이미지 배치 조회. */
    @Transactional(readOnly = true)
    public List<DescriptionImageResult> fetchDescriptionImages(Long descriptionId) {
        return compositeQueryPort.findDescriptionImagesByDescriptionId(descriptionId);
    }
}
