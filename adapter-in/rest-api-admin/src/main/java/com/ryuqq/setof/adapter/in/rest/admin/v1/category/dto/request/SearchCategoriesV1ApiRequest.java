package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchCategoriesV1ApiRequest - 카테고리 검색 요청 DTO.
 *
 * <p>레거시 CategoryFilter 기반 변환.
 *
 * <p>GET /api/v1/category/page - 카테고리 페이징 조회
 *
 * <p>No-Offset 페이징 지원: lastCategoryId가 있으면 커서 기반 조회
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "카테고리 검색 요청")
public record SearchCategoriesV1ApiRequest(
        @Parameter(description = "카테고리 ID (특정 카테고리 필터)", example = "100") Long categoryId,
        @Parameter(description = "카테고리명 검색 (displayName LIKE 검색)", example = "의류")
                String categoryName,
        @Parameter(description = "카테고리 depth 필터 (1=루트, 2=중분류, 3=소분류)", example = "2")
                Integer categoryDepth,
        @Parameter(description = "마지막 조회 카테고리 ID (No-Offset 페이징용)", example = "50")
                Long lastCategoryId,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
