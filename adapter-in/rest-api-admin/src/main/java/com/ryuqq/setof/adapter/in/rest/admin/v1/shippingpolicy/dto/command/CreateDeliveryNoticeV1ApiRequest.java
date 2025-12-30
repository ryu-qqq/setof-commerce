package com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * V1 배송 고지 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송 고지 생성 요청")
public record CreateDeliveryNoticeV1ApiRequest(
        @Schema(description = "배송 지역", example = "전국", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배송 지역은 필수입니다.")
                @Length(max = 200, message = "배송 지역은 200자를 넘을 수 없습니다.")
                String deliveryArea,
        @Schema(description = "배송비", example = "3000", requiredMode = Schema.RequiredMode.REQUIRED)
                @Min(value = 0, message = "배송비는 0 이상이어야 합니다.")
                @Max(value = 100000, message = "배송비는 100,000원 이하여야 합니다.")
                Integer deliveryFee,
        @Schema(
                        description = "평균 배송 기간",
                        example = "3",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @Min(value = 0, message = "평균 배송 기간은 0일 이상이어야 합니다.")
                @Max(value = 30, message = "평균 배송 기간은 30일 이하여야 합니다.")
                Integer deliveryPeriodAverage) {}
