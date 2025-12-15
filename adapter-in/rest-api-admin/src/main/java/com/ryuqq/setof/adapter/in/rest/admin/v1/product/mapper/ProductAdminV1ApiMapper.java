package com.ryuqq.setof.adapter.in.rest.admin.v1.product.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateOptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductImageV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductStockV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.query.ProductGroupFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ClothesDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.DeliveryNoticeV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.PriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductImageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductNoticeV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.RefundNoticeV1ApiResponse;
import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Product Mapper
 *
 * <p>Application Response를 Admin V1 API Response로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.GodClass"})
@Component
public class ProductAdminV1ApiMapper {

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Filter Request를 Application Query로 변환
     *
     * @param filter Admin V1 필터 요청
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Application 검색 쿼리
     */
    public ProductGroupSearchQuery toQuery(
            ProductGroupFilterV1ApiRequest filter, int page, int size) {
        return new ProductGroupSearchQuery(
                filter != null ? filter.sellerId() : null,
                filter != null ? filter.categoryId() : null,
                filter != null ? filter.brandId() : null,
                filter != null ? filter.searchKeyword() : null,
                null,
                page,
                size > 0 ? size : DEFAULT_PAGE_SIZE);
    }

    /**
     * FullProductResponse를 단건 조회 Response로 변환
     *
     * @param response Application 전체 상품 응답
     * @return Admin V1 상품 그룹 조회 응답
     */
    public ProductGroupFetchV1ApiResponse toFetchResponse(FullProductResponse response) {
        ProductGroupResponse productGroup = response.productGroup();
        List<ProductResponse> products = response.products();
        List<ProductImageResponse> images = response.images();
        ProductDescriptionResponse description = response.description();
        ProductNoticeResponse notice = response.notice();
        List<ProductStockResponse> stocks = response.stocks();

        Map<Long, Integer> stockMap = buildStockMap(stocks);

        return new ProductGroupFetchV1ApiResponse(
                toProductGroupInfoResponse(productGroup),
                toNoticeResponse(notice),
                toImageResponses(images),
                description != null ? description.htmlContent() : null,
                Collections.emptyList(),
                toProductFetchResponses(products, stockMap));
    }

    /**
     * ProductGroupSummaryResponse 목록을 목록 조회 Response로 변환
     *
     * @param responses Application 상품그룹 요약 목록
     * @return Admin V1 상품 그룹 상세 응답 목록
     */
    public List<ProductGroupDetailV1ApiResponse> toDetailResponses(
            List<ProductGroupSummaryResponse> responses) {
        return responses.stream().map(this::toDetailResponse).toList();
    }

    private ProductGroupDetailV1ApiResponse toDetailResponse(ProductGroupSummaryResponse response) {
        ProductGroupInfoV1ApiResponse productGroupInfo = toProductGroupInfoFromSummary(response);
        return new ProductGroupDetailV1ApiResponse(productGroupInfo, Collections.emptySet());
    }

    private ProductGroupInfoV1ApiResponse toProductGroupInfoResponse(ProductGroupResponse pg) {
        Long regularPrice = toLong(pg.regularPrice());
        Long currentPrice = toLong(pg.currentPrice());
        int discountRate = calculateDiscountRate(regularPrice, currentPrice);

        return new ProductGroupInfoV1ApiResponse(
                pg.productGroupId(),
                pg.name(),
                pg.sellerId(),
                null,
                pg.categoryId(),
                pg.optionType(),
                null,
                new BrandV1ApiResponse(pg.brandId(), null),
                new PriceV1ApiResponse(
                        regularPrice,
                        currentPrice,
                        currentPrice,
                        discountRate,
                        regularPrice - currentPrice,
                        discountRate),
                new ClothesDetailV1ApiResponse(null, null, null),
                new DeliveryNoticeV1ApiResponse(null, null, null),
                new RefundNoticeV1ApiResponse(null, null, null, null),
                null,
                null,
                pg.status(),
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private ProductGroupInfoV1ApiResponse toProductGroupInfoFromSummary(
            ProductGroupSummaryResponse summary) {
        Long currentPrice = toLong(summary.currentPrice());

        return new ProductGroupInfoV1ApiResponse(
                summary.productGroupId(),
                summary.name(),
                summary.sellerId(),
                null,
                null,
                null,
                null,
                new BrandV1ApiResponse(null, null),
                new PriceV1ApiResponse(currentPrice, currentPrice, currentPrice, 0, 0L, 0),
                new ClothesDetailV1ApiResponse(null, null, null),
                new DeliveryNoticeV1ApiResponse(null, null, null),
                new RefundNoticeV1ApiResponse(null, null, null, null),
                null,
                null,
                summary.status(),
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private ProductNoticeV1ApiResponse toNoticeResponse(ProductNoticeResponse notice) {
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

    private List<ProductImageV1ApiResponse> toImageResponses(List<ProductImageResponse> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(
                        img ->
                                new ProductImageV1ApiResponse(
                                        img.imageType(),
                                        img.cdnUrl() != null ? img.cdnUrl() : img.originUrl()))
                .toList();
    }

    private Set<ProductFetchV1ApiResponse> toProductFetchResponses(
            List<ProductResponse> products, Map<Long, Integer> stockMap) {
        if (products == null || products.isEmpty()) {
            return Collections.emptySet();
        }
        return products.stream()
                .map(p -> toProductFetchResponse(p, stockMap.getOrDefault(p.productId(), 0)))
                .collect(Collectors.toSet());
    }

    private ProductFetchV1ApiResponse toProductFetchResponse(ProductResponse p, int stockQuantity) {
        String option = buildOptionString(p.option1Value(), p.option2Value());
        String status = p.soldOut() ? "SOLD_OUT" : (p.displayYn() ? "ON_SALE" : "HIDDEN");
        return new ProductFetchV1ApiResponse(
                p.productId(),
                stockQuantity,
                status,
                option,
                Collections.emptySet(),
                p.additionalPrice());
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

    /**
     * V1 상품 등록 요청을 Application Command로 변환
     *
     * @param request V1 상품 등록 요청
     * @return Application 등록 Command
     */
    public RegisterFullProductCommand toRegisterCommand(CreateProductGroupV1ApiRequest request) {
        return new RegisterFullProductCommand(
                request.sellerId(),
                request.categoryId(),
                request.brandId(),
                request.productGroupName(),
                request.optionType(),
                request.price().regularPrice(),
                request.price().currentPrice(),
                null,
                null,
                toProductSkuCommands(request.productOptions()),
                toProductImageCommands(request.productImageList()),
                toDescriptionCommand(request.detailDescription()),
                toNoticeCommand(request.productNotice()));
    }

    /**
     * 상품그룹 삭제 Command 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param sellerId 셀러 ID
     * @return 삭제 Command
     */
    public DeleteProductGroupCommand toDeleteCommand(Long productGroupId, Long sellerId) {
        return new DeleteProductGroupCommand(productGroupId, sellerId);
    }

    private List<ProductSkuCommandDto> toProductSkuCommands(
            List<CreateOptionV1ApiRequest> options) {
        if (options == null || options.isEmpty()) {
            return Collections.emptyList();
        }
        return options.stream().map(this::toProductSkuCommand).toList();
    }

    private ProductSkuCommandDto toProductSkuCommand(CreateOptionV1ApiRequest option) {
        String option1Name = null;
        String option1Value = null;
        String option2Name = null;
        String option2Value = null;

        if (option.options() != null && !option.options().isEmpty()) {
            var first = option.options().get(0);
            option1Name = first.optionName();
            option1Value = first.optionValue();

            if (option.options().size() > 1) {
                var second = option.options().get(1);
                option2Name = second.optionName();
                option2Value = second.optionValue();
            }
        }

        return new ProductSkuCommandDto(
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                option.additionalPrice() != null ? option.additionalPrice() : BigDecimal.ZERO,
                option.quantity() != null ? option.quantity() : 0);
    }

    private List<ProductImageCommandDto> toProductImageCommands(
            List<CreateProductImageV1ApiRequest> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        AtomicInteger order = new AtomicInteger(0);
        return images.stream()
                .map(
                        img ->
                                new ProductImageCommandDto(
                                        null,
                                        img.productImageType(),
                                        img.originUrl() != null ? img.originUrl() : img.imageUrl(),
                                        img.imageUrl(),
                                        order.getAndIncrement()))
                .toList();
    }

    private ProductDescriptionCommandDto toDescriptionCommand(String detailDescription) {
        if (detailDescription == null || detailDescription.isBlank()) {
            return null;
        }
        return new ProductDescriptionCommandDto(null, detailDescription, Collections.emptyList());
    }

    private ProductNoticeCommandDto toNoticeCommand(CreateProductNoticeV1ApiRequest notice) {
        if (notice == null) {
            return null;
        }
        List<NoticeItemDto> items = new ArrayList<>();
        int order = 0;
        if (notice.material() != null) {
            items.add(new NoticeItemDto("material", notice.material(), order++));
        }
        if (notice.color() != null) {
            items.add(new NoticeItemDto("color", notice.color(), order++));
        }
        if (notice.size() != null) {
            items.add(new NoticeItemDto("size", notice.size(), order++));
        }
        if (notice.maker() != null) {
            items.add(new NoticeItemDto("maker", notice.maker(), order++));
        }
        if (notice.origin() != null) {
            items.add(new NoticeItemDto("origin", notice.origin(), order++));
        }
        if (notice.washingMethod() != null) {
            items.add(new NoticeItemDto("washingMethod", notice.washingMethod(), order++));
        }
        if (notice.yearMonth() != null) {
            items.add(new NoticeItemDto("yearMonth", notice.yearMonth(), order++));
        }
        if (notice.assuranceStandard() != null) {
            items.add(new NoticeItemDto("assuranceStandard", notice.assuranceStandard(), order++));
        }
        if (notice.asPhone() != null) {
            items.add(new NoticeItemDto("asPhone", notice.asPhone(), order++));
        }
        return items.isEmpty() ? null : new ProductNoticeCommandDto(null, null, items);
    }

    /**
     * V1 재고 수정 요청을 Application Command로 변환
     *
     * @param request V1 재고 수정 요청
     * @return SetStockCommand
     */
    public SetStockCommand toSetStockCommand(UpdateProductStockV1ApiRequest request) {
        return new SetStockCommand(
                request.productId(),
                request.productStockQuantity() != null ? request.productStockQuantity() : 0);
    }

    /**
     * V1 상품그룹 수정 요청을 Application Command로 변환
     *
     * @param productGroupId 상품그룹 ID
     * @param sellerId 셀러 ID
     * @param request V1 수정 요청
     * @return UpdateProductGroupCommand
     */
    public UpdateProductGroupCommand toUpdateProductGroupCommand(
            Long productGroupId, Long sellerId, UpdateProductGroupV1ApiRequest request) {
        var details = request.productGroupDetails();
        if (details == null) {
            return new UpdateProductGroupCommand(
                    productGroupId, sellerId, null, null, null, null, null, null, null);
        }
        return new UpdateProductGroupCommand(
                productGroupId,
                sellerId,
                details.categoryId(),
                details.brandId(),
                details.productGroupName(),
                null,
                null,
                null,
                null);
    }
}
