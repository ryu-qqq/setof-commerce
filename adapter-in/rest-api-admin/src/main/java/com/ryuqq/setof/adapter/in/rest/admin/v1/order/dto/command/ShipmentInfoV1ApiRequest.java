package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 배송 정보 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송 정보 요청")
public record ShipmentInfoV1ApiRequest(
        @Schema(description = "송장 번호", example = "1234567890")
                @Length(max = 80, message = "송장 번호는 80자를 넘을 수 없습니다.")
                String invoiceNo,
        @Schema(
                        description = "배송 타입",
                        example = "DOMESTIC",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "배송 타입은 필수입니다.")
                String shipmentType,
        @Schema(
                        description = "배송 회사 코드",
                        example = "CJ",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "배송 회사 코드는 필수입니다.")
                String companyCode) {}
