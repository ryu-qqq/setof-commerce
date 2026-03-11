package com.ryuqq.setof.storage.legacy.composite.productgroup.adapter;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositionQueryPort;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.mapper.LegacyWebProductGroupMapper;
import com.ryuqq.setof.storage.legacy.composite.productgroup.repository.LegacyWebProductGroupCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.composite.search.mapper.LegacyWebSearchMapper;
import com.ryuqq.setof.storage.legacy.composite.search.repository.LegacyWebSearchCompositeQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupCompositeQueryAdapter - 레거시 Web 상품그룹 Composite 조회 Adapter.
 *
 * <p>ProductGroupCompositionQueryPort를 구현하여 Application Layer에 상품그룹 조회 기능을 제공합니다.
 *
 * <p>새로운 ProductGroupSearchCriteria를 받아 내부적으로 레거시 조건 객체로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupCompositeQueryAdapter
        implements ProductGroupCompositionQueryPort {

    private final LegacyWebProductGroupCompositeQueryDslRepository productGroupRepository;
    private final LegacyWebSearchCompositeQueryDslRepository searchRepository;
    private final LegacyWebProductGroupMapper productGroupMapper;
    private final LegacyWebSearchMapper searchMapper;

    public LegacyWebProductGroupCompositeQueryAdapter(
            LegacyWebProductGroupCompositeQueryDslRepository productGroupRepository,
            LegacyWebSearchCompositeQueryDslRepository searchRepository,
            LegacyWebProductGroupMapper productGroupMapper,
            LegacyWebSearchMapper searchMapper) {
        this.productGroupRepository = productGroupRepository;
        this.searchRepository = searchRepository;
        this.productGroupMapper = productGroupMapper;
        this.searchMapper = searchMapper;
    }

    @Override
    public Optional<ProductGroupDetailCompositeResult> fetchProductGroupDetail(
            Long productGroupId) {
        Optional<LegacyWebProductGroupBasicQueryDto> basicOpt =
                productGroupRepository.fetchBasicInfo(productGroupId);
        if (basicOpt.isEmpty()) {
            return Optional.empty();
        }
        LegacyWebProductGroupBasicQueryDto basic = basicOpt.get();
        List<LegacyWebProductQueryDto> products =
                productGroupRepository.fetchProducts(productGroupId);
        List<LegacyWebProductImageQueryDto> images =
                productGroupRepository.fetchImages(productGroupId);
        return Optional.of(productGroupMapper.toDetailCompositeResult(basic, products, images));
    }

    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupThumbnails(
            ProductGroupSearchCriteria criteria) {
        LegacyProductGroupSearchCondition condition = toLegacyCondition(criteria);
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupThumbnails(condition);
        return productGroupMapper.toThumbnailCompositeResults(dtos);
    }

    @Override
    public long fetchProductGroupCount(ProductGroupSearchCriteria criteria) {
        LegacyProductGroupSearchCondition condition = toLegacyCondition(criteria);
        return productGroupRepository.fetchProductGroupCount(condition);
    }

    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsByIds(productGroupIds);
        List<ProductGroupThumbnailCompositeResult> results =
                productGroupMapper.toThumbnailCompositeResults(dtos);
        return productGroupMapper.reOrder(productGroupIds, results);
    }

    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchSearchResults(
            ProductGroupSearchCriteria criteria) {
        LegacySearchCondition condition = toLegacySearchCondition(criteria);
        return searchMapper.toThumbnailCompositeResults(
                searchRepository.fetchSearchResults(condition));
    }

    @Override
    public long fetchSearchCount(ProductGroupSearchCriteria criteria) {
        LegacySearchCondition condition = toLegacySearchCondition(criteria);
        return searchRepository.fetchSearchCount(condition);
    }

    // ========== Criteria → Legacy Condition 변환 ==========

    private LegacyProductGroupSearchCondition toLegacyCondition(
            ProductGroupSearchCriteria criteria) {
        return new LegacyProductGroupSearchCondition(
                null,
                null,
                criteria.queryContext().hasCursor() ? criteria.queryContext().cursor() : null,
                criteria.cursorValue(),
                criteria.lowestPrice(),
                criteria.highestPrice(),
                criteria.hasCategoryId() ? criteria.categoryId().value() : null,
                criteria.hasBrandId() ? criteria.brandId().value() : null,
                criteria.hasSellerId() ? criteria.sellerId().value() : null,
                criteria.categoryIds(),
                criteria.brandIds(),
                toLegacyOrderType(criteria),
                criteria.queryContext().size());
    }

    private LegacySearchCondition toLegacySearchCondition(ProductGroupSearchCriteria criteria) {
        return new LegacySearchCondition(
                criteria.searchWord(),
                null,
                criteria.queryContext().hasCursor() ? criteria.queryContext().cursor() : null,
                criteria.cursorValue(),
                criteria.lowestPrice(),
                criteria.highestPrice(),
                criteria.hasCategoryId() ? criteria.categoryId().value() : null,
                criteria.hasBrandId() ? criteria.brandId().value() : null,
                criteria.hasSellerId() ? criteria.sellerId().value() : null,
                criteria.categoryIds(),
                criteria.brandIds(),
                toLegacyOrderType(criteria),
                criteria.queryContext().size());
    }

    private String toLegacyOrderType(ProductGroupSearchCriteria criteria) {
        ProductGroupSortKey sortKey = criteria.queryContext().sortKey();
        boolean ascending = criteria.queryContext().isAscending();

        return switch (sortKey) {
            case RECOMMEND -> "RECOMMEND";
            case REVIEW -> "REVIEW";
            case RATING -> "HIGH_RATING";
            case CURRENT_PRICE, SALE_PRICE -> ascending ? "LOW_PRICE" : "HIGH_PRICE";
            case DISCOUNT -> ascending ? "LOW_DISCOUNT" : "HIGH_DISCOUNT";
            case RECENT, CREATED_AT -> "RECENT";
            case UPDATED_AT -> "RECENT";
            case NAME -> "RECOMMEND";
        };
    }
}
