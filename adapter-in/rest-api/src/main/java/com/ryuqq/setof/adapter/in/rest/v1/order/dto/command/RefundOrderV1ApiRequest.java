package com.ryuqq.setof.adapter.in.rest.v1.order.dto.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 환불 주문 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeName("refundOrder")
@Schema(description = "환불 주문 수정 요청")
public record RefundOrderV1ApiRequest(
        @Schema(description = "결제 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제 ID는 필수입니다.")
                Long paymentId,
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(
                        description = "주문 상태",
                        example = "REFUNDED",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "주문 상태는 필수입니다.")
                String orderStatus,
        @Schema(description = "변경 사유", example = "고객 요청")
                @Length(max = 200, message = "사유는 200자를 넘어갈 수 없습니다.")
                String changeReason,
        @Schema(description = "변경 상세 사유", example = "단순 변심")
                @Length(max = 500, message = "사유는 500자를 넘어갈 수 없습니다.")
                String changeDetailReason,
        @Schema(description = "결제 대행사 ID", example = "portone_123") String paymentAgencyId,
        @Schema(description = "환불 계좌 정보") RefundAccountInfoV1ApiRequest refundAccountInfo)
        implements UpdateOrderV1ApiRequest {

    @Schema(description = "환불 계좌 정보")
    public record RefundAccountInfoV1ApiRequest(
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
            @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}
}
