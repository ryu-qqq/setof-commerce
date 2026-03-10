package com.ryuqq.setof.application.payment.internal;

import com.ryuqq.setof.application.payment.validator.PaymentValidator;
import com.ryuqq.setof.application.stock.manager.StockDeductionManager;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * PaymentCreationCoordinator - 결제 생성 코디네이터.
 *
 * <p>마일리지 검증 → 가격 검증 → 재고 차감을 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentCreationCoordinator {

    private final PaymentValidator paymentValidator;
    private final StockDeductionManager stockDeductionManager;

    public PaymentCreationCoordinator(
            PaymentValidator paymentValidator, StockDeductionManager stockDeductionManager) {
        this.paymentValidator = paymentValidator;
        this.stockDeductionManager = stockDeductionManager;
    }

    /**
     * 마일리지 검증 + 가격 검증 + 재고 차감을 수행합니다.
     *
     * @param payment 결제 도메인 객체 (마일리지 검증용)
     * @param order 주문 도메인 객체 (OrderItem 기반 가격 검증)
     * @param stockItems 재고 차감 항목 목록
     */
    public void validateAndDeductStock(
            Payment payment, Order order, List<StockDeductionItem> stockItems) {
        paymentValidator.validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
        paymentValidator.validatePrices(order.orderItems());
        stockDeductionManager.deductAll(stockItems);
    }
}
