package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * SearchCategoriesByIdsV1ApiRequest - 카테고리 ID 목록으로 조회 요청 DTO.
 *
 * <p>레거시 @RequestParam List categoryIds 기반 변환.
 *
 * <p>GET /api/v1/category/parents - 다건 카테고리 조회
 *
 * <p>주의: 레거시 API명은 'parents'지만 실제로는 요청한 카테고리만 반환함.
 *
 * <p>상위 카테고리 재귀 탐색 없음.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "카테고리 ID 목록 조회 요청")
public record SearchCategoriesByIdsV1ApiRequest(
        @Parameter(description = "조회할 카테고리 ID 목록", example = "[100, 200, 300]")
                @NotEmpty(message = "카테고리 ID 목록은 필수입니다.")
                @Size(max = 100, message = "한 번에 최대 100개까지 조회 가능합니다.")
                List<Long> categoryIds) {}
