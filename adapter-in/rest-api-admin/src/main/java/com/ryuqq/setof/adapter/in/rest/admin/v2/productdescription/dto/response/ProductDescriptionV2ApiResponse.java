package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.response;

import com.ryuqq.setof.application.productdescription.dto.response.DescriptionImageResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * 상품설명 조회 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품설명 조회 응답")
public record ProductDescriptionV2ApiResponse(
        @Schema(description = "상품설명 ID", example = "1") Long productDescriptionId,
        @Schema(description = "상품그룹 ID", example = "100") Long productGroupId,
        @Schema(description = "HTML 컨텐츠") String htmlContent,
        @Schema(description = "이미지 목록") List<DescriptionImageV2ApiResponse> images,
        @Schema(description = "컨텐츠 존재 여부") boolean hasContent,
        @Schema(description = "모든 이미지 CDN 변환 완료 여부") boolean allImagesCdnConverted,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    public static ProductDescriptionV2ApiResponse from(ProductDescriptionResponse response) {
        List<DescriptionImageV2ApiResponse> imageResponses =
                response.images() != null
                        ? response.images().stream()
                                .map(DescriptionImageV2ApiResponse::from)
                                .toList()
                        : List.of();

        return new ProductDescriptionV2ApiResponse(
                response.productDescriptionId(),
                response.productGroupId(),
                response.htmlContent(),
                imageResponses,
                response.hasContent(),
                response.allImagesCdnConverted(),
                response.createdAt(),
                response.updatedAt());
    }

    @Schema(description = "설명 이미지 응답")
    public record DescriptionImageV2ApiResponse(
            @Schema(description = "표시 순서", example = "1") int displayOrder,
            @Schema(description = "원본 URL") String originUrl,
            @Schema(description = "CDN URL") String cdnUrl,
            @Schema(description = "업로드 일시") Instant uploadedAt,
            @Schema(description = "CDN 변환 완료 여부") boolean cdnConverted) {

        public static DescriptionImageV2ApiResponse from(DescriptionImageResponse response) {
            return new DescriptionImageV2ApiResponse(
                    response.displayOrder(),
                    response.originUrl(),
                    response.cdnUrl(),
                    response.uploadedAt(),
                    response.cdnConverted());
        }
    }
}
