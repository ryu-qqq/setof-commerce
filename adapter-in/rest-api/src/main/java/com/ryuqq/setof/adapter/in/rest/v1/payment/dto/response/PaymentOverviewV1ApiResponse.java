package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * PaymentOverviewV1ApiResponse - 결제 목록 개요 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param paymentId 결제 ID
 * @param paymentStatus 결제 상태
 * @param paymentMethod 결제수단 표시명
 * @param paymentDate 결제 일시
 * @param canceledDate 취소 일시
 * @param paymentAmount 결제 금액
 * @param usedMileageAmount 사용 마일리지
 * @param paymentAgencyId PG사 거래 ID
 * @param cardName 카드사명
 * @param cardNumber 카드번호 (마스킹)
 * @param orderIds 해당 결제에 묶인 주문 ID 목록
 * @param vBankAccount 가상계좌 정보 (가상계좌 결제 시에만 포함, nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "결제 개요 응답 (목록용)")
public record PaymentOverviewV1ApiResponse(
        @Schema(description = "결제 ID", example = "12345") long paymentId,
        @Schema(
                        description = "결제 상태",
                        example = "PAID",
                        allowableValues = {
                            "PAYMENT_PENDING",
                            "PAID",
                            "PAYMENT_FAILED",
                            "PAYMENT_CANCELED",
                            "REFUND_REQUESTED",
                            "REFUND_COMPLETED"
                        })
                String paymentStatus,
        @Schema(description = "결제수단 표시명", example = "신용카드") String paymentMethod,
        @Schema(description = "결제 일시", example = "2024-01-15T10:30:00") LocalDateTime paymentDate,
        @Schema(description = "취소 일시", nullable = true) LocalDateTime canceledDate,
        @Schema(description = "결제 금액", example = "50000") long paymentAmount,
        @Schema(description = "사용 마일리지", example = "1000.0") double usedMileageAmount,
        @Schema(description = "PG사 거래 ID", example = "imp_123456") String paymentAgencyId,
        @Schema(description = "카드사명", example = "신한카드") String cardName,
        @Schema(description = "카드번호 (마스킹)", example = "1234-****-****-5678") String cardNumber,
        @Schema(description = "해당 결제에 묶인 주문 ID 목록") Set<Long> orderIds,
        @Schema(description = "가상계좌 정보", nullable = true) VBankV1ApiResponse vBankAccount) {

    /**
     * VBankV1ApiResponse - 가상계좌 정보 응답.
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param paymentAmount 입금 금액
     * @param dueDate 입금 기한
     */
    @Schema(description = "가상계좌 정보")
    public record VBankV1ApiResponse(
            @Schema(description = "은행명", example = "KB국민은행") String bankName,
            @Schema(description = "계좌번호", example = "123-456-789012") String accountNumber,
            @Schema(description = "입금 금액", example = "50000") long paymentAmount,
            @Schema(description = "입금 기한", example = "2024-01-18T23:59:59")
                    LocalDateTime dueDate) {}
}
