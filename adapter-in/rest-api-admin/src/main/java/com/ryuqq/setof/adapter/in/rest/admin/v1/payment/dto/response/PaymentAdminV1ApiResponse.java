package com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 결제 Admin Response
 *
 * <p>Admin에서 결제 조회 시 반환되는 V1 호환 응답입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Admin 결제 응답")
public record PaymentAdminV1ApiResponse(
        @Schema(description = "결제 ID", example = "1") Long paymentId,
        @Schema(description = "주문 ID", example = "100") Long orderId,
        @Schema(description = "PG 거래 ID", example = "portone_123") String pgTransactionId,
        @Schema(description = "결제 상태", example = "APPROVED") String paymentStatus,
        @Schema(description = "결제 방법", example = "CARD") String paymentMethod,
        @Schema(description = "결제 방법 표시명", example = "카드") String paymentMethodDisplayName,
        @Schema(description = "요청 금액", example = "100000") Long requestedAmount,
        @Schema(description = "승인 금액", example = "100000") Long approvedAmount,
        @Schema(description = "결제 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime paymentDate,
        @Schema(description = "취소 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                @JsonInclude(JsonInclude.Include.NON_NULL)
                LocalDateTime cancelledDate,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "회원 ID", example = "1") Long memberId,
        @Schema(description = "구매자 정보") @JsonInclude(JsonInclude.Include.NON_NULL)
                BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "수령인 정보") @JsonInclude(JsonInclude.Include.NON_NULL)
                ReceiverInfoV1ApiResponse receiverInfo) {

    @Schema(description = "구매자 정보")
    public record BuyerInfoV1ApiResponse(
            @Schema(description = "구매자 이름", example = "홍길동") String buyerName,
            @Schema(description = "구매자 이메일", example = "buyer@example.com") String buyerEmail,
            @Schema(description = "구매자 전화번호", example = "01012345678") String buyerPhoneNumber) {}

    @Schema(description = "수령인 정보")
    public record ReceiverInfoV1ApiResponse(
            @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
            @Schema(description = "수령인 전화번호", example = "01012345678") String receiverPhoneNumber,
            @Schema(description = "주소", example = "서울시 강남구 테헤란로 123") String address,
            @Schema(description = "상세 주소", example = "124-1234") String addressDetail,
            @Schema(description = "우편번호", example = "12345") String zipCode,
            @Schema(description = "배송 메모", example = "문 앞에 놔주세요") String memo) {}
}
