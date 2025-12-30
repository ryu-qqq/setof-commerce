package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 운송장 번호 변경 요청 DTO
 *
 * <p>발송 전(PENDING 상태)에만 변경 가능합니다.
 *
 * @param carrierId 새 택배사 ID
 * @param invoiceNumber 새 운송장 번호
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "운송장 번호 변경 요청")
public record ChangeInvoiceV2ApiRequest(
        @Schema(
                        description = "새 택배사 ID",
                        example = "2",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "택배사 ID는 필수입니다")
                Long carrierId,
        @Schema(
                        description = "새 운송장 번호 (10-20자)",
                        example = "9876543210987",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "운송장 번호는 필수입니다")
                @Size(min = 10, max = 20, message = "운송장 번호는 10~20자여야 합니다")
                @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "운송장 번호는 영문/숫자만 가능합니다")
                String invoiceNumber) {}
