package com.ryuqq.setof.application.payment.validator;

import com.ryuqq.setof.application.payment.port.out.query.ProductPriceQueryPort;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.payment.exception.PaymentErrorCode;
import com.ryuqq.setof.domain.payment.exception.PaymentException;
import com.ryuqq.setof.domain.payment.exception.ProductPriceMismatchException;
import com.ryuqq.setof.domain.payment.vo.UsedMileage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 결제 검증 Validator.
 *
 * <p>상품 가격 검증 및 마일리지 사용 규칙 검증을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentValidator {

    private static final long MILEAGE_UNIT = 100L;
    private static final long MIN_CASH_WITH_MILEAGE = 10_000L;

    private final ProductPriceQueryPort productPriceQueryPort;

    public PaymentValidator(ProductPriceQueryPort productPriceQueryPort) {
        this.productPriceQueryPort = productPriceQueryPort;
    }

    /**
     * 주문 항목들의 가격을 검증합니다.
     *
     * <p>서버에서 상품 가격을 조회하고, 도메인 OrderItem의 orderAmount와 비교합니다.
     *
     * @param orderItems 도메인 주문 항목 목록
     * @throws ProductPriceMismatchException 가격 불일치 시
     */
    public void validatePrices(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            long actualPrice = productPriceQueryPort.getCurrentPrice(item.productGroupIdValue());
            long serverCalculated = actualPrice * item.quantityValue();

            if (item.orderAmount() != serverCalculated) {
                throw new ProductPriceMismatchException(
                        item.productGroupIdValue(), item.orderAmount(), serverCalculated);
            }
        }
    }

    /**
     * 마일리지 사용 규칙을 검증합니다.
     *
     * <p>레거시 검증 규칙: (1) 100원 단위 사용, (2) 마일리지 사용 시 현금 결제액 최소 10,000원.
     *
     * @param usedMileage 사용 마일리지
     * @param payAmount 현금 결제 금액
     */
    public void validateMileageUsage(UsedMileage usedMileage, long payAmount) {
        if (!usedMileage.isUsed()) {
            return;
        }

        if (usedMileage.amount() % MILEAGE_UNIT != 0) {
            throw new PaymentException(PaymentErrorCode.INVALID_MILEAGE_UNIT);
        }

        if (payAmount > 0 && payAmount < MIN_CASH_WITH_MILEAGE) {
            throw new PaymentException(PaymentErrorCode.INSUFFICIENT_CASH_WITH_MILEAGE);
        }
    }
}
