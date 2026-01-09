package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 가상계좌 결제 실패 주문 처리 전략 - ORDER_PROCESSING 상태 + 가상계좌 기한 초과 주문을 Spring API로 전송
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class VBankFailStrategy implements OrderUpdateStrategy {

    private static final int PERIOD_MINUTES = 15;

    @Override
    public List<OrderStatus> getTargetStatuses() {
        return OrderStatus.paymentFailStatuses();
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusMinutes(PERIOD_MINUTES).withSecond(0).withNano(0);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now().withSecond(59).withNano(0);
    }

    @Override
    public String getStrategyName() {
        return "vbank_fail";
    }

    @Override
    public boolean requiresExternalApiCall() {
        return true;
    }
}
