package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 브랜드 매핑 정보 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 매핑 정보 요청")
public record BrandMappingInfoV1ApiRequest(
        @Schema(description = "매핑 브랜드 ID", example = "mapping_123") String brandMappingId,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "브랜드 ID", example = "1") Long brandId) {
}
