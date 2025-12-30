package com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 환불 고지 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "환불 고지 생성 요청")
public record CreateRefundNoticeV1ApiRequest(
        @Schema(
                        description = "국내 반품 방법",
                        example = "택배 반품",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "국내 반품 방법은 필수입니다.")
                String returnMethodDomestic,
        @Schema(
                        description = "국내 반품 택배사",
                        example = "CJ대한통운",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "국내 반품 택배사는 필수입니다.")
                @Length(max = 30, message = "국내 반품 택배사는 30자를 넘을 수 없습니다.")
                String returnCourierDomestic,
        @Schema(
                        description = "국내 반품비",
                        example = "3000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @Min(value = 0, message = "국내 반품비는 0 이상이어야 합니다.")
                @Max(value = 100000, message = "국내 반품비는 100,000원 이하여야 합니다.")
                Integer returnChargeDomestic,
        @Schema(
                        description = "국내 반품/교환 지역",
                        example = "전국",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "국내 반품/교환 지역은 필수입니다.")
                @Length(max = 200, message = "국내 반품/교환 지역은 200자를 넘을 수 없습니다.")
                String returnExchangeAreaDomestic) {}
