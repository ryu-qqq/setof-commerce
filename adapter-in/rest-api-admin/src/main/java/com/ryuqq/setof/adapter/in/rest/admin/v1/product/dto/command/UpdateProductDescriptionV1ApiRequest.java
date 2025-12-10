package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 상세 설명 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 상세 설명 수정 요청")
public record UpdateProductDescriptionV1ApiRequest(
        @Schema(description = "상세 설명", example = "상품 상세 설명입니다.") String detailDescription) {}
