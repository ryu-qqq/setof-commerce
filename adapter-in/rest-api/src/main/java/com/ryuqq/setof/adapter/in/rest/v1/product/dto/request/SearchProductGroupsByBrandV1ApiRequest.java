package com.ryuqq.setof.adapter.in.rest.v1.product.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchProductGroupsByBrandV1ApiRequest - 브랜드별 상품그룹 검색 요청 DTO.
 *
 * <p>레거시 fetchProductGroupWithBrand 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>brandId는 PathVariable로 전달되므로 이 DTO에는 페이징 정보만 포함.
 *
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기 (1~100)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "브랜드별 상품그룹 검색 요청")
public record SearchProductGroupsByBrandV1ApiRequest(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Schema(description = "페이지 번호", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Schema(description = "페이지 크기", defaultValue = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    public SearchProductGroupsByBrandV1ApiRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
    }
}
