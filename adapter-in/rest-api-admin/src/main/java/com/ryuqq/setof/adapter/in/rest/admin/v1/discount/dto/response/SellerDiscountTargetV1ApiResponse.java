package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 셀러 할인 대상 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 할인 대상 응답")
public record SellerDiscountTargetV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "할인 대상 ID", example = "1") Long discountTargetId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate)
        implements DiscountTargetV1ApiResponse {

    @Override
    public String getType() {
        return "SELLER";
    }
}
