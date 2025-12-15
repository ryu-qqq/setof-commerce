package com.ryuqq.setof.adapter.in.rest.v1.product.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse.ProductGroupBrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse.ProductGroupPriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse.ProductGroupStatusV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse.ClothesDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse.DeliveryNoticeV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse.PriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupV1ApiResponse.RefundNoticeV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductImageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductNoticeV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductV1ApiResponse;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Product V1 API Mapper
 *
 * <p>Application Response를 V1 API Response로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SuppressWarnings("PMD.ExcessiveImports")
@Component
public class ProductV1ApiMapper {

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * V1 Search Request를 Application Query로 변환
     *
     * @param request V1 검색 요청
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Application 검색 쿼리
     */
    public ProductGroupSearchQuery toQuery(
            ProductGroupV1SearchApiRequest request, int page, int size) {
        return new ProductGroupSearchQuery(
                request != null ? request.sellerId() : null,
                request != null ? request.categoryId() : null,
                request != null ? request.brandId() : null,
                null,
                null,
                page,
                size > 0 ? size : DEFAULT_PAGE_SIZE);
    }

    /**
     * FullProductResponse를 V1 상세 Response로 변환
     *
     * @param response Application 전체 상품 응답
     * @return V1 상세 응답
     */
    public ProductGroupDetailV1ApiResponse toDetailResponse(FullProductResponse response) {
        ProductGroupResponse productGroup = response.productGroup();
        List<ProductResponse> products = response.products();
        List<ProductImageResponse> images = response.images();
        ProductDescriptionResponse description = response.description();
        ProductNoticeResponse notice = response.notice();
        List<ProductStockResponse> stocks = response.stocks();

        Map<Long, Integer> stockMap = buildStockMap(stocks);

        return new ProductGroupDetailV1ApiResponse(
                toProductGroupV1Response(productGroup),
                toNoticeV1Response(notice),
                toImageV1Responses(images),
                toProductV1Responses(products, stockMap),
                Collections.emptySet(),
                description != null ? description.htmlContent() : null,
                0.0,
                0.0,
                false,
                "NORMAL");
    }

    /**
     * ProductGroupSummaryResponse 목록을 V1 썸네일 Response 목록으로 변환
     *
     * @param responses Application 상품그룹 요약 목록
     * @return V1 썸네일 응답 목록
     */
    public List<ProductGroupThumbnailV1ApiResponse> toThumbnailResponses(
            List<ProductGroupSummaryResponse> responses) {
        return responses.stream().map(this::toThumbnailResponse).toList();
    }

    /**
     * ProductGroupSummaryResponse를 V1 썸네일 Response로 변환
     *
     * @param response Application 상품그룹 요약 응답
     * @return V1 썸네일 응답
     */
    public ProductGroupThumbnailV1ApiResponse toThumbnailResponse(
            ProductGroupSummaryResponse response) {
        Long currentPrice = toLong(response.currentPrice());

        return new ProductGroupThumbnailV1ApiResponse(
                response.productGroupId(),
                response.sellerId(),
                response.name(),
                new ProductGroupBrandV1ApiResponse(null, null),
                null,
                new ProductGroupPriceV1ApiResponse(
                        currentPrice, currentPrice, currentPrice, 0L, 0, 0),
                null,
                0.0,
                0,
                0.0,
                false,
                new ProductGroupStatusV1ApiResponse(
                        "ACTIVE".equals(response.status()) ? "N" : "Y", "Y"));
    }

    private ProductGroupV1ApiResponse toProductGroupV1Response(ProductGroupResponse pg) {
        Long regularPrice = toLong(pg.regularPrice());
        Long currentPrice = toLong(pg.currentPrice());
        int discountRate = calculateDiscountRate(regularPrice, currentPrice);

        return new ProductGroupV1ApiResponse(
                pg.productGroupId(),
                pg.name(),
                pg.sellerId(),
                null,
                new BrandV1ApiResponse(pg.brandId(), null),
                pg.categoryId(),
                new PriceV1ApiResponse(
                        regularPrice,
                        currentPrice,
                        currentPrice,
                        discountRate,
                        regularPrice - currentPrice,
                        discountRate),
                pg.optionType(),
                new ClothesDetailV1ApiResponse(null, null),
                new DeliveryNoticeV1ApiResponse(null, null, null),
                new RefundNoticeV1ApiResponse(null, null, null, null),
                null,
                pg.status(),
                null,
                null,
                null,
                null,
                0.0,
                0L);
    }

    private ProductNoticeV1ApiResponse toNoticeV1Response(ProductNoticeResponse notice) {
        if (notice == null || notice.items() == null || notice.items().isEmpty()) {
            return null;
        }
        Map<String, String> itemMap =
                notice.items().stream()
                        .collect(
                                Collectors.toMap(
                                        item -> item.fieldKey(),
                                        item -> item.fieldValue() != null ? item.fieldValue() : "",
                                        (a, b) -> a));

        return new ProductNoticeV1ApiResponse(
                itemMap.getOrDefault("material", ""),
                itemMap.getOrDefault("color", ""),
                itemMap.getOrDefault("size", ""),
                itemMap.getOrDefault("maker", ""),
                itemMap.getOrDefault("origin", ""),
                itemMap.getOrDefault("washingMethod", ""),
                itemMap.getOrDefault("yearMonth", ""),
                itemMap.getOrDefault("assuranceStandard", ""),
                itemMap.getOrDefault("asPhone", ""));
    }

    private Set<ProductImageV1ApiResponse> toImageV1Responses(List<ProductImageResponse> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptySet();
        }
        return images.stream()
                .map(
                        img ->
                                new ProductImageV1ApiResponse(
                                        img.imageType(),
                                        img.cdnUrl() != null ? img.cdnUrl() : img.originUrl()))
                .collect(Collectors.toSet());
    }

    private Set<ProductV1ApiResponse> toProductV1Responses(
            List<ProductResponse> products, Map<Long, Integer> stockMap) {
        if (products == null || products.isEmpty()) {
            return Collections.emptySet();
        }
        return products.stream()
                .map(p -> toProductV1Response(p, stockMap.getOrDefault(p.productId(), 0)))
                .collect(Collectors.toSet());
    }

    private ProductV1ApiResponse toProductV1Response(ProductResponse p, int stockQuantity) {
        String option = buildOptionString(p.option1Value(), p.option2Value());
        String status = p.soldOut() ? "SOLD_OUT" : (p.displayYn() ? "ON_SALE" : "HIDDEN");
        return new ProductV1ApiResponse(
                p.productId(), stockQuantity, status, option, Collections.emptySet());
    }

    private Map<Long, Integer> buildStockMap(List<ProductStockResponse> stocks) {
        if (stocks == null || stocks.isEmpty()) {
            return Collections.emptyMap();
        }
        return stocks.stream()
                .collect(
                        Collectors.toMap(
                                ProductStockResponse::productId,
                                ProductStockResponse::quantity,
                                (a, b) -> a));
    }

    private String buildOptionString(String option1, String option2) {
        if (option1 == null && option2 == null) {
            return null;
        }
        if (option2 == null) {
            return option1;
        }
        return option1 + " " + option2;
    }

    private Long toLong(BigDecimal value) {
        return value != null ? value.longValue() : 0L;
    }

    private int calculateDiscountRate(Long regularPrice, Long currentPrice) {
        if (regularPrice == null || regularPrice == 0 || currentPrice == null) {
            return 0;
        }
        return (int) ((regularPrice - currentPrice) * 100 / regularPrice);
    }
}
