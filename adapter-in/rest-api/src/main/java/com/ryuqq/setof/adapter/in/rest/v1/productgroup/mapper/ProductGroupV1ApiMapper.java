package com.ryuqq.setof.adapter.in.rest.v1.productgroup.mapper;

import static com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils.format;

import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.request.SearchProductGroupsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.application.common.dto.query.CommonCursorParams;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult.OptionInfoResult;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult.ProductInfoResult;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupDetailResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupThumbnailResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupV1ApiMapper - 상품그룹 V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 ProductController 5개 엔드포인트 변환 지원.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupV1ApiMapper {

    // ========================================================================
    // Request → SearchParams 변환
    // ========================================================================

    /**
     * SearchProductGroupsCursorV1ApiRequest → ProductGroupSearchParams 변환.
     *
     * <p>lastDomainId + cursorValue → "lastDomainId,cursorValue" 복합 커서 조립.
     *
     * <p>기본값 처리: size null이면 20 적용.
     *
     * @param request 검색 요청 DTO
     * @return ProductGroupSearchParams
     */
    public ProductGroupSearchParams toSearchParams(SearchProductGroupsCursorV1ApiRequest request) {
        int pageSize = request.size() != null ? request.size() : 20;
        String cursor = buildCursor(request.lastDomainId(), request.cursorValue());

        return new ProductGroupSearchParams(
                null,
                null,
                request.brandId(),
                request.sellerId(),
                request.categoryId(),
                request.categoryIds(),
                request.brandIds(),
                request.lowestPrice(),
                request.highestPrice(),
                null,
                request.orderType(),
                CommonCursorParams.of(cursor, pageSize));
    }

    /**
     * lastDomainId + cursorValue → "lastDomainId,cursorValue" 복합 커서 조립.
     *
     * <p>lastDomainId가 null이면 null 반환 (첫 페이지).
     */
    private String buildCursor(Long lastDomainId, String cursorValue) {
        if (lastDomainId == null) {
            return null;
        }
        if (cursorValue == null || cursorValue.isBlank()) {
            return lastDomainId.toString();
        }
        return lastDomainId + "," + cursorValue;
    }

    // ========================================================================
    // Result → Response 변환 (목록)
    // ========================================================================

    /**
     * ProductGroupSliceResult → ProductGroupSliceV1ApiResponse 변환.
     *
     * <p>SliceMeta.cursor "lastDomainId,cursorValue" 포맷을 파싱하여 분리합니다.
     *
     * @param result 상품그룹 목록 + 페이징 메타
     * @return ProductGroupSliceV1ApiResponse
     */
    public ProductGroupSliceV1ApiResponse toSliceResponse(ProductGroupSliceResult result) {
        List<ProductGroupThumbnailV1ApiResponse> content =
                result.content().stream().map(this::toThumbnailResponse).toList();

        SliceMeta sliceMeta = result.sliceMeta();
        boolean isEmpty = content.isEmpty();
        int numberOfElements = content.size();
        boolean isFirst = !sliceMeta.hasNext() && !sliceMeta.hasCursor();

        Long lastDomainId = null;
        String cursorValue = null;
        if (sliceMeta.hasCursor()) {
            String[] parts = sliceMeta.cursor().split(",", 2);
            if (parts.length >= 1 && !parts[0].isBlank()) {
                try {
                    lastDomainId = Long.parseLong(parts[0].trim());
                } catch (NumberFormatException ignored) {
                }
            }
            if (parts.length >= 2) {
                cursorValue = parts[1];
            }
        }

        return new ProductGroupSliceV1ApiResponse(
                content,
                !sliceMeta.hasNext(),
                isFirst,
                sliceMeta.size(),
                numberOfElements,
                isEmpty,
                lastDomainId,
                cursorValue,
                result.totalElements());
    }

    /**
     * ProductGroupThumbnailResult 목록 → ProductGroupThumbnailV1ApiResponse 목록 변환.
     *
     * <p>fetchProductGroupLikes / fetchProductGroupWithBrand / fetchProductGroupWithSeller 응답용.
     *
     * @param results 썸네일 결과 목록
     * @return ProductGroupThumbnailV1ApiResponse 목록
     */
    public List<ProductGroupThumbnailV1ApiResponse> toThumbnailListResponse(
            List<ProductGroupThumbnailResult> results) {
        return results.stream().map(this::toThumbnailResponse).toList();
    }

    /**
     * ProductGroupThumbnailResult → ProductGroupThumbnailV1ApiResponse 변환.
     *
     * @param result 썸네일 결과
     * @return ProductGroupThumbnailV1ApiResponse
     */
    public ProductGroupThumbnailV1ApiResponse toThumbnailResponse(
            ProductGroupThumbnailResult result) {

        ProductGroupThumbnailV1ApiResponse.BrandResponse brand =
                new ProductGroupThumbnailV1ApiResponse.BrandResponse(
                        result.brandId(), result.brandName());

        ProductGroupThumbnailV1ApiResponse.PriceResponse price =
                new ProductGroupThumbnailV1ApiResponse.PriceResponse(
                        result.regularPrice(),
                        result.currentPrice(),
                        result.salePrice(),
                        result.directDiscountRate(),
                        result.directDiscountPrice(),
                        result.discountRate());

        ProductGroupThumbnailV1ApiResponse.ProductStatusResponse productStatus =
                new ProductGroupThumbnailV1ApiResponse.ProductStatusResponse(
                        result.soldOutYn(), result.displayYn());

        return new ProductGroupThumbnailV1ApiResponse(
                result.productGroupId(),
                result.sellerId(),
                result.productGroupName(),
                brand,
                result.productImageUrl(),
                price,
                format(result.createdAt()),
                0.0,
                0L,
                0.0,
                false,
                productStatus);
    }

    // ========================================================================
    // Result → Response 변환 (단건 상세)
    // ========================================================================

    /**
     * ProductGroupDetailResult → ProductGroupDetailV1ApiResponse 변환.
     *
     * <p>fetchProductGroup 단건 상세 조회 응답용.
     *
     * <p>categories는 path 기반 계층 조회를 하지 않으므로 빈 목록으로 반환합니다.
     *
     * @param result 상품그룹 상세 결과
     * @return ProductGroupDetailV1ApiResponse
     */
    public ProductGroupDetailV1ApiResponse toDetailResponse(ProductGroupDetailResult result) {

        ProductGroupDetailV1ApiResponse.ProductGroupResponse productGroup =
                toProductGroupResponse(result);

        ProductGroupDetailV1ApiResponse.ProductNoticeResponse productNotices =
                new ProductGroupDetailV1ApiResponse.ProductNoticeResponse(
                        null, null, null, null, null, null, null, null, null);

        List<ProductGroupDetailV1ApiResponse.ProductImageResponse> productGroupImages =
                toImageResponses(result);

        List<ProductGroupDetailV1ApiResponse.ProductResponse> products = toProductResponses(result);

        return new ProductGroupDetailV1ApiResponse(
                productGroup,
                productNotices,
                productGroupImages,
                products,
                List.of(),
                result.detailDescription(),
                0.0,
                0.0,
                false,
                "NORMAL");
    }

    private ProductGroupDetailV1ApiResponse.ProductGroupResponse toProductGroupResponse(
            ProductGroupDetailResult result) {

        ProductGroupDetailV1ApiResponse.BrandResponse brandResponse =
                new ProductGroupDetailV1ApiResponse.BrandResponse(
                        result.brandId(), result.brandName());

        ProductGroupDetailV1ApiResponse.PriceResponse price =
                new ProductGroupDetailV1ApiResponse.PriceResponse(
                        result.regularPrice(),
                        result.currentPrice(),
                        result.salePrice(),
                        result.directDiscountRate(),
                        result.directDiscountPrice(),
                        result.discountRate());

        ProductGroupDetailV1ApiResponse.ProductStatusResponse productStatus =
                new ProductGroupDetailV1ApiResponse.ProductStatusResponse(
                        result.soldOutYn(), result.displayYn());

        ProductGroupDetailV1ApiResponse.ClothesDetailResponse clothesDetail =
                new ProductGroupDetailV1ApiResponse.ClothesDetailResponse(null, null);

        ProductGroupDetailV1ApiResponse.DeliveryNoticeResponse deliveryNotice =
                new ProductGroupDetailV1ApiResponse.DeliveryNoticeResponse(null, null, 0L, 0L);

        ProductGroupDetailV1ApiResponse.RefundNoticeResponse refundNotice =
                new ProductGroupDetailV1ApiResponse.RefundNoticeResponse(null, null, 0, null);

        return new ProductGroupDetailV1ApiResponse.ProductGroupResponse(
                result.productGroupId(),
                result.productGroupName(),
                result.sellerId(),
                result.sellerName(),
                brandResponse,
                result.categoryId(),
                price,
                result.optionType(),
                clothesDetail,
                deliveryNotice,
                refundNotice,
                null,
                productStatus,
                format(result.insertDate()),
                format(result.insertDate()),
                result.averageRating(),
                result.reviewCount());
    }

    private List<ProductGroupDetailV1ApiResponse.ProductImageResponse> toImageResponses(
            ProductGroupDetailResult result) {
        if (result.images() == null || result.images().isEmpty()) {
            return List.of();
        }

        return result.images().stream()
                .sorted(Comparator.comparing(img -> "MAIN".equals(img.imageType()) ? 0 : 1))
                .map(
                        img ->
                                new ProductGroupDetailV1ApiResponse.ProductImageResponse(
                                        img.imageType(), img.imageUrl()))
                .toList();
    }

    private List<ProductGroupDetailV1ApiResponse.ProductResponse> toProductResponses(
            ProductGroupDetailResult result) {
        if (result.products() == null || result.products().isEmpty()) {
            return List.of();
        }

        return result.products().stream()
                .map(
                        product -> {
                            ProductGroupDetailV1ApiResponse.ProductStatusResponse status =
                                    new ProductGroupDetailV1ApiResponse.ProductStatusResponse(
                                            product.soldOutYn(), "Y");

                            List<ProductGroupDetailV1ApiResponse.OptionResponse> options =
                                    toOptionResponses(product);

                            String optionLabel =
                                    options.stream()
                                            .map(o -> o.optionName() + " / " + o.optionValue())
                                            .reduce((a, b) -> a + " / " + b)
                                            .orElse("");

                            return new ProductGroupDetailV1ApiResponse.ProductResponse(
                                    product.productId(),
                                    product.stockQuantity(),
                                    status,
                                    optionLabel,
                                    options);
                        })
                .toList();
    }

    private List<ProductGroupDetailV1ApiResponse.OptionResponse> toOptionResponses(
            ProductInfoResult product) {
        if (product.options() == null || product.options().isEmpty()) {
            return List.of();
        }

        return product.options().stream()
                .map(
                        (OptionInfoResult option) ->
                                new ProductGroupDetailV1ApiResponse.OptionResponse(
                                        option.optionGroupId(),
                                        option.optionDetailId(),
                                        option.optionGroupName(),
                                        option.optionValue()))
                .toList();
    }
}
