package com.ryuqq.setof.application.notification.port.in.command;

/**
 * PublishNotificationOutboxUseCase - PENDING 아웃박스를 SQS로 발행하는 UseCase.
 *
 * <p>스케줄러가 주기적으로 호출하여 대기 중인 아웃박스를 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PublishNotificationOutboxUseCase {

    /**
     * PENDING 아웃박스를 조회하여 SQS로 발행하고 PUBLISHED로 상태 변경.
     *
     * @return 발행된 아웃박스 건수
     */
    int execute();
}
