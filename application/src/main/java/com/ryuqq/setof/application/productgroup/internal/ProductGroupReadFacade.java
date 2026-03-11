package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCompositionReadManager;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupReadFacade - 상품그룹 Read Facade.
 *
 * <p>Manager들만 조합하여 조회 결과를 번들 DTO로 묶어 반환합니다. 조립(Assembling)은 Service에서 Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupReadFacade {

    private final ProductGroupCompositionReadManager compositionReadManager;

    public ProductGroupReadFacade(ProductGroupCompositionReadManager compositionReadManager) {
        this.compositionReadManager = compositionReadManager;
    }

    /**
     * 상품그룹 단건 상세 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 Composite 결과
     * @throws IllegalArgumentException 상품그룹이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public ProductGroupDetailCompositeResult getDetailBundle(Long productGroupId) {
        return compositionReadManager
                .fetchProductGroupDetail(productGroupId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "ProductGroup not found: " + productGroupId));
    }

    /**
     * 상품그룹 커서 페이징 목록 번들.
     *
     * @param criteria 검색 조건
     * @return 썸네일 목록 + 전체 건수 + sortKey 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundle(ProductGroupSearchCriteria criteria) {
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupThumbnails(criteria);
        long totalElements = compositionReadManager.fetchProductGroupCount(criteria);
        return new ProductGroupListBundle(
                thumbnails, totalElements, criteria.queryContext().sortKey());
    }

    /**
     * ID 목록 기반 상품그룹 썸네일 번들 (찜 목록 등).
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 썸네일 목록 번들 (totalElements = 요청 ID 수)
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleByIds(List<Long> productGroupIds) {
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupsByIds(productGroupIds);
        return new ProductGroupListBundle(thumbnails, productGroupIds.size(), null);
    }

    /**
     * 브랜드별 상품그룹 썸네일 번들.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleByBrand(Long brandId, int pageSize) {
        CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                CursorQueryContext.firstPage(ProductGroupSortKey.RECOMMEND, null, pageSize);
        ProductGroupSearchCriteria criteria =
                new ProductGroupSearchCriteria(
                        null,
                        BrandId.of(brandId),
                        null,
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        queryContext);
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupThumbnails(criteria);
        return new ProductGroupListBundle(
                thumbnails, thumbnails.size(), ProductGroupSortKey.RECOMMEND);
    }

    /**
     * 셀러별 상품그룹 썸네일 번들.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleBySeller(Long sellerId, int pageSize) {
        CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                CursorQueryContext.firstPage(ProductGroupSortKey.RECOMMEND, null, pageSize);
        ProductGroupSearchCriteria criteria =
                new ProductGroupSearchCriteria(
                        SellerId.of(sellerId),
                        null,
                        null,
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        queryContext);
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupThumbnails(criteria);
        return new ProductGroupListBundle(
                thumbnails, thumbnails.size(), ProductGroupSortKey.RECOMMEND);
    }

    /**
     * 키워드 검색 결과 번들 (MySQL ngram FULLTEXT + 커서 페이징).
     *
     * @param criteria 검색 조건 (searchWord 포함)
     * @return 검색 결과 목록 + 전체 건수 + sortKey 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getSearchBundle(ProductGroupSearchCriteria criteria) {
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchSearchResults(criteria);
        long totalElements = compositionReadManager.fetchSearchCount(criteria);
        return new ProductGroupListBundle(
                thumbnails, totalElements, criteria.queryContext().sortKey());
    }
}
