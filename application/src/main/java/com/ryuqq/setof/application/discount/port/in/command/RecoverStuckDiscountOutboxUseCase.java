package com.ryuqq.setof.application.discount.port.in.command;

/**
 * Stuck 아웃박스 복구 유스케이스.
 *
 * <p>스케줄러에서 호출합니다. PUBLISHED 상태로 일정 시간 이상 머문 항목을 PENDING으로 복구합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RecoverStuckDiscountOutboxUseCase {

    /**
     * Stuck 아웃박스 복구.
     *
     * @param timeoutSeconds 타임아웃 기준 (초)
     * @param batchSize 배치 크기
     * @return 복구된 건수
     */
    int execute(long timeoutSeconds, int batchSize);
}
