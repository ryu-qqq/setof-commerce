package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 할인 정책 사용 상태 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 정책 사용 상태 수정 요청")
public record UpdateUseDiscountV1ApiRequest(
        @Schema(description = "할인 정책 ID 목록", example = "[1, 2, 3]") List<Long> discountPolicyIds,
        @Schema(description = "활성 여부 (Y/N)", example = "Y") String activeYn) {
}
