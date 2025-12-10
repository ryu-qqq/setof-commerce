package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 주문 히스토리 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 히스토리 응답")
public record OrderHistoryV1ApiResponse(@Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "변경 사유", example = "고객 요청") String changeReason,
        @Schema(description = "변경 상세 사유", example = "단순 변심") String changeDetailReason,
        @Schema(description = "주문 상태", example = "CANCELLED") String orderStatus,
        @Schema(description = "송장 번호", example = "1234567890") String invoiceNo,
        @Schema(description = "배송 회사 코드", example = "CJ") String shipmentCompanyCode,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateDate) {
}
