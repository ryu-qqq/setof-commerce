package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.product.port.out.query.ProductQueryPort;
import com.ryuqq.setof.application.productdescription.port.out.query.ProductGroupDescriptionQueryPort;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCompositionReadManager;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupReadFacade - 상품그룹 Read Facade.
 *
 * <p>Manager들만 조합하여 조회 결과를 번들 DTO로 묶어 반환합니다. 조립(Assembling)은 Service에서 Assembler를 통해 수행합니다.
 *
 * <p>Redis 캐시 없이 모든 조회는 레거시 DB 직접 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupReadFacade {

    private final ProductGroupCompositionReadManager compositionReadManager;
    private final ProductGroupCompositeQueryPort compositeQueryPort;
    private final ProductGroupQueryPort productGroupQueryPort;
    private final ProductQueryPort productQueryPort;
    private final ProductGroupDescriptionQueryPort descriptionQueryPort;
    private final ProductNoticeQueryPort noticeQueryPort;

    public ProductGroupReadFacade(
            ProductGroupCompositionReadManager compositionReadManager,
            ProductGroupCompositeQueryPort compositeQueryPort,
            ProductGroupQueryPort productGroupQueryPort,
            ProductQueryPort productQueryPort,
            ProductGroupDescriptionQueryPort descriptionQueryPort,
            ProductNoticeQueryPort noticeQueryPort) {
        this.compositionReadManager = compositionReadManager;
        this.compositeQueryPort = compositeQueryPort;
        this.productGroupQueryPort = productGroupQueryPort;
        this.productQueryPort = productQueryPort;
        this.descriptionQueryPort = descriptionQueryPort;
        this.noticeQueryPort = noticeQueryPort;
    }

    /**
     * 상품그룹 단건 상세 조회.
     *
     * <p>기본 정보(쿼리 1) + 개별 상품 목록(쿼리 2) + 이미지 목록(쿼리 3)을 조합합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 Composite 결과
     * @throws IllegalArgumentException 상품그룹이 존재하지 않을 경우
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
     * <p>Composition 쿼리(기본 정보 + 정책) + Aggregate(이미지, 옵션 구조) + Products + Description + Notice를
     * 조합합니다. Assembler에서 용도별(Admin/Web)로 다르게 조립합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 번들
     * @throws ProductGroupNotFoundException 상품그룹이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public ProductGroupDetailBundle getProductGroupDetailBundle(Long productGroupId) {
        ProductGroupId pgId = ProductGroupId.of(productGroupId);

        ProductGroupDetailCompositeQueryResult queryResult =
                compositeQueryPort
                        .findDetailCompositeById(productGroupId)
                        .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));

        ProductGroup group =
                productGroupQueryPort
                        .findById(pgId)
                        .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));

        List<Product> products = productQueryPort.findByProductGroupId(pgId);

        Optional<ProductGroupDescription> description =
                descriptionQueryPort.findByProductGroupId(pgId);

        Optional<ProductNotice> notice = noticeQueryPort.findByProductGroupId(pgId);

        return new ProductGroupDetailBundle(queryResult, group, products, description, notice);
    }

    /**
     * 상품그룹 커서 페이징 목록 번들.
     *
     * @param condition 검색 조건
     * @return 썸네일 목록 + 전체 건수 + orderType 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getListBundle(LegacyProductGroupSearchCondition condition) {
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupThumbnails(condition);
        long totalElements = compositionReadManager.fetchProductGroupCount(condition);
        return new ProductGroupListBundle(thumbnails, totalElements, condition.orderType());
    }

    /**
     * ID 목록 기반 상품그룹 썸네일 번들 (찜 목록 등).
     *
     * <p>요청 ID 순서로 재정렬된 결과를 반환합니다.
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
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupsByBrand(brandId, pageSize);
        return new ProductGroupListBundle(thumbnails, thumbnails.size(), "RECOMMEND");
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
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchProductGroupsBySeller(sellerId, pageSize);
        return new ProductGroupListBundle(thumbnails, thumbnails.size(), "RECOMMEND");
    }

    /**
     * 키워드 검색 결과 번들 (MySQL ngram FULLTEXT + 커서 페이징).
     *
     * @param condition 검색 조건 (searchWord 포함)
     * @return 검색 결과 목록 + 전체 건수 + orderType 번들
     */
    @Transactional(readOnly = true)
    public ProductGroupListBundle getSearchBundle(LegacySearchCondition condition) {
        List<ProductGroupThumbnailCompositeResult> thumbnails =
                compositionReadManager.fetchSearchResults(condition);
        long totalElements = compositionReadManager.fetchSearchCount(condition);
        return new ProductGroupListBundle(thumbnails, totalElements, condition.orderType());
    }
}
