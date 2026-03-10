package com.ryuqq.setof.application.payment.dto.bundle;

import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import java.util.List;

/**
 * 결제 생성 번들.
 *
 * <p>Factory에서 생성한 도메인 객체들을 하나로 묶어 전달합니다.
 *
 * @param payment 결제 도메인 객체
 * @param order 주문 도메인 객체 (OrderItem 포함)
 * @param stockDeductionItems 재고 차감 항목 목록
 * @param receiverInfo 배송지 스냅샷
 * @param refundAccountSnapshot 환불 계좌 스냅샷 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentCreationBundle(
        Payment payment,
        Order order,
        List<StockDeductionItem> stockDeductionItems,
        ReceiverInfo receiverInfo,
        RefundAccountSnapshot refundAccountSnapshot) {

    public PaymentCreationBundle {
        if (payment == null) {
            throw new IllegalArgumentException("PaymentCreationBundle의 payment는 null일 수 없습니다");
        }
        if (order == null) {
            throw new IllegalArgumentException("PaymentCreationBundle의 order는 null일 수 없습니다");
        }
        if (receiverInfo == null) {
            throw new IllegalArgumentException("PaymentCreationBundle의 receiverInfo는 null일 수 없습니다");
        }
        stockDeductionItems =
                stockDeductionItems != null ? List.copyOf(stockDeductionItems) : List.of();
    }
}
