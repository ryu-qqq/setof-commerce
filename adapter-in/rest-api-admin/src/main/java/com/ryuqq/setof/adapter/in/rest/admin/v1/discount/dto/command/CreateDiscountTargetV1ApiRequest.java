package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * V1 할인 대상 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 대상 생성 요청")
public record CreateDiscountTargetV1ApiRequest(
        @Schema(description = "발행 타입", example = "PRODUCT",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "발행 타입은 필수입니다.") String issueType,
        @Schema(description = "대상 ID 목록", example = "[1, 2, 3]",
                requiredMode = Schema.RequiredMode.REQUIRED) @Size(min = 1,
                        message = "적어도 하나 이상의 대상 ID가 필요합니다.") @NotNull(
                                message = "대상 ID 목록은 필수입니다.") List<Long> targetIds) {
}
