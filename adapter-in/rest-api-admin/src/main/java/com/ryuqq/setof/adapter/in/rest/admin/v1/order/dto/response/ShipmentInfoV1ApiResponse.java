package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배송 정보 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송 정보 응답")
public record ShipmentInfoV1ApiResponse(
        @Schema(description = "송장 번호", example = "1234567890") String invoiceNo,
        @Schema(description = "배송 회사 코드", example = "CJ") String companyCode) {
}
