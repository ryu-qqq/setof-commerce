package com.ryuqq.setof.adapter.in.rest.v2.product.dto.response;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.DescriptionImageResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.NoticeItemResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * 전체 상품 V2 응답 (상품그룹 + 모든 관련 Aggregate)
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "전체 상품 응답 (상품그룹 + 모든 관련 Aggregate)")
public record FullProductV2ApiResponse(
        @Schema(description = "상품그룹 정보") ProductGroupV2ApiResponse productGroup,
        @Schema(description = "상품(SKU) 목록") List<ProductV2ApiResponse> products,
        @Schema(description = "이미지 목록") List<ImageV2Response> images,
        @Schema(description = "상세설명") DescriptionV2Response description,
        @Schema(description = "고시정보") NoticeV2Response notice,
        @Schema(description = "재고 정보 목록") List<StockV2Response> stocks) {

    /**
     * Application Response로부터 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static FullProductV2ApiResponse from(FullProductResponse response) {
        List<ProductV2ApiResponse> products =
                response.products() != null
                        ? response.products().stream().map(ProductV2ApiResponse::from).toList()
                        : List.of();

        List<ImageV2Response> images =
                response.images() != null
                        ? response.images().stream().map(ImageV2Response::from).toList()
                        : List.of();

        DescriptionV2Response description =
                response.description() != null
                        ? DescriptionV2Response.from(response.description())
                        : null;

        NoticeV2Response notice =
                response.notice() != null ? NoticeV2Response.from(response.notice()) : null;

        List<StockV2Response> stocks =
                response.stocks() != null
                        ? response.stocks().stream().map(StockV2Response::from).toList()
                        : List.of();

        return new FullProductV2ApiResponse(
                ProductGroupV2ApiResponse.from(response.productGroup()),
                products,
                images,
                description,
                notice,
                stocks);
    }

    /** 이미지 응답 */
    @Schema(description = "이미지 응답")
    public record ImageV2Response(
            @Schema(description = "이미지 ID", example = "1") Long imageId,
            @Schema(description = "이미지 타입", example = "MAIN") String imageType,
            @Schema(description = "CDN URL", example = "https://cdn.example.com/image.jpg")
                    String cdnUrl,
            @Schema(description = "표시 순서", example = "1") int displayOrder) {

        public static ImageV2Response from(ProductImageResponse response) {
            return new ImageV2Response(
                    response.id(),
                    response.imageType(),
                    response.cdnUrl(),
                    response.displayOrder());
        }
    }

    /** 상세설명 응답 */
    @Schema(description = "상세설명 응답")
    public record DescriptionV2Response(
            @Schema(description = "상품설명 ID", example = "1") Long productDescriptionId,
            @Schema(description = "HTML 컨텐츠") String htmlContent,
            @Schema(description = "이미지 목록") List<DescriptionImageV2Response> images) {

        public static DescriptionV2Response from(ProductDescriptionResponse response) {
            List<DescriptionImageV2Response> images =
                    response.images() != null
                            ? response.images().stream()
                                    .map(DescriptionImageV2Response::from)
                                    .toList()
                            : List.of();

            return new DescriptionV2Response(
                    response.productDescriptionId(), response.htmlContent(), images);
        }
    }

    /** 상세설명 이미지 응답 */
    @Schema(description = "상세설명 이미지 응답")
    public record DescriptionImageV2Response(
            @Schema(description = "표시 순서", example = "1") int displayOrder,
            @Schema(description = "CDN URL", example = "https://cdn.example.com/desc.jpg")
                    String cdnUrl) {

        public static DescriptionImageV2Response from(DescriptionImageResponse response) {
            return new DescriptionImageV2Response(response.displayOrder(), response.cdnUrl());
        }
    }

    /** 고시정보 응답 */
    @Schema(description = "고시정보 응답")
    public record NoticeV2Response(
            @Schema(description = "상품고시 ID", example = "1") Long productNoticeId,
            @Schema(description = "고시 항목 목록") List<NoticeItemV2Response> items) {

        public static NoticeV2Response from(ProductNoticeResponse response) {
            List<NoticeItemV2Response> items =
                    response.items() != null
                            ? response.items().stream().map(NoticeItemV2Response::from).toList()
                            : List.of();

            return new NoticeV2Response(response.productNoticeId(), items);
        }
    }

    /** 고시 항목 응답 */
    @Schema(description = "고시 항목 응답")
    public record NoticeItemV2Response(
            @Schema(description = "필드 키", example = "material") String fieldKey,
            @Schema(description = "필드 값", example = "면 100%") String fieldValue,
            @Schema(description = "표시 순서", example = "1") int displayOrder) {

        public static NoticeItemV2Response from(NoticeItemResponse response) {
            return new NoticeItemV2Response(
                    response.fieldKey(), response.fieldValue(), response.displayOrder());
        }
    }

    /** 재고 응답 */
    @Schema(description = "재고 응답")
    public record StockV2Response(
            @Schema(description = "재고 ID", example = "1") Long productStockId,
            @Schema(description = "상품 ID", example = "1") Long productId,
            @Schema(description = "재고 수량", example = "100") int quantity,
            @Schema(description = "수정일시") Instant updatedAt) {

        public static StockV2Response from(ProductStockResponse response) {
            return new StockV2Response(
                    response.productStockId(),
                    response.productId(),
                    response.quantity(),
                    response.updatedAt());
        }
    }
}
