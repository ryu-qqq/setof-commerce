package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * WithdrawalV1ApiRequest - 회원탈퇴 V1 요청 DTO.
 *
 * <p>API-DTO-001: record 기반 Request DTO.
 *
 * <p>API-DTO-002: Jakarta Validation + Swagger 어노테이션.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "회원탈퇴 요청")
public record WithdrawalV1ApiRequest(
        @Schema(description = "탈퇴 사유") @NotBlank(message = "탈퇴 사유는 필수입니다.") String reason) {}
