package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 상품 그룹 삭제 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 삭제 요청")
public record DeleteProductGroupV1ApiRequest(
        @Schema(description = "상품 그룹 ID 목록", example = "[1, 2, 3]") List<Long> productGroupIds) {}
