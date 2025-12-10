package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배송 고지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송 고지 응답")
public record DeliveryNoticeV1ApiResponse(
        @Schema(description = "배송 지역", example = "전국") String deliveryArea,
        @Schema(description = "배송비", example = "3000") Long deliveryFee,
        @Schema(description = "평균 배송 기간", example = "3") Integer deliveryPeriodAverage) {
}
