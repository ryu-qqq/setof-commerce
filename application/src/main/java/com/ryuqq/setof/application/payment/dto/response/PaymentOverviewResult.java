package com.ryuqq.setof.application.payment.dto.response;

import com.ryuqq.setof.application.order.dto.response.OrderDetailResult;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PaymentOverviewResult - 결제 목록 개요 Application Result DTO.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * <p>레거시 호환을 위해 userId, siteName, paymentMethodEnum, preDiscountAmount, totalExpectedMileageAmount,
 * orderProducts를 포함합니다.
 *
 * @param paymentId 결제 ID
 * @param paymentAgencyId PG사 거래 ID
 * @param paymentStatus 결제 상태
 * @param paymentMethodEnum 결제수단 Enum명 (CARD, KAKAO_PAY 등)
 * @param paymentMethod 결제수단 표시명
 * @param paymentDate 결제일시
 * @param canceledDate 취소일시
 * @param userId 사용자 ID
 * @param siteName 사이트명
 * @param preDiscountAmount 할인 전 금액 (주문상품 정가 합계)
 * @param paymentAmount 결제금액
 * @param usedMileageAmount 사용 마일리지
 * @param cardName 카드사명
 * @param cardNumber 카드번호
 * @param totalExpectedMileageAmount 예상 적립 마일리지
 * @param vBank 가상계좌 정보 (없으면 null)
 * @param orderProducts 주문 상품 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentOverviewResult(
        long paymentId,
        String paymentAgencyId,
        String paymentStatus,
        String paymentMethodEnum,
        String paymentMethod,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        long userId,
        String siteName,
        long preDiscountAmount,
        long paymentAmount,
        double usedMileageAmount,
        String cardName,
        String cardNumber,
        double totalExpectedMileageAmount,
        Set<Long> orderIds,
        VBankResult vBank,
        List<OrderDetailResult.OrderProductResult> orderProducts) {

    private static final Map<Long, String> PAYMENT_METHOD_ENUM_MAP =
            Map.of(
                    1L, "CARD",
                    2L, "KAKAO_PAY",
                    3L, "NAVER_PAY",
                    4L, "VBANK",
                    5L, "VBANK_ESCROW",
                    6L, "MILEAGE");

    /**
     * PaymentOverview + orderProducts → PaymentOverviewResult 변환.
     *
     * @param vo 결제 개요 도메인 VO
     * @param orderProducts 주문 상품 목록
     * @return PaymentOverviewResult
     */
    public static PaymentOverviewResult from(
            PaymentOverview vo, List<OrderDetailResult.OrderProductResult> orderProducts) {
        VBankResult vBank =
                new VBankResult(
                        vo.vBankName(),
                        vo.vBankNumber(),
                        vo.vBankPaymentAmount(),
                        vo.vBankDueDate());

        String methodEnum = resolvePaymentMethodEnum(vo.paymentMethodId());

        long preDiscount =
                orderProducts != null
                        ? orderProducts.stream()
                                .mapToLong(OrderDetailResult.OrderProductResult::regularPrice)
                                .sum()
                        : 0L;

        return new PaymentOverviewResult(
                vo.paymentId(),
                vo.paymentAgencyId(),
                vo.paymentStatus(),
                methodEnum,
                vo.paymentMethod(),
                vo.paymentDate(),
                vo.canceledDate(),
                vo.userId(),
                vo.siteName(),
                preDiscount,
                vo.paymentAmount(),
                vo.usedMileageAmount(),
                vo.cardName(),
                vo.cardNumber(),
                0,
                vo.orderIds(),
                vBank,
                orderProducts != null ? orderProducts : List.of());
    }

    private static String resolvePaymentMethodEnum(long paymentMethodId) {
        return PAYMENT_METHOD_ENUM_MAP.getOrDefault(paymentMethodId, "");
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
