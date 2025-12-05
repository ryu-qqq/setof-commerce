package com.ryuqq.setof.api.member.dto.command;

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
public record WithdrawApiRequest(@NotBlank(message = "탈퇴 사유는 필수입니다.") String reason) {}
