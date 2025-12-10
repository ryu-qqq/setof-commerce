package com.ryuqq.setof.adapter.in.rest.member.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Withdraw API Request
 *
 * <p>회원 탈퇴 요청 DTO
 *
 * @param reason 탈퇴 사유
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "회원 탈퇴 요청")
public record WithdrawApiRequest(
        @Schema(
                        description = "탈퇴 사유",
                        example = "서비스 이용 불만족",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "탈퇴 사유는 필수입니다.")
                String reason) {}
