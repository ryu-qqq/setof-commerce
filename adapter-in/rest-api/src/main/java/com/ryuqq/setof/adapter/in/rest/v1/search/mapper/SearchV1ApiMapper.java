package com.ryuqq.setof.adapter.in.rest.v1.search.mapper;

import static com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils.format;

import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.search.dto.request.SearchProductsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.search.dto.response.SearchSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.search.dto.response.SearchSliceV1ApiResponse.SortResponse;
import com.ryuqq.setof.application.common.dto.query.CommonCursorParams;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupThumbnailResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SearchV1ApiMapper - 상품 검색 V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 SearchController.fetchSearchResults 변환 지원. orderType 미지정 시 RECOMMEND로 자동 설정은
 * SearchProductGroupsService에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class SearchV1ApiMapper {

    // ========================================================================
    // Request → SearchParams 변환
    // ========================================================================

    /**
     * SearchProductsCursorV1ApiRequest → ProductGroupSearchParams 변환.
     *
     * <p>lastDomainId + cursorValue → "lastDomainId,cursorValue" 복합 커서 조립.
     *
     * <p>기본값 처리: size null이면 20 적용, orderType null이면 SearchProductGroupsService에서 RECOMMEND로 처리.
     *
     * @param request 검색 요청 DTO
     * @return ProductGroupSearchParams
     */
    public ProductGroupSearchParams toSearchParams(SearchProductsCursorV1ApiRequest request) {
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
                request.searchWord(),
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
    // Result → Response 변환
    // ========================================================================

    /**
     * ProductGroupSliceResult → SearchSliceV1ApiResponse 변환.
     *
     * <p>SliceMeta.cursor "lastDomainId,cursorValue" 포맷을 파싱하여 분리합니다.
     *
     * @param result 검색 결과 + 페이징 메타
     * @return SearchSliceV1ApiResponse
     */
    public SearchSliceV1ApiResponse toSliceResponse(
            ProductGroupSliceResult result, boolean isFirstPage) {
        List<ProductGroupThumbnailV1ApiResponse> content =
                result.content().stream().map(this::toThumbnailResponse).toList();

        SliceMeta sliceMeta = result.sliceMeta();
        boolean isEmpty = content.isEmpty();
        int numberOfElements = content.size();

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

        return new SearchSliceV1ApiResponse(
                content,
                !sliceMeta.hasNext(),
                isFirstPage,
                0,
                SortResponse.defaultSort(),
                sliceMeta.size(),
                numberOfElements,
                isEmpty,
                lastDomainId,
                cursorValue,
                result.totalElements());
    }

    /**
     * ProductGroupThumbnailResult → ProductGroupThumbnailV1ApiResponse 변환.
     *
     * @param result 썸네일 결과
     * @return ProductGroupThumbnailV1ApiResponse
     */
    private ProductGroupThumbnailV1ApiResponse toThumbnailResponse(
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
                format(result.insertDate()),
                result.averageRating(),
                result.reviewCount(),
                result.score(),
                false,
                productStatus);
    }
}
