package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * V1 회원 탈퇴 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "회원 탈퇴 요청")
public record WithdrawV1ApiRequest(
        @Schema(
                        description = "탈퇴 동의 여부 (Y/N)",
                        example = "Y",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "탈퇴 동의 여부는 필수입니다.")
                @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 허용됩니다.")
                String agreementYn,
        @Schema(
                        description = "탈퇴 사유",
                        example = "NOT_USING",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "탈퇴 사유는 필수입니다.")
                String reason) {}
