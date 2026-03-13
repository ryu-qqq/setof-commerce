package com.ryuqq.setof.application.productgroup.assembler;

import com.ryuqq.setof.application.product.dto.response.ProductDetailResult;
import com.ryuqq.setof.application.product.dto.response.ResolvedProductOptionResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
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
     * <p>pageSize + 1개 조회한 결과에서 hasNext를 판별하고, 커서를 결정합니다. cursor는 레거시 방식과 동일하게
     * "lastDomainId,cursorValue" 형식으로 인코딩합니다.
     *
     * @param bundle 썸네일 번들 (thumbnails + totalElements + orderType)
     * @param requestedSize 요청 페이지 크기
     * @return 슬라이스 결과
     */
    public ProductGroupSliceResult toSliceResult(ProductGroupListBundle bundle, int requestedSize) {
        if (bundle.thumbnails().isEmpty()) {
            return ProductGroupSliceResult.empty(requestedSize);
        }

        boolean hasNext = bundle.thumbnails().size() > requestedSize;
        List<ProductGroupThumbnailCompositeResult> rawContent =
                hasNext ? bundle.thumbnails().subList(0, requestedSize) : bundle.thumbnails();

        List<ProductGroupThumbnailResult> content =
                rawContent.stream().map(ProductGroupThumbnailResult::from).toList();

        String nextCursor = resolveNextCursor(rawContent, bundle.orderType());
        SliceMeta sliceMeta =
                SliceMeta.withCursor(nextCursor, requestedSize, hasNext, content.size());

        return ProductGroupSliceResult.of(content, sliceMeta, bundle.totalElements());
    }

    /**
     * 다음 커서 값 결정.
     *
     * <p>레거시 커서 전략: - 마지막 아이템의 productGroupId를 lastDomainId로 사용 - orderType에 따라 cursorValue를 결정 -
     * "lastDomainId,cursorValue" 형식으로 인코딩
     */
    private String resolveNextCursor(
            List<ProductGroupThumbnailCompositeResult> content, String orderType) {
        if (content.isEmpty()) {
            return null;
        }

        ProductGroupThumbnailCompositeResult last = content.get(content.size() - 1);
        long lastDomainId = last.productGroupId();

        String cursorValue = resolveCursorValue(last, orderType);

        if (cursorValue != null) {
            return lastDomainId + "," + cursorValue;
        }
        return String.valueOf(lastDomainId);
    }

    /** orderType에 따른 cursorValue 결정. */
    private String resolveCursorValue(ProductGroupThumbnailCompositeResult item, String orderType) {
        if (orderType == null) {
            return null;
        }
        return switch (orderType.toUpperCase()) {
            case "RECOMMEND" -> String.valueOf(item.score());
            case "REVIEW" -> String.valueOf(item.reviewCount());
            case "HIGH_RATING" -> String.valueOf(item.averageRating());
            case "LOW_PRICE", "HIGH_PRICE" -> String.valueOf(item.salePrice());
            case "LOW_DISCOUNT", "HIGH_DISCOUNT" -> String.valueOf(item.discountRate());
            case "RECENT" -> item.insertDate() != null ? item.insertDate().toString() : null;
            default -> null;
        };
    }

    /** 상세 번들 → DetailCompositeResult 조립. */
    public ProductGroupDetailCompositeResult toDetailResult(ProductGroupDetailBundle bundle) {
        ProductGroupDetailCompositeQueryResult queryResult = bundle.queryResult();
        ProductGroup group = bundle.group();

        List<ProductGroupImageResult> images =
                group.images().stream().map(ProductGroupImageResult::from).toList();

        ProductOptionMatrixResult optionProductMatrix =
                buildOptionProductMatrix(group, bundle.products());

        ProductGroupDescriptionResult descriptionResult =
                bundle.description().map(ProductGroupDescriptionResult::from).orElse(null);
        ProductNoticeResult noticeResult =
                bundle.notice().map(ProductNoticeResult::from).orElse(null);

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

    /** 상세 번들 → 웹(사용자) DetailCompositeResult 조립. */
    public WebProductGroupDetailCompositeResult toWebDetailResult(ProductGroupDetailBundle bundle) {
        ProductGroupDetailCompositeQueryResult queryResult = bundle.queryResult();
        ProductGroup group = bundle.group();

        List<ProductGroupImageResult> images =
                group.images().stream().map(ProductGroupImageResult::from).toList();

        ProductOptionMatrixResult optionProductMatrix =
                buildOptionProductMatrix(group, bundle.products());

        ProductGroupDescriptionResult descriptionResult =
                bundle.description().map(ProductGroupDescriptionResult::from).orElse(null);
        ProductNoticeResult noticeResult =
                bundle.notice().map(ProductNoticeResult::from).orElse(null);

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
}
