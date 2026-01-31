package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ApproveSellerApplicationApiRequest - 셀러 입점 신청 승인 요청 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: Jakarta Validation 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 입점 신청 승인 요청 DTO")
public record ApproveSellerApplicationApiRequest(
        @Schema(description = "처리자 식별자 (이메일 등)", example = "admin@example.com", required = true)
                @NotBlank(message = "처리자 정보는 필수입니다.")
                @Size(max = 100, message = "처리자 정보는 100자 이하여야 합니다.")
                String processedBy) {}
