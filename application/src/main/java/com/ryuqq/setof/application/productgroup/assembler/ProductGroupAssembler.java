package com.ryuqq.setof.application.productgroup.assembler;

import com.ryuqq.setof.application.product.dto.response.ProductDetailResult;
import com.ryuqq.setof.application.product.dto.response.ResolvedProductOptionResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.WebProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupThumbnailResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductOptionMatrixResult;
import com.ryuqq.setof.application.productgroup.dto.response.SellerOptionGroupResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.ProductGroupDescriptionResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ProductGroupImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * ProductGroupAssembler - 상품그룹 Result DTO 조립.
 *
 * <p>ReadFacade에서 조회한 번들 데이터를 조합하여 최종 응답 객체를 생성합니다. 조립 로직은 Service에서 이 Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupAssembler {

    /**
     * 커서 기반 목록 번들 → 슬라이스 결과 조립.
     *
     * <p>pageSize + 1개 조회한 결과에서 hasNext를 판별하고, 커서를 결정합니다.
     *
     * @param bundle 목록 번들 (results + totalElements + sortKey)
     * @param requestedSize 요청 페이지 크기
     * @return 슬라이스 결과
     */
    public ProductGroupSliceResult toSliceResult(ProductGroupListBundle bundle, int requestedSize) {
        if (bundle.isEmpty()) {
            return ProductGroupSliceResult.empty(requestedSize);
        }

        List<ProductGroupListCompositeResult> allResults = bundle.results();
        boolean hasNext = allResults.size() > requestedSize;
        List<ProductGroupListCompositeResult> rawContent =
                hasNext ? allResults.subList(0, requestedSize) : allResults;

        List<ProductGroupThumbnailResult> content =
                rawContent.stream().map(ProductGroupThumbnailResult::from).toList();

        String nextCursor = resolveNextCursor(rawContent, bundle.sortKey());
        SliceMeta sliceMeta =
                SliceMeta.withCursor(nextCursor, requestedSize, hasNext, content.size());

        return ProductGroupSliceResult.of(content, sliceMeta, bundle.totalElements());
    }

    /**
     * 다음 커서 값 결정.
     *
     * <p>마지막 아이템의 id를 lastDomainId로 사용하고, sortKey에 따라 cursorValue를 결정합니다.
     */
    private String resolveNextCursor(
            List<ProductGroupListCompositeResult> content, ProductGroupSortKey sortKey) {
        if (content.isEmpty()) {
            return null;
        }

        ProductGroupListCompositeResult last = content.get(content.size() - 1);
        long lastDomainId = last.id();

        String cursorValue = resolveCursorValue(last, sortKey);

        if (cursorValue != null) {
            return lastDomainId + "," + cursorValue;
        }
        return String.valueOf(lastDomainId);
    }

    /** SortKey에 따른 cursorValue 결정. */
    private String resolveCursorValue(
            ProductGroupListCompositeResult item, ProductGroupSortKey sortKey) {
        if (sortKey == null) {
            return null;
        }
        return switch (sortKey) {
            case SALE_PRICE -> String.valueOf(item.salePrice());
            case DISCOUNT_RATE -> String.valueOf(item.discountRate());
            case CREATED_AT -> item.createdAt() != null ? item.createdAt().toString() : null;
            default -> null;
        };
    }

    /**
     * 상세 번들 → Admin DetailCompositeResult 조립.
     *
     * <p>Admin 조회에서는 variant URL 치환을 하지 않고 원본 이미지 URL을 그대로 사용합니다.
     */
    public ProductGroupDetailCompositeResult toDetailResult(ProductGroupDetailBundle bundle) {
        ProductGroupDetailCompositeQueryResult queryResult = bundle.queryResult();

        List<ProductGroupImageResult> images =
                bundle.imageResults().images().stream().map(ProductGroupImageResult::from).toList();

        ProductOptionMatrixResult optionProductMatrix =
                buildOptionProductMatrix(bundle.group(), bundle.products());

        ProductGroupDescriptionResult descriptionResult = buildDescriptionResult(bundle);
        ProductNoticeResult noticeResult = buildNoticeResult(bundle);

        return new ProductGroupDetailCompositeResult(
                queryResult.id(),
                queryResult.sellerId(),
                queryResult.sellerName(),
                queryResult.brandId(),
                queryResult.brandName(),
                queryResult.categoryId(),
                queryResult.categoryName(),
                queryResult.categoryPath(),
                queryResult.productGroupName(),
                queryResult.optionType(),
                queryResult.status(),
                queryResult.createdAt(),
                queryResult.updatedAt(),
                images,
                optionProductMatrix,
                queryResult.shippingPolicy(),
                queryResult.refundPolicy(),
                descriptionResult,
                noticeResult);
    }

    /**
     * 상세 번들 → 웹(사용자) DetailCompositeResult 조립.
     *
     * <p>번들에 포함된 variant URL 맵으로 이미지 URL을 enrichment합니다.
     */
    public WebProductGroupDetailCompositeResult toWebDetailResult(ProductGroupDetailBundle bundle) {
        ProductGroupDetailCompositeQueryResult queryResult = bundle.queryResult();

        ProductGroupDetailImageResults imageResults = bundle.imageResults();
        List<ProductGroupImageResult> images = enrichDetailImages(imageResults);

        ProductOptionMatrixResult optionProductMatrix =
                buildOptionProductMatrix(bundle.group(), bundle.products());

        ProductGroupDescriptionResult descriptionResult = buildDescriptionResult(bundle);
        ProductNoticeResult noticeResult = buildNoticeResult(bundle);

        return new WebProductGroupDetailCompositeResult(
                queryResult.id(),
                queryResult.sellerId(),
                queryResult.sellerName(),
                queryResult.brandId(),
                queryResult.brandName(),
                queryResult.brandIconImageUrl(),
                queryResult.categoryId(),
                queryResult.categoryName(),
                queryResult.categoryPath(),
                queryResult.productGroupName(),
                queryResult.optionType(),
                queryResult.status(),
                0.0,
                0L,
                queryResult.createdAt(),
                queryResult.updatedAt(),
                images,
                optionProductMatrix,
                queryResult.shippingPolicy(),
                queryResult.refundPolicy(),
                descriptionResult,
                noticeResult);
    }

    /**
     * ProductGroup + Products → ProductDetailResult 목록.
     *
     * <p>옵션 매핑을 resolved하여 각 Product를 ProductDetailResult로 변환합니다.
     */
    public List<ProductDetailResult> toProductDetailResults(
            ProductGroup group, List<Product> products) {
        Map<Long, ResolvedProductOptionResult> optionValueMap = buildOptionValueMap(group);
        return toProductDetailResults(products, optionValueMap);
    }

    private ProductOptionMatrixResult buildOptionProductMatrix(
            ProductGroup group, List<Product> products) {
        List<SellerOptionGroupResult> optionGroups =
                group.sellerOptionGroups().stream().map(SellerOptionGroupResult::from).toList();

        Map<Long, ResolvedProductOptionResult> optionValueMap = buildOptionValueMap(group);
        List<ProductDetailResult> productDetails = toProductDetailResults(products, optionValueMap);

        return new ProductOptionMatrixResult(optionGroups, productDetails);
    }

    private Map<Long, ResolvedProductOptionResult> buildOptionValueMap(ProductGroup group) {
        Map<Long, ResolvedProductOptionResult> map = new HashMap<>();
        for (SellerOptionGroup optionGroup : group.sellerOptionGroups()) {
            for (SellerOptionValue optionValue : optionGroup.optionValues()) {
                map.put(
                        optionValue.idValue(),
                        new ResolvedProductOptionResult(
                                optionGroup.idValue(),
                                optionGroup.optionGroupNameValue(),
                                optionValue.idValue(),
                                optionValue.optionValueNameValue()));
            }
        }
        return map;
    }

    private List<ProductDetailResult> toProductDetailResults(
            List<Product> products, Map<Long, ResolvedProductOptionResult> optionValueMap) {
        return products.stream()
                .map(product -> toProductDetailResult(product, optionValueMap))
                .toList();
    }

    private ProductDetailResult toProductDetailResult(
            Product product, Map<Long, ResolvedProductOptionResult> optionValueMap) {
        List<ResolvedProductOptionResult> options =
                product.optionMappings().stream()
                        .map(mapping -> optionValueMap.get(mapping.sellerOptionValueIdValue()))
                        .filter(Objects::nonNull)
                        .toList();

        return new ProductDetailResult(
                product.idValue(),
                product.skuCodeValue(),
                product.regularPriceValue(),
                product.currentPriceValue(),
                product.salePriceValue(),
                product.discountRate(),
                product.stockQuantity(),
                product.status().name(),
                product.sortOrder(),
                options,
                product.createdAt(),
                product.updatedAt());
    }

    // ========== Description / Notice 조립 ==========

    /** 1:1 쿼리 결과 + 1:N 배치 결과에서 Description Result를 조립합니다. */
    private ProductGroupDescriptionResult buildDescriptionResult(ProductGroupDetailBundle bundle) {
        Long descriptionId = bundle.queryResult().descriptionId();
        if (descriptionId == null) {
            return null;
        }
        return new ProductGroupDescriptionResult(
                descriptionId,
                bundle.queryResult().descriptionContent(),
                bundle.queryResult().descriptionCdnPath(),
                bundle.descriptionImages());
    }

    /** 1:1 쿼리 결과 + 1:N 배치 결과에서 Notice Result를 조립합니다. */
    private ProductNoticeResult buildNoticeResult(ProductGroupDetailBundle bundle) {
        Long noticeId = bundle.queryResult().noticeId();
        if (noticeId == null) {
            return null;
        }
        return new ProductNoticeResult(
                noticeId,
                bundle.noticeEntries(),
                bundle.queryResult().noticeCreatedAt(),
                bundle.queryResult().noticeUpdatedAt());
    }

    // ========== Variant URL Enrichment ==========

    /**
     * 웹 상세 이미지에 Variant URL을 적용합니다.
     *
     * <p>래핑 객체에 미리 해석된 variant URL을 사용하여 이미지 URL을 대체합니다.
     */
    private List<ProductGroupImageResult> enrichDetailImages(
            ProductGroupDetailImageResults imageResults) {
        return imageResults.images().stream()
                .map(
                        image ->
                                ProductGroupImageResult.from(
                                        image,
                                        imageResults.resolveImageUrl(
                                                image.imageId(), image.imageUrl())))
                .toList();
    }
}
