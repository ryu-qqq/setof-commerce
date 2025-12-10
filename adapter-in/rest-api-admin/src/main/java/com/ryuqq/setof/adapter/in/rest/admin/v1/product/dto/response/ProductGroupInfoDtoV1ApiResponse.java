package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 그룹 정보 DTO Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 정보 DTO 응답")
public record ProductGroupInfoDtoV1ApiResponse(
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName) {}
