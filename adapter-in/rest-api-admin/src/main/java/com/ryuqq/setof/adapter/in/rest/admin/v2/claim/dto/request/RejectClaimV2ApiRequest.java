package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * RejectClaimV2ApiRequest - 클레임 반려 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "클레임 반려 요청")
public record RejectClaimV2ApiRequest(
        @Schema(
                        description = "반려 처리자 ID",
                        example = "admin-001",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "처리자 ID는 필수입니다")
                String adminId,
        @Schema(
                        description = "반려 사유",
                        example = "반품 기간 초과로 인해 반품이 불가합니다.",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "반려 사유는 필수입니다")
                String rejectReason) {}
