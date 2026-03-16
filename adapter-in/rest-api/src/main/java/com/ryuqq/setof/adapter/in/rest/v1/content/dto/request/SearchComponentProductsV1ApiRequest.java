package com.ryuqq.setof.adapter.in.rest.v1.content.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * SearchComponentProductsV1ApiRequest - 컴포넌트 상품 검색 요청 DTO.
 *
 * <p>레거시 ComponentFilter 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "컴포넌트 상품 검색 요청")
public record SearchComponentProductsV1ApiRequest(
        @Parameter(
                        description = "컴포넌트 타입",
                        example = "PRODUCT",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "TEXT",
                                            "TITLE",
                                            "IMAGE",
                                            "BLANK",
                                            "TAB",
                                            "BRAND",
                                            "CATEGORY",
                                            "PRODUCT"
                                        }))
                String componentType,
        @Parameter(description = "탭 ID", example = "1") Long tabId,
        @Parameter(description = "카테고리 ID 목록", example = "[1, 2, 3]") List<Long> categoryIds,
        @Parameter(description = "브랜드 ID", example = "1") Long brandId,
        @Parameter(description = "브랜드 ID 목록", example = "[1, 2, 3]") List<Long> brandIds,
        @Parameter(description = "제외할 상품 ID 목록", example = "[100, 200]")
                List<Long> exclusiveProductIds,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    public SearchComponentProductsV1ApiRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
        if (categoryIds == null) {
            categoryIds = List.of();
        }
        if (brandIds == null) {
            brandIds = List.of();
        }
        if (exclusiveProductIds == null) {
            exclusiveProductIds = List.of();
        }
    }
}
