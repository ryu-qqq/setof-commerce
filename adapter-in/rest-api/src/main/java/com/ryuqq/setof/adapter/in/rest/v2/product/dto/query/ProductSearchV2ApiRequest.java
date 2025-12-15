package com.ryuqq.setof.adapter.in.rest.v2.product.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 상품 검색 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품 검색 요청")
public record ProductSearchV2ApiRequest(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "카테고리 ID", example = "100") Long categoryId,
        @Schema(description = "브랜드 ID", example = "10") Long brandId,
        @Schema(description = "상품명 (LIKE 검색)", example = "반팔") String name,
        @Schema(description = "상태 (ACTIVE, INACTIVE)", example = "ACTIVE") String status,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
                Integer size) {

    /** 기본값 적용 */
    public ProductSearchV2ApiRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }
}
