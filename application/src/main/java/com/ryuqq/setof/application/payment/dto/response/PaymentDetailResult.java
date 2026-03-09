package com.ryuqq.setof.application.payment.dto.response;

import com.ryuqq.setof.application.order.dto.response.OrderDetailResult;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * PaymentDetailResult - 결제 단건 상세 Application Result DTO.
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
 * @param orderProducts 주문 상품 목록 (주문별 상품/옵션/배송/환불 정보)
 * @param vBank 가상계좌 정보 (없으면 null)
 * @param buyerInfo 구매자 정보
 * @param receiverInfo 수령인 정보
 * @param refundAccount 환불 계좌 정보 (없으면 null)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentDetailResult(
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
        List<OrderDetailResult.OrderProductResult> orderProducts,
        VBankResult vBank,
        BuyerInfoResult buyerInfo,
        ReceiverInfoResult receiverInfo,
        RefundAccountResult refundAccount) {

    public static PaymentDetailResult from(PaymentFullDetail vo, List<OrderDetail> orderDetails) {
        List<OrderDetailResult.OrderProductResult> orderProducts =
                orderDetails.stream().map(OrderDetailResult.OrderProductResult::from).toList();
        VBankResult vBank =
                (vo.vBankName() != null || vo.vBankNumber() != null)
                        ? new VBankResult(
                                vo.vBankName(),
                                vo.vBankNumber(),
                                vo.vBankPaymentAmount(),
                                vo.vBankDueDate())
                        : null;

        RefundAccountResult refundAccount =
                (vo.refundBankName() != null || vo.refundAccountNumber() != null)
                        ? new RefundAccountResult(
                                vo.refundAccountId(),
                                vo.refundBankName(),
                                vo.refundAccountNumber(),
                                vo.refundAccountHolderName())
                        : null;

        return new PaymentDetailResult(
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
                orderProducts,
                vBank,
                new BuyerInfoResult(vo.buyerName(), vo.buyerEmail(), vo.buyerPhoneNumber()),
                new ReceiverInfoResult(
                        vo.receiverName(),
                        vo.receiverPhoneNumber(),
                        vo.addressLine1(),
                        vo.addressLine2(),
                        vo.zipCode(),
                        vo.country(),
                        vo.deliveryRequest(),
                        vo.phoneNumber()),
                refundAccount);
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

    /**
     * 구매자 정보.
     *
     * @param name 구매자명
     * @param email 구매자 이메일
     * @param phoneNumber 구매자 전화번호
     */
    public record BuyerInfoResult(String name, String email, String phoneNumber) {}

    /**
     * 수령인 정보.
     *
     * @param receiverName 수령인명
     * @param receiverPhoneNumber 수령인 전화번호
     * @param addressLine1 배송지 주소 1
     * @param addressLine2 배송지 주소 2
     * @param zipCode 우편번호
     * @param country 국가
     * @param deliveryRequest 배송 요청사항
     * @param phoneNumber 주문자 전화번호
     */
    public record ReceiverInfoResult(
            String receiverName,
            String receiverPhoneNumber,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber) {}

    /**
     * 환불 계좌 정보.
     *
     * @param refundAccountId 환불 계좌 ID
     * @param bankName 환불 은행명
     * @param accountNumber 환불 계좌번호
     * @param holderName 환불 계좌 예금주명
     */
    public record RefundAccountResult(
            long refundAccountId, String bankName, String accountNumber, String holderName) {}
}
