package com.ryuqq.setof.adapter.out.persistence.composite.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.composite.productgroup.repository.ProductGroupCompositeQueryDslRepository;
import com.ryuqq.setof.application.product.dto.response.ProductResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupEnrichmentResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupExcelBaseBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ImageWithVariantsResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupOffsetSearchCriteria;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ProductGroupCompositionQueryAdapter - 상품그룹 Composition 조회 Adapter.
 *
 * <p>새 스키마(setof) 크로스 도메인 조인을 통한 성능 최적화된 조회 구현.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Port 인터페이스 구현.
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.productgroup.enabled", havingValue = "false")
public class ProductGroupCompositionQueryAdapter implements ProductGroupCompositeQueryPort {

    private final ProductGroupCompositeQueryDslRepository compositeRepository;

    public ProductGroupCompositionQueryAdapter(
            ProductGroupCompositeQueryDslRepository compositeRepository) {
        this.compositeRepository = compositeRepository;
    }

    @Override
    public Optional<ProductGroupListCompositeResult> findCompositeById(Long productGroupId) {
        return compositeRepository.findCompositeById(productGroupId);
    }

    @Override
    public List<ProductGroupListCompositeResult> findCompositeByCriteria(
            ProductGroupOffsetSearchCriteria criteria) {
        return compositeRepository.findCompositeByCriteria(criteria);
    }

    @Override
    public long countByCriteria(ProductGroupOffsetSearchCriteria criteria) {
        return compositeRepository.countByCriteria(criteria);
    }

    @Override
    public List<ProductGroupEnrichmentResult> findEnrichmentsByProductGroupIds(
            List<Long> productGroupIds) {
        return compositeRepository.findEnrichmentsByProductGroupIds(productGroupIds);
    }

    @Override
    public Optional<ProductGroupDetailCompositeQueryResult> findDetailCompositeById(
            Long productGroupId) {
        return compositeRepository.findDetailCompositeById(productGroupId);
    }

    @Override
    public ProductGroupExcelBaseBundle findExcelBaseBundleByCriteria(
            ProductGroupOffsetSearchCriteria criteria) {
        return compositeRepository.findExcelBaseBundleByCriteria(criteria);
    }

    @Override
    public Map<Long, List<ProductResult>> findProductsWithOptionNamesByProductGroupIds(
            List<Long> productGroupIds) {
        return compositeRepository.findProductsWithOptionNamesByProductGroupIds(productGroupIds);
    }

    @Override
    public List<ImageWithVariantsResult> findImagesWithVariantsByProductGroupId(
            Long productGroupId) {
        return compositeRepository.findImagesWithVariantsByProductGroupId(productGroupId);
    }

    @Override
    public List<ProductNoticeEntryResult> findNoticeEntriesByNoticeId(Long noticeId) {
        return compositeRepository.findNoticeEntriesByNoticeId(noticeId);
    }

    @Override
    public List<DescriptionImageResult> findDescriptionImagesByDescriptionId(Long descriptionId) {
        return compositeRepository.findDescriptionImagesByDescriptionId(descriptionId);
    }
}
