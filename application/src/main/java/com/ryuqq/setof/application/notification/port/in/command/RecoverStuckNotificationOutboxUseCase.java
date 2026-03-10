package com.ryuqq.setof.application.notification.port.in.command;

/**
 * RecoverStuckNotificationOutboxUseCase - Stuck 상태 아웃박스 복구 UseCase.
 *
 * <p>PUBLISHED 상태에서 일정 시간 이상 머문 아웃박스를 PENDING으로 복구하거나, 재시도 초과 시 FAILED로 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RecoverStuckNotificationOutboxUseCase {

    /**
     * stuck 아웃박스 복구 처리.
     *
     * @return 복구 처리된 아웃박스 건수
     */
    int execute();
}
