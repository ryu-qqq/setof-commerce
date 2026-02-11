package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AvailableReviewOrderV1ApiResponse - 작성 가능한 리뷰 주문 응답 DTO.
 *
 * <p>레거시 ReviewOrderProductDto 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "작성 가능한 리뷰 주문 응답")
public record AvailableReviewOrderV1ApiResponse(
        @Schema(description = "결제 ID", example = "1001") long paymentId,
        @Schema(description = "셀러 ID", example = "50") long sellerId,
        @Schema(description = "주문 ID", example = "456") long orderId,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "상품그룹 ID", example = "789") long productGroupId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "상품 ID", example = "1000") long productId,
        @Schema(description = "셀러명", example = "나이키 공식스토어") String sellerName,
        @Schema(description = "상품그룹 메인 이미지 URL", example = "https://...")
                String productGroupMainImageUrl,
        @Schema(description = "주문 수량", example = "2") int productQuantity,
        @Schema(description = "주문 상태", example = "DELIVERED") String orderStatus,
        @Schema(description = "정가", example = "50000") long regularPrice,
        @Schema(description = "판매가", example = "45000") long currentPrice,
        @Schema(description = "주문 금액", example = "90000") long orderAmount,
        @Schema(description = "옵션 목록") List<OptionResponse> options,
        @Schema(description = "결제일시", example = "2024-01-10T14:20:00") LocalDateTime paymentDate) {

    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "NIKE") String brandName) {}

    @Schema(description = "옵션 정보")
    public record OptionResponse(
            @Schema(description = "옵션그룹 ID", example = "1") long optionGroupId,
            @Schema(description = "옵션상세 ID", example = "10") long optionDetailId,
            @Schema(description = "옵션명", example = "색상") String optionName,
            @Schema(description = "옵션값", example = "Black") String optionValue) {}
}
