package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * V1 상품 그룹 상세 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 상세 응답")
public record ProductGroupDetailV1ApiResponse(
        @Schema(description = "상품 그룹 정보") ProductGroupInfoV1ApiResponse productGroup,
        @Schema(description = "상품 목록") Set<ProductFetchV1ApiResponse> products) {}
