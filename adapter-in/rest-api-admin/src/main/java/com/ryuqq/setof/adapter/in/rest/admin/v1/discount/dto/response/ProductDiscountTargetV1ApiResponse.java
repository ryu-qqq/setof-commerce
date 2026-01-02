package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/** V1 상품 할인 대상 Response */
@Schema(description = "상품 할인 대상 응답")
public record ProductDiscountTargetV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "할인 대상 ID", example = "1") Long discountTargetId,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate)
        implements DiscountTargetV1ApiResponse {

    @Override
    public String getType() {
        return "PRODUCT";
    }
}
