package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 환불 고지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "환불 고지 응답")
public record RefundNoticeV1ApiResponse(
        @Schema(description = "국내 반품 방법", example = "택배 반품") String returnMethodDomestic,
        @Schema(description = "국내 반품 택배사", example = "CJ대한통운") String returnCourierDomestic,
        @Schema(description = "국내 반품비", example = "3000") Integer returnChargeDomestic,
        @Schema(description = "국내 반품/교환 지역", example = "전국") String returnExchangeAreaDomestic) {}
