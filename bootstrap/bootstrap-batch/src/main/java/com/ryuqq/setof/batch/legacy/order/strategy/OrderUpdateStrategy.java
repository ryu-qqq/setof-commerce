package com.ryuqq.setof.batch.legacy.order.strategy;

import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 업데이트 전략 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface OrderUpdateStrategy {

    /** 처리 대상 주문 상태 목록 */
    List<OrderStatus> getTargetStatuses();

    /** 조회 시작 시간 */
    LocalDateTime getStartTime();

    /** 조회 종료 시간 */
    LocalDateTime getEndTime();

    /** 전략 식별자 */
    String getStrategyName();

    /** 외부 API 호출 필요 여부 */
    default boolean requiresExternalApiCall() {
        return false;
    }
}
