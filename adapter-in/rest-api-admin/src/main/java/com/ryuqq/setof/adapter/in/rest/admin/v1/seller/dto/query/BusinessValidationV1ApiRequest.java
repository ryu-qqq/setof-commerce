package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 사업자 등록번호 검증 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "사업자 등록번호 검증")
public record BusinessValidationV1ApiRequest(
        @Schema(description = "사업자 등록번호", example = "123-45-67890")
                String businessRegistrationNumber) {}
