package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 상품그룹 검색 조건 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 검색 조건")
public record ProductGroupSearchV2ApiRequest(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "카테고리 ID", example = "100") Long categoryId,
        @Schema(description = "브랜드 ID", example = "10") Long brandId,
        @Schema(description = "상품그룹명 (LIKE 검색)", example = "티셔츠") String name,
        @Schema(
                        description = "상태 (ACTIVE, INACTIVE)",
                        example = "ACTIVE",
                        allowableValues = {"ACTIVE", "INACTIVE"})
                String status,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", minimum = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                Integer page,
        @Schema(description = "페이지 크기", example = "20", minimum = "1", maximum = "100")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                @Max(value = 100, message = "페이지 크기는 100 이하이어야 합니다")
                Integer size) {

    /** 기본 페이지 크기 */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** 페이지 번호 반환 (null이면 0) */
    public int pageOrDefault() {
        return page != null ? page : 0;
    }

    /** 페이지 크기 반환 (null이면 기본값) */
    public int sizeOrDefault() {
        return size != null ? size : DEFAULT_PAGE_SIZE;
    }
}
