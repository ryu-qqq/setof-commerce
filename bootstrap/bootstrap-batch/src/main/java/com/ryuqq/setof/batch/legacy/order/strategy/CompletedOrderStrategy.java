package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 완료 처리 주문 업데이트 전략 - CANCEL_REQUEST_CONFIRMED → CANCEL_REQUEST_COMPLETED - SALE_CANCELLED →
 * SALE_CANCELLED_COMPLETED - RETURN_REQUEST_CONFIRMED → RETURN_REQUEST_COMPLETED
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CompletedOrderStrategy implements OrderUpdateStrategy {

    private static final int PERIOD_DAYS = 30;

    @Override
    public List<OrderStatus> getTargetStatuses() {
        return OrderStatus.completedStatuses();
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusDays(PERIOD_DAYS).withSecond(0).withNano(0);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now().withSecond(59).withNano(0);
    }

    @Override
    public String getStrategyName() {
        return "completed";
    }
}
