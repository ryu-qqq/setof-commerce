package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * ApproveClaimV2ApiRequest - 클레임 승인 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "클레임 승인 요청")
public record ApproveClaimV2ApiRequest(
        @Schema(
                        description = "승인 처리자 ID",
                        example = "admin-001",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "처리자 ID는 필수입니다")
                String adminId) {}
