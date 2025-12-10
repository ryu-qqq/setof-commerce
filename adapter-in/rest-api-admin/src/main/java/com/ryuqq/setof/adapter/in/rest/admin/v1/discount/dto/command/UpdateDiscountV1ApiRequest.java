package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 할인 정책 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 정책 수정 요청")
public record UpdateDiscountV1ApiRequest(@Schema(
        description = "할인 상세 정보") CreateDiscountV1ApiRequest.CreateDiscountDetailsV1ApiRequest discountDetails,
        @Schema(description = "대상 ID 목록", example = "[1, 2, 3]") List<Long> targetIds) {
}
