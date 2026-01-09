package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 취소 요청 주문 처리 전략 - CANCEL_REQUEST 상태 주문을 Spring API로 전송
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CancelRequestStrategy implements OrderUpdateStrategy {

    @Override
    public List<OrderStatus> getTargetStatuses() {
        return OrderStatus.cancelRequestStatuses();
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now().withSecond(59).withNano(0);
    }

    @Override
    public String getStrategyName() {
        return "cancel_request";
    }

    @Override
    public boolean requiresExternalApiCall() {
        return true;
    }
}
