package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 정산 처리 주문 업데이트 전략 - DELIVERY_COMPLETED → SETTLEMENT_PROCESSING
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SettlementProcessingStrategy implements OrderUpdateStrategy {

    @Override
    public List<OrderStatus> getTargetStatuses() {
        return OrderStatus.settlementProcessingStatuses();
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    @Override
    public String getStrategyName() {
        return "settlement";
    }
}
