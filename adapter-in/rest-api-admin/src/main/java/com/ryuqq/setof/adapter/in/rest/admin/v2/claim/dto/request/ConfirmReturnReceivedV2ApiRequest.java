package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * ConfirmReturnReceivedV2ApiRequest - 반품 수령 확인 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "반품 수령 확인 요청")
public record ConfirmReturnReceivedV2ApiRequest(
        @Schema(
                        description = "검수 결과",
                        example = "PASS",
                        allowableValues = {"PASS", "FAIL", "PARTIAL"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "검수 결과는 필수입니다")
                String inspectionResult,
        @Schema(
                        description = "검수 메모 (검수 실패 시 사유 등)",
                        example = "상품 상태 양호",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String inspectionNote) {}
