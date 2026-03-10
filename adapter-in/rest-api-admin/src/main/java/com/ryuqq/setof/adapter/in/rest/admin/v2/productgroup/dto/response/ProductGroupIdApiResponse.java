package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 상품 그룹 ID 응답.
 *
 * @param productGroupId 생성된 상품 그룹 ID
 */
@Schema(description = "상품 그룹 ID 응답")
public record ProductGroupIdApiResponse(
        @Schema(description = "상품 그룹 ID", example = "1") long productGroupId) {

    public static ProductGroupIdApiResponse of(long productGroupId) {
        return new ProductGroupIdApiResponse(productGroupId);
    }
}
