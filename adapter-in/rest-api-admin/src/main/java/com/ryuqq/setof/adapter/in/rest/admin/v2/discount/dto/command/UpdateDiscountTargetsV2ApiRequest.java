package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * V2 할인 정책 적용 대상 수정 Request
 *
 * @param targetIds 새로운 적용 대상 ID 목록 (빈 목록 허용 - 모든 대상 제거)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "할인 정책 적용 대상 수정 요청")
public record UpdateDiscountTargetsV2ApiRequest(
        @Schema(
                        description = "적용 대상 ID 목록 (빈 목록 시 모든 대상 제거)",
                        example = "[1, 2, 3]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "적용 대상 ID 목록은 필수입니다.")
                List<Long> targetIds) {}
