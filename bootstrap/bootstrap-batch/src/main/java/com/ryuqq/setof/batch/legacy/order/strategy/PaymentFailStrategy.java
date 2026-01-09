package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 결제 실패 주문 처리 전략 - ORDER_PROCESSING 상태 + 가상계좌 외 결제 수단인 주문을 Spring API로 전송
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentFailStrategy implements OrderUpdateStrategy {

    @Override
    public List<OrderStatus> getTargetStatuses() {
        return OrderStatus.paymentFailStatuses();
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now()
                .minusDays(1)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
    }

    @Override
    public String getStrategyName() {
        return "payment_fail";
    }

    @Override
    public boolean requiresExternalApiCall() {
        return true;
    }
}
