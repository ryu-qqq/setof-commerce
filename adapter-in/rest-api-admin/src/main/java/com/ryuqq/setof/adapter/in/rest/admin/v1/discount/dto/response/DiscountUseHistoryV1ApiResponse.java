package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * V1 할인 사용 히스토리 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 사용 히스토리 응답")
public record DiscountUseHistoryV1ApiResponse(
        @Schema(description = "사용자 ID", example = "1") Long userId,
        @Schema(description = "사용자명", example = "홍길동") String name,
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "주문 금액", example = "100000") BigDecimal orderAmount,
        @Schema(description = "결제 ID", example = "1") Long paymentId,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "직접 할인 금액", example = "10000") BigDecimal directDiscountPrice,
        @Schema(description = "사용 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime useDate,
        @Schema(description = "총 직접 할인 금액", example = "10000")
                BigDecimal totalDirectDiscountPrice) {}
