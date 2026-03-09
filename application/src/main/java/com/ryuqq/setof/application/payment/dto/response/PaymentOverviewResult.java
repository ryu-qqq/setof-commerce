package com.ryuqq.setof.application.payment.dto.response;

import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * PaymentOverviewResult - 결제 목록 개요 Application Result DTO.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * <p>APP-DTO-004: Domain VO → Application Result 변환은 from() 팩토리 메서드.
 *
 * @param paymentId 결제 ID
 * @param paymentStatus 결제 상태
 * @param paymentMethod 결제수단 표시명
 * @param paymentDate 결제일시
 * @param canceledDate 취소일시
 * @param paymentAmount 결제금액
 * @param usedMileageAmount 사용 마일리지
 * @param paymentAgencyId PG사 거래 ID
 * @param cardName 카드사명
 * @param cardNumber 카드번호
 * @param orderIds 해당 결제에 묶인 주문 ID 목록
 * @param vBank 가상계좌 정보 (없으면 null)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentOverviewResult(
        long paymentId,
        String paymentStatus,
        String paymentMethod,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        long paymentAmount,
        double usedMileageAmount,
        String paymentAgencyId,
        String cardName,
        String cardNumber,
        Set<Long> orderIds,
        VBankResult vBank) {

    public static PaymentOverviewResult from(PaymentOverview vo) {
        VBankResult vBank =
                (vo.vBankName() != null || vo.vBankNumber() != null)
                        ? new VBankResult(
                                vo.vBankName(),
                                vo.vBankNumber(),
                                vo.vBankPaymentAmount(),
                                vo.vBankDueDate())
                        : null;

        return new PaymentOverviewResult(
                vo.paymentId(),
                vo.paymentStatus(),
                vo.paymentMethod(),
                vo.paymentDate(),
                vo.canceledDate(),
                vo.paymentAmount(),
                vo.usedMileageAmount(),
                vo.paymentAgencyId(),
                vo.cardName(),
                vo.cardNumber(),
                vo.orderIds(),
                vBank);
    }

    /**
     * 가상계좌 정보.
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param paymentAmount 결제금액
     * @param dueDate 입금 기한
     */
    public record VBankResult(
            String bankName, String accountNumber, long paymentAmount, LocalDateTime dueDate) {}
}
