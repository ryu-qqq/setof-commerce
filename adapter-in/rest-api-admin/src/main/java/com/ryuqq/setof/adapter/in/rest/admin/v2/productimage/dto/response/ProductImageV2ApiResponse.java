package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response;

import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 상품이미지 조회 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품이미지 조회 응답")
public record ProductImageV2ApiResponse(
        @Schema(description = "이미지 ID", example = "1") Long id,
        @Schema(description = "상품그룹 ID", example = "100") Long productGroupId,
        @Schema(description = "이미지 타입 (MAIN, SUB, DETAIL)", example = "MAIN") String imageType,
        @Schema(description = "원본 URL") String originUrl,
        @Schema(description = "CDN URL") String cdnUrl,
        @Schema(description = "표시 순서", example = "1") int displayOrder,
        @Schema(description = "생성일시") Instant createdAt) {

    public static ProductImageV2ApiResponse from(ProductImageResponse response) {
        return new ProductImageV2ApiResponse(
                response.id(),
                response.productGroupId(),
                response.imageType(),
                response.originUrl(),
                response.cdnUrl(),
                response.displayOrder(),
                response.createdAt());
    }
}
