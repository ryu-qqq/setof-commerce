package com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 찜 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "찜 응답")
public record FavoriteV1ApiResponse(
        @Schema(description = "찜 ID", example = "1") Long userFavoriteId,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "상품 이미지 URL",
                example = "https://example.com/image.jpg") String productImageUrl,
        @Schema(description = "정가", example = "50000") Long regularPrice,
        @Schema(description = "현재가", example = "45000") Long currentPrice,
        @Schema(description = "판매가", example = "39000") Long salePrice,
        @Schema(description = "직접 할인율", example = "10") Integer directDiscountRate,
        @Schema(description = "직접 할인가", example = "5000") Long directDiscountPrice,
        @Schema(description = "할인율", example = "22") Integer discountRate,
        @Schema(description = "등록일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate,
        @Schema(description = "평균 평점", example = "4.5") Double averageRating,
        @Schema(description = "리뷰 수", example = "150") Long reviewCount,
        @Schema(description = "점수", example = "8.5") Double score,
        @Schema(description = "찜 여부", example = "true") Boolean isFavorite,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus) {
}
