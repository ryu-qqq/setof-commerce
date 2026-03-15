package com.ryuqq.setof.storage.legacy.composite.productgroup.adapter;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.LegacyProductGroupWebQueryPort;
import com.ryuqq.setof.domain.common.vo.SortDirection;
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
 * <p>LegacyProductGroupWebQueryPort를 구현하여 Application Layer에 상품그룹 조회 기능을 제공합니다.
 *
 * <p>ProductGroupSearchCriteria → LegacyProductGroupSearchCondition 변환을 내부에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupCompositeQueryAdapter implements LegacyProductGroupWebQueryPort {

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
    public Optional<LegacyProductGroupDetailCompositeResult> fetchProductGroupDetail(
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
    public List<ProductGroupListCompositeResult> fetchProductGroupThumbnails(
            ProductGroupSearchCriteria criteria) {
        LegacyProductGroupSearchCondition condition = toLegacyCondition(criteria);
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupThumbnails(condition);
        return productGroupMapper.toListCompositeResults(dtos);
    }

    @Override
    public long fetchProductGroupCount(ProductGroupSearchCriteria criteria) {
        LegacyProductGroupSearchCondition condition = toLegacyCondition(criteria);
        return productGroupRepository.fetchProductGroupCount(condition);
    }

    @Override
    public List<ProductGroupListCompositeResult> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsByIds(productGroupIds);
        List<ProductGroupListCompositeResult> results =
                productGroupMapper.toListCompositeResults(dtos);
        return productGroupMapper.reOrder(productGroupIds, results);
    }

    @Override
    public List<ProductGroupListCompositeResult> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsByBrand(brandId, pageSize);
        return productGroupMapper.toListCompositeResults(dtos);
    }

    @Override
    public List<ProductGroupListCompositeResult> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsBySeller(sellerId, pageSize);
        return productGroupMapper.toListCompositeResults(dtos);
    }

    @Override
    public List<ProductGroupListCompositeResult> fetchSearchResults(
            ProductGroupSearchCriteria criteria) {
        LegacySearchCondition condition = toLegacySearchCondition(criteria);
        return searchMapper.toListCompositeResults(searchRepository.fetchSearchResults(condition));
    }

    @Override
    public long fetchSearchCount(ProductGroupSearchCriteria criteria) {
        LegacySearchCondition condition = toLegacySearchCondition(criteria);
        return searchRepository.fetchSearchCount(condition);
    }

    // ── 변환 ──

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
                toOrderType(criteria),
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
                toOrderType(criteria),
                criteria.queryContext().size());
    }

    private String toOrderType(ProductGroupSearchCriteria criteria) {
        ProductGroupSortKey sortKey = criteria.queryContext().sortKey();
        SortDirection direction = criteria.queryContext().sortDirection();

        return switch (sortKey) {
            case SCORE -> "RECOMMEND";
            case REVIEW_COUNT -> "REVIEW";
            case AVERAGE_RATING -> "HIGH_RATING";
            case SALE_PRICE -> direction.isAscending() ? "LOW_PRICE" : "HIGH_PRICE";
            case DISCOUNT_RATE -> direction.isAscending() ? "LOW_DISCOUNT" : "HIGH_DISCOUNT";
            case CREATED_AT -> "RECENT";
            default -> "RECOMMEND";
        };
    }
}
