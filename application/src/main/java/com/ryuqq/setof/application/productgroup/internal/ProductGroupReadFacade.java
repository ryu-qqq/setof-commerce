package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCompositionReadManager;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupReadFacade - 상품그룹 Read Facade.
 *
 * <p>ReadManager들만 조합하여 조회 결과를 번들 DTO로 묶어 반환합니다. 조립(Assembling)은 Service에서 Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupReadFacade {

    private final ProductGroupCompositionReadManager compositionReadManager;
    private final ProductGroupReadManager productGroupReadManager;
    private final ProductReadManager productReadManager;

    public ProductGroupReadFacade(
            ProductGroupCompositionReadManager compositionReadManager,
            ProductGroupReadManager productGroupReadManager,
            ProductReadManager productReadManager) {
        this.compositionReadManager = compositionReadManager;
        this.productGroupReadManager = productGroupReadManager;
        this.productReadManager = productReadManager;
    }

    /**
     * 상품그룹 단건 상세 조회 (레거시).
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 Composite 결과
     * @throws ProductGroupNotFoundException 상품그룹이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public LegacyProductGroupDetailCompositeResult getDetailBundle(Long productGroupId) {
        return compositionReadManager
                .fetchProductGroupDetail(productGroupId)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));
    }

    /**
     * 상품그룹 상세 조회 번들 (Admin/Web 공용).
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 번들
     * @throws ProductGroupNotFoundException 상품그룹이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public ProductGroupDetailBundle getProductGroupDetailBundle(Long productGroupId) {
        ProductGroupId pgId = ProductGroupId.of(productGroupId);

        ProductGroupDetailCompositeQueryResult queryResult =
                compositionReadManager.getDetailComposite(productGroupId);

        ProductGroupDetailImageResults imageResults =
                compositionReadManager.fetchDetailImageResults(productGroupId);

        ProductGroup group = productGroupReadManager.getById(pgId);

        List<Product> products = productReadManager.findByProductGroupId(pgId);

        List<ProductNoticeEntryResult> noticeEntries =
                compositionReadManager.fetchNoticeEntries(queryResult.noticeId());

        List<DescriptionImageResult> descriptionImages =
                compositionReadManager.fetchDescriptionImages(queryResult.descriptionId());

        return new ProductGroupDetailBundle(
                queryResult, imageResults, group, products, noticeEntries, descriptionImages);
    }

    /**
     * 상품그룹 커서 페이징 목록 번들.
     *
     * @param criteria 검색 조건
     * @return 목록 결과 + 전체 건수 + sortKey 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundle(ProductGroupSearchCriteria criteria) {
        List<ProductGroupListCompositeResult> results =
                compositionReadManager.fetchThumbnailResults(criteria);
        long totalElements = compositionReadManager.fetchProductGroupCount(criteria);
        return new ProductGroupListBundle(
                results, totalElements, criteria.queryContext().sortKey());
    }

    /**
     * ID 목록 기반 상품그룹 목록 번들 (찜 목록 등).
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 목록 번들 (totalElements = 요청 ID 수)
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleByIds(List<Long> productGroupIds) {
        List<ProductGroupListCompositeResult> results =
                compositionReadManager.fetchThumbnailResultsByIds(productGroupIds);
        return new ProductGroupListBundle(results, productGroupIds.size(), null);
    }

    /**
     * 브랜드별 상품그룹 목록 번들.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 목록 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleByBrand(Long brandId, int pageSize) {
        List<ProductGroupListCompositeResult> results =
                compositionReadManager.fetchThumbnailResultsByBrand(brandId, pageSize);
        return new ProductGroupListBundle(results, results.size(), ProductGroupSortKey.SCORE);
    }

    /**
     * 셀러별 상품그룹 목록 번들.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 목록 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundleBySeller(Long sellerId, int pageSize) {
        List<ProductGroupListCompositeResult> results =
                compositionReadManager.fetchThumbnailResultsBySeller(sellerId, pageSize);
        return new ProductGroupListBundle(results, results.size(), ProductGroupSortKey.SCORE);
    }

    /**
     * 키워드 검색 결과 번들 (MySQL ngram FULLTEXT + 커서 페이징).
     *
     * @param criteria 검색 조건 (searchWord 포함)
     * @return 검색 결과 목록 + 전체 건수 + sortKey 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getSearchBundle(ProductGroupSearchCriteria criteria) {
        List<ProductGroupListCompositeResult> results =
                compositionReadManager.fetchSearchThumbnailResults(criteria);
        long totalElements = compositionReadManager.fetchSearchCount(criteria);
        return new ProductGroupListBundle(
                results, totalElements, criteria.queryContext().sortKey());
    }
}
